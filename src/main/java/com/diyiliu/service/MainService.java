package com.diyiliu.service;

import com.diyiliu.service.task.LoadTask;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Description: MainService
 * Author: DIYILIU
 * Update: 2018-03-06 14:18
 */

public class MainService extends Service{

    @Override
    protected Task createTask() {

        return new LoadTask();
    }
}
