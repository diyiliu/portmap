package com.diyiliu.ui.service;

import com.diyiliu.support.util.TelnetUtil;
import com.diyiliu.ui.service.task.OperateTask;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Description: OperateService
 * Author: DIYILIU
 * Update: 2018-03-07 10:56
 */

@Component
public class OperateService extends Service {

    @Resource
    private TelnetUtil telnetUtil;

    @Override
    protected Task createTask() {
        return new OperateTask(telnetUtil);
    }
}
