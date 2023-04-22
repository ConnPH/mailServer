package com.panghu.controller;

import com.panghu.config.MailConstants;
import com.panghu.mode.User;
import com.panghu.util.EmailUtils;
import com.panghu.util.RedisUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 发送邮件
 */
@Slf4j
@Component
public class MailSendLogController {

    @Resource
    EmailUtils emailUtils;

    @Resource
    JavaMailSender javaMailSender;

    @Resource
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    String rootEmail;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @RabbitListener(queues = MailConstants.MAIL_QUEUE_NAME)
    public void handler(Message message, Channel channel) throws IOException {
        User user = (User) message.getPayload();
        MessageHeaders headers = message.getHeaders();
        Long tag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        String msgId = (String) headers.get("spring_returned_message_correlation");

        /**
         * 处理rabbitmq消费幂等性
         */
        if (stringRedisTemplate.opsForHash().entries("mail-log").containsKey(msgId)) {
            // redis中包含该key 说明该消息已经被消费过
            log.info(msgId + "消息已经被消费");
            /*
             * 此时消息队列的数据已经给你了，直接return 消息又会回到队列里面去，这里手动确认一下消息接受成功，不回消息队列
             * 其实没消费，但是这里告诉消息中间件我们消费了，用tag标记
             */
            channel.basicAck(tag, false);
            return;
        }

        try {
            emailUtils.sendAttachmentsMail(javaMailSender,user.getEmail(),"我是图图小淘气",
                    "我是一个爱动脑筋的小孩哦\n" +
                    "\n" +
                    "爸爸妈妈准备好了吗\n" +
                    "\n" +
                    "我是图图小淘气 爱吃糖果巧克力\n" +
                    "\n" +
                    "为什么感冒会流鼻涕为什么小熊吃蜂蜜\n" +
                    "\n" +
                    "我家住在翻斗花园我上翻斗幼儿园\n" +
                    "\n" +
                    "为什么我们要过年又为什么过年贴对联\n" +
                    "\n" +
                    "图图耳朵圆又大 牛爷爷我最害怕\n" +
                    "\n" +
                    "为什么小孩会长大又为什么女孩是长发\n" +
                    "\n" +
                    "夜晚到底有多黑 北风呼呼往哪吹\n" +
                    "\n" +
                    "小鸟为什么会飞又为什么水要倒进杯\n" +
                    "\n" +
                    "我的猫咪叫小怪 五颜六色很可爱\n" +
                    "\n" +
                    "有时候它却很坏总偷吃妈妈做的菜\n" +
                    "\n" +
                    "我还会动耳神功和其他小孩不普通\n" +
                    "\n" +
                    "友谊真是奇怪东东思考总面对天空","/Users/ruhua/Desktop/Idea_Project/mail-service/src/main/resources/static/img.png");
            channel.basicAck(tag, false);
            stringRedisTemplate.opsForHash().put("mail-log", msgId, "");
            stringRedisTemplate.expire("mail-log", 1, TimeUnit.HOURS);
            log.info(msgId + "邮件发送成功");
        } catch (Exception e) {
            e.printStackTrace();
            // 回到消息队列里面重新等待下次消费
            channel.basicNack(tag, false, true);
            log.info("邮件发送失败");
        }


    }
}
