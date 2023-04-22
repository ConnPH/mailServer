package com.panghu.config;

import com.panghu.service.MailSendLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.annotation.Resource;


@Slf4j
@Configuration
public class RabbitConfig {

    @Resource
    private CachingConnectionFactory cachingConnectionFactory;


    @Resource
    private MailSendLogService mailSendLogService;


    @Bean
    RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            String correlationDataId = correlationData.getId();
            if (ack) {
                log.info("消息发送成功" + correlationDataId);
                mailSendLogService.updateStatus(correlationDataId, MailConstants.SUCCESS);
            } else {
                log.error("消息发送失败" + correlationDataId);
            }
        });

        // 消息在交换机发送到队列时的错误
        rabbitTemplate.setReturnsCallback((returnCallback) -> {
            log.info("消息发送到队列失败，响应码{}，失败原因{}，交换机{}，路由键{},消息{}",
                    returnCallback.getReplyCode(),
                    returnCallback.getReplyText(),
                    returnCallback.getExchange(),
                    returnCallback.getRoutingKey(),
                    returnCallback.getMessage().toString());
        });

        return rabbitTemplate;
    }



    @Bean
    Queue queue(){
        return new Queue(MailConstants.MAIL_QUEUE_NAME,true,false,false,null);
    }


    @Bean
    DirectExchange directExchange(){
        return new DirectExchange(MailConstants.MAIL_EXCHANGE_NAME,true,false,null);
    }


    @Bean
    Binding binding(){
        return BindingBuilder.bind(queue()).to(directExchange()).with(MailConstants.MAIL_ROUTING_KEY_NAME);
    }


}
