package com.diyiliu.ui.service.task;

import com.diyiliu.support.util.TelnetUtil;
import com.diyiliu.ui.service.task.base.TelnetTask;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

/**
 * Description: OperateTask
 * Author: DIYILIU
 * Update: 2018-03-07 10:58
 */
public class OperateTask extends TelnetTask {

    public OperateTask(TelnetUtil telnetUtil) {
        this.telnetUtil = telnetUtil;
    }

    @Override
    protected ObservableList call() throws Exception {
        for (int i = 0; i < 500; i++) {
            updateProgress(i, 500);
            Thread.sleep(5);
        }

        return null;
    }
}
