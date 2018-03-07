package com.diyiliu.service;

import com.diyiliu.service.task.HandlerTask;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Description: OperateService
 * Author: DIYILIU
 * Update: 2018-03-07 10:56
 */
public class OperateService extends Service {

    @Override
    protected Task createTask() {
        return new HandlerTask();
    }
}
