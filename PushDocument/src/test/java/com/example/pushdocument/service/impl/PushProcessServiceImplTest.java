package com.example.pushdocument.service.impl;

import com.example.pushdocument.service.PushProcessService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PushProcessServiceImplTest {
    @Autowired
    private PushProcessService pushProcessService;

    @Test
    void pushData() {
    try {
        pushProcessService.pushData();
    } catch (ExecutionException e) {
        e.printStackTrace();
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}
}