package com.diyiliu.support.thread;

import com.diyiliu.support.util.SpringUtil;
import com.diyiliu.support.util.TelnetUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Description: ExchangeThread
 * Author: DIYILIU
 * Update: 2017-08-17 09:32
 */
public class ExchangeThread implements Runnable {

    private OutputStream os;
    private String input;
    private Queue queue;
    private String endFlag;

    public ExchangeThread(OutputStream os) {
        this.os = os;
    }

    public ExchangeThread(OutputStream os, String input, Queue queue, String endFlag) {
        this.os = os;
        this.input = input;
        this.queue = queue;
        this.endFlag = endFlag;
    }

    private List<String> results = new ArrayList<>();

    private boolean live = false;

    @Override
    public void run() {
        results.clear();
        live = true;

        LinkedList<String> inputList = new LinkedList();
        inputList.add(input);

        String input = "";
        String content = "";
        boolean flag = true;
        while (flag) {
            try {
                if (!queue.isEmpty()) {
                    content = (String) queue.poll();
                    results.add(content);

                    System.out.println(content);
                } else {
                    if (inputList.isEmpty()){
                        write(" ", os);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else {
                        input = inputList.poll();
                        System.out.println("输入:" + input);
                        write(input, os);
                    }
                }

                if (inputList.isEmpty() && content.contains(endFlag)) {
                    if (!content.contains(endFlag + " " + input.split(" ")[0])){
                        System.out.println("输入完成!");
                        live = false;
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                flag = false;
                try {
                    TelnetUtil telnetUtil = SpringUtil.getBean("telnetUtil");
                    telnetUtil.init();
                    telnetUtil.run(endFlag, input);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public void setInputValue(String input) {
        this.input = input;
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }

    public void setEndFlag(String endFlag) {
        this.endFlag = endFlag;
    }

    /**
     * 写入命令方法
     *
     * @param cmd
     * @param os
     */
    public void write(String cmd, OutputStream os) throws IOException{
            cmd += "\n";
            os.write(cmd.getBytes());
            os.flush();
    }

    public boolean isLive() {
        return live;
    }

    public List<String> getResults() {
        return results;
    }
}
