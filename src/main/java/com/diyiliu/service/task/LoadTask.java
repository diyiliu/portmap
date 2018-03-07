package com.diyiliu.service.task;

import com.diyiliu.model.Host;
import com.diyiliu.model.Pair;
import com.diyiliu.model.TableProperty;
import com.diyiliu.util.TelnetUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * Description: LoadTask
 * Author: DIYILIU
 * Update: 2018-03-06 14:41
 */
public class LoadTask extends Task<ObservableList> {
    private TelnetUtil telnetUtil;

    public LoadTask() {
        Properties properties = new Properties();

        try (InputStream in = ClassLoader.getSystemResourceAsStream("config.properties")) {
            properties.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }

        telnetUtil = new TelnetUtil(properties);
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


    public void doRunning(String endFlag, String input) {
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
    public List<Pair> toListPair(List<String> l) {
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

    public boolean isMatch(String content) {
        String regex = "^static \\(inside,outside\\) [tcp|udp][\\s\\S]*?";

        return Pattern.matches(regex, content);
    }

    public Pair dataFormat(String content) {
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
