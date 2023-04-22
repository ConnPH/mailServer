package com.panghu.task;

import com.panghu.config.MailConstants;
import com.panghu.mode.MailSendLog;
import com.panghu.mode.User;
import com.panghu.service.MailSendLogService;
import com.panghu.service.UserService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Component
public class MailSendLogTask {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private MailSendLogService mailSendLogService;

    @Resource
    private UserService userService;


    // 10秒扫描一次数据库
    @Scheduled(cron = "0/10 * * * * ?")
    public void taskMailSend() {
        // 查询出所有符合重试的数据 status=0 tryTime< 当前时间 nowData();
        List<MailSendLog> mailSendLogList = mailSendLogService.selectList();

        mailSendLogList.forEach(mailSendLog -> {
            // 判断重试次数限制
            String msgId = mailSendLog.getMsgId();
            if (mailSendLog.getTryCount() >= MailConstants.MAX_TRY_COUNT) {
                // 不再进行重试直接标记为次消息失败
                mailSendLogService.updateStatus(msgId, MailConstants.FAILURE);
            } else {
                mailSendLogService.updateCount(msgId, new Date());
                // 重试发送消息
                // 根据msgId定位到用户
                User user = userService.selectUserById(mailSendLog.getUserId());
                rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME, MailConstants.MAIL_ROUTING_KEY_NAME, user, new CorrelationData(msgId));
            }
        });

    }
}
