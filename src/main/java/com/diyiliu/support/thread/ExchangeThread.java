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

    public ExchangeThread() {

    }

    private List<String> results = new ArrayList<>();

    private boolean live = false;
    private boolean flag = false;

    @Override
    public void run() {
        results.clear();
        live = true;
        flag = true;

        LinkedList<String> inputList = new LinkedList();
        inputList.add(input);

        String input = "";
        String content = "";
        try {
            while (flag) {
                if (!queue.isEmpty()) {
                    content = (String) queue.poll();
                    results.add(content);

                    System.out.println(content);
                } else {
                    if (inputList.isEmpty()) {
                        write(" ", os);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        input = inputList.poll();
                        System.out.println("输入:" + input);
                        write(input, os);
                    }
                }

                if (inputList.isEmpty() && content.contains(endFlag)) {
                    if (!content.contains(endFlag + " " + input.split(" ")[0])) {
                        System.out.println("输入完成!");
                        live = false;
                        flag = false;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
            try {
                TelnetUtil telnetUtil = SpringUtil.getBean("telnetUtil");
                telnetUtil.init();
                telnetUtil.doRunning(endFlag, input);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
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


    public void setOs(OutputStream os) {
        this.os = os;
    }

    /**
     * 写入命令方法
     *
     * @param cmd
     * @param os
     */
    public void write(String cmd, OutputStream os) throws IOException {
        cmd += "\n";
        os.write(cmd.getBytes());
        os.flush();
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    /**
     * 指令结束标识
     *
     * @return
     */
    public boolean isLive() {
        return live;
    }

    /**
     * 线程结束标志
     *
     * @return
     */
    public boolean isFlag() {
        return flag;
    }

    public List<String> getResults() {
        return results;
    }
}
