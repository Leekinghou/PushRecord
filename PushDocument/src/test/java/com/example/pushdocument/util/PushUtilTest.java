package com.example.pushdocument.util;

import com.example.pushdocument.entity.PushProcess;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class PushUtilTest {
    @Autowired
    private PushUtil pushUtil;

    @Test
    void sendRecord() {
        PushProcess pushProcess = new PushProcess();
        pushProcess.setId(5L);
        System.out.println(pushUtil.sendRecord(pushProcess));
    }
}