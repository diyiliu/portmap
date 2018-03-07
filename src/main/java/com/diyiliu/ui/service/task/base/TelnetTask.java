package com.diyiliu.ui.service.task.base;

import com.diyiliu.support.util.TelnetUtil;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

/**
 * Description: TelnetTask
 * Author: DIYILIU
 * Update: 2018-03-07 14:05
 */
public abstract class TelnetTask extends Task<ObservableList> {
    protected TelnetUtil telnetUtil;


    protected void doRunning(String endFlag, String input) {
        if (!telnetUtil.isAlive()) {
            telnetUtil.init();
        }

        telnetUtil.run(endFlag, input);
        while (telnetUtil.isRunning()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
