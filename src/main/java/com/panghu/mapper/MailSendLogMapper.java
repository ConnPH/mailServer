package com.panghu.mapper;

import com.panghu.mode.MailSendLog;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface MailSendLogMapper {
    void updateStatusByMsgId(@Param("msgId") String correlationDataId, @Param("status") int status);

    int saveMailSendLog(MailSendLog mailSendLog);

    List<MailSendLog> selectList(String now);

    int updateCount(@Param("msgId") String msgId, @Param("date") Date date);
}
