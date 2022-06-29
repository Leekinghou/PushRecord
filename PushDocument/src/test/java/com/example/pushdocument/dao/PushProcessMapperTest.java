package com.example.pushdocument.dao;

import com.example.pushdocument.entity.PushProcess;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PushProcessMapperTest {
    @Autowired
    private PushProcessMapper pushProcessMapper;


    @Test
    void findPushRecordsByStateLimit() {
        List<PushProcess> pushProcesses = pushProcessMapper.findPushRecordsByStateLimit(0, 0, 500);
        for(PushProcess push: pushProcesses) {
            System.out.println(push.getId());
        }
    }

    @Test
    void countPushRecordsByState() {
        Integer integer = pushProcessMapper.countPushRecordsByState(0);
        System.out.println("flag为0的总数为" + integer);
    }
}