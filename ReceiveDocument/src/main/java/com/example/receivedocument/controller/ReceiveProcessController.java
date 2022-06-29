package com.example.receivedocument.controller;

import com.example.receivedocument.common.Result;
import com.example.receivedocument.dto.PushProcessDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReceiveProcessController {
    @PostMapping("/process/receive")
    public Result receiveProcess(@RequestBody PushProcessDTO pushProcessDTO) {
        // 此处进行业务处理

        return Result.ok();
    }
}
