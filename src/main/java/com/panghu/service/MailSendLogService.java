package com.panghu.service;

import com.panghu.mode.MailSendLog;

import java.util.Date;
import java.util.List;

public interface MailSendLogService {
    void updateStatus(String correlationDataId, int status);

    int saveMailSendLog(MailSendLog mailSendLog);

    List<MailSendLog> selectList();

    int updateCount(String msgId, Date date);

}
