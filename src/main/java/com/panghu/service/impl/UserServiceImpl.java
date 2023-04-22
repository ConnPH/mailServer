package com.panghu.service.impl;

import com.panghu.config.MailConstants;
import com.panghu.mapper.UserMapper;
import com.panghu.mode.MailSendLog;
import com.panghu.mode.User;
import com.panghu.service.MailSendLogService;
import com.panghu.service.UserService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private MailSendLogService mailSendLogService;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    public int save(User user) {
        int result = userMapper.saveUser(user);

        if (result==1){
            User userMail = userMapper.selectUserById(user.getId());
            String msgId = UUID.randomUUID().toString().replace("-", "");
            MailSendLog mailSendLog = new MailSendLog();
            mailSendLog.setMsgId(msgId);
            mailSendLog.setUserId(userMail.getId());
            mailSendLog.setStatus(MailConstants.DELIVERING);
            mailSendLog.setExchange(MailConstants.MAIL_EXCHANGE_NAME);
            mailSendLog.setRouteKey(MailConstants.MAIL_ROUTING_KEY_NAME);
            mailSendLog.setTryCount(MailConstants.DEFAULT_MSG_TRY_COUNT);
            mailSendLog.setTryTime(new Date(System.currentTimeMillis() + 1000L * 60 * MailConstants.MSG_TIMEOUT));
            mailSendLog.setCreateTime(new Date());
            mailSendLogService.saveMailSendLog(mailSendLog);
            rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME,MailConstants.MAIL_ROUTING_KEY_NAME,userMail,new CorrelationData(msgId));
        }

        return result;
    }

    @Override
    public User selectUserById(Integer userId) {
        return userMapper.selectUserById(userId);
    }
}
