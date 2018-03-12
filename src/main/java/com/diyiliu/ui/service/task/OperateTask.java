package com.diyiliu.ui.service.task;

import com.diyiliu.support.model.CmdCouple;
import com.diyiliu.support.model.MappingResult;
import com.diyiliu.support.model.Pair;
import com.diyiliu.support.util.TelnetUtil;
import com.diyiliu.ui.service.task.base.TelnetTask;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * Description: OperateTask
 * Author: DIYILIU
 * Update: 2018-03-07 10:58
 */
public class OperateTask extends TelnetTask {
    private Pair pair;

    public OperateTask(TelnetUtil telnetUtil, Pair pair) {
        this.telnetUtil = telnetUtil;
        this.pair = pair;
    }

    @Override
    protected ObservableList call() {
        ObservableList list = FXCollections.observableArrayList();
        list.add(toMapping());

        return list;
    }

    public MappingResult toMapping() {
        CmdCouple[] couples = new CmdCouple[]{new CmdCouple("conf t", "#"),
                new CmdCouple("int e0/0", "#"),
                new CmdCouple(pair.isNo() ? "no " + pair.toString() : pair.toString(), "#")};

        doRunning(couples);
        MappingResult mr = new MappingResult();
        List<String> list = telnetUtil.getResults();
        for (String rs : list) {
            if (rs.contains("ERROR:")) {

                mr.setResult(0);
                mr.setMessage(rs);
                return mr;
            }
        }

        doRunning(new CmdCouple[]{new CmdCouple("wr", "#")});
        list = telnetUtil.getResults();
        for (String rs : list) {
            if (rs.contains("OK")) {
                mr.setResult(1);
                mr.setMessage("OK");
            }
        }

        return mr;
    }
}
