package com.diyiliu.ui.service.task.base;

import com.diyiliu.support.model.CmdCouple;
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
        if (!telnetUtil.isOK()) {
            telnetUtil.init();
        }

        try {
            while (telnetUtil.isRunning()) {
                Thread.sleep(1000);
            }

            telnetUtil.doRunning(new CmdCouple[]{new CmdCouple(input, endFlag)});
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
