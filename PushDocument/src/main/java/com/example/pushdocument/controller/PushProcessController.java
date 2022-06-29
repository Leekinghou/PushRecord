package com.example.pushdocument.controller;

import com.example.pushdocument.result.Result;
import com.example.pushdocument.service.PushProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
public class PushProcessController {
    @Autowired
    private PushProcessService pushProcessService;

    private Logger logger = LoggerFactory.getLogger(PushProcessController.class);

    /**
     * 推送数据接口
     * @return
     */
    @GetMapping("/push")
    public Result pushData() {
        try {
            pushProcessService.pushData();
        } catch (ExecutionException | InterruptedException e) {
            logger.error("推送异常", e);
        }
        return Result.ok();
    }
}


