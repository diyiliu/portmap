package com.diyiliu.service.task;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;

/**
 * Description: HandlerTask
 * Author: DIYILIU
 * Update: 2018-03-07 10:58
 */
public class HandlerTask extends Task<ObservableList> {


    @Override
    protected ObservableList call() throws Exception {
        for (int i = 0; i < 500; i++) {
            updateProgress(i, 500);
            Thread.sleep(5);
        }

        return null;
    }
}
