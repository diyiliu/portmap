package com.diyiliu.ui.service;

import com.diyiliu.support.util.TelnetUtil;
import com.diyiliu.ui.service.task.DataLoadTask;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Description: DataLoadService
 * Author: DIYILIU
 * Update: 2018-03-06 14:18
 */

@Component
public class DataLoadService extends Service{

    @Resource
    private TelnetUtil telnetUtil;

    @Override
    protected Task createTask() {

        return new DataLoadTask(telnetUtil);
    }
}
