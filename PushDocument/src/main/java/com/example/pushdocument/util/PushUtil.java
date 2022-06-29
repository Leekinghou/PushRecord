package com.example.pushdocument.util;

import com.example.pushdocument.entity.PushProcess;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PushUtil {
    private Logger logger = LoggerFactory.getLogger(PushUtil.class);

    @Autowired
    private RestTemplate restTemplate;

    private final String url = "http://localhost:9011/process/receive";

    public boolean sendRecord(PushProcess pushProcess) {
        ResponseEntity<String> responseBody = restTemplate.postForEntity(url, pushProcess, String.class);

        String body = responseBody.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(body);
            Integer code = jsonNode.get("code").asInt();
            if (code == 200) {
                return true;
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return true;
    }
}
