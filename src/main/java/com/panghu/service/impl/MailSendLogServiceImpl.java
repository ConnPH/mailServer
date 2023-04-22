package com.panghu.service.impl;

import com.panghu.mapper.MailSendLogMapper;
import com.panghu.mode.MailSendLog;
import com.panghu.service.MailSendLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Deque;
import java.util.List;

@Service
public class MailSendLogServiceImpl implements MailSendLogService {

    @Resource
    MailSendLogMapper mailSendLogMapper;

    @Override
    public void updateStatus(String correlationDataId, int status) {
        mailSendLogMapper.updateStatusByMsgId(correlationDataId,status);
    }

    @Override
    public int saveMailSendLog(MailSendLog mailSendLog) {

        return mailSendLogMapper.saveMailSendLog(mailSendLog);
    }

    @Override
    public List<MailSendLog> selectList() {

        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        return mailSendLogMapper.selectList(now);
    }

    @Override
    public int updateCount(String msgId, Date date) {

        return mailSendLogMapper.updateCount(msgId,date);
    }
}
