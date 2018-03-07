package com.diyiliu.ui.service.task;

import com.diyiliu.support.model.Host;
import com.diyiliu.support.model.Pair;
import com.diyiliu.support.model.TableProperty;
import com.diyiliu.support.util.TelnetUtil;
import com.diyiliu.ui.service.task.base.TelnetTask;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Description: DataLoadTask
 * Author: DIYILIU
 * Update: 2018-03-06 14:41
 */

public class DataLoadTask extends TelnetTask {
    public DataLoadTask(TelnetUtil telnetUtil) {
        this.telnetUtil = telnetUtil;
    }

    @Override
    protected ObservableList call() {
        List<Pair> pairList = getList();

        ObservableList list = FXCollections.observableArrayList();

        for (int i = 0; i < pairList.size(); i++) {
            Pair pair = pairList.get(i);
            list.add(new TableProperty(i + 1, pair.getProtocol(),
                    pair.getInside().getIp(), pair.getInside().getPort(),
                    pair.getOutside().getIp(), pair.getOutside().getPort()));
        }

        return list;
    }

    public List<Pair> getList() {
        doRunning("#", "sh ru");

        List list = telnetUtil.getResults();
        List<Pair> pairList = toListPair(list);
        Collections.sort(pairList);

        return pairList;
    }

    /**
     * String集合转Pair集合
     *
     * @param l
     * @return
     */
    private List<Pair> toListPair(List<String> l) {
        List list = new ArrayList();
        for (int i = 0; i < l.size(); i++) {
            String content = l.get(i);
            if (isMatch(content)) {
                Pair p = dataFormat(content);
                list.add(p);
            }
        }

        return list;
    }

    private boolean isMatch(String content) {
        String regex = "^static \\(inside,outside\\) [tcp|udp][\\s\\S]*?";

        return Pattern.matches(regex, content);
    }

    private Pair dataFormat(String content) {
        String[] array = content.split(" ");
        if (array.length < 7) {
            return null;
        }

        String protocol = array[2];
        Host inside = new Host(array[5], array[6]);
        Host outside = new Host(array[3], array[4]);

        Pair p = new Pair();
        p.setProtocol(protocol);
        p.setInside(inside);
        p.setOutside(outside);

        return p;
    }
}
