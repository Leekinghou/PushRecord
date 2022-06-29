package com.example.pushdocument.service;

import java.util.concurrent.ExecutionException;

public interface PushProcessService {
    void pushData() throws ExecutionException, InterruptedException;
}
