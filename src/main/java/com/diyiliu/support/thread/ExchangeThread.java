package com.diyiliu.support.thread;

import com.diyiliu.support.model.CmdCouple;
import com.diyiliu.support.util.SpringUtil;
import com.diyiliu.support.util.TelnetUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * Description: ExchangeThread
 * Author: DIYILIU
 * Update: 2017-08-17 09:32
 */
public class ExchangeThread implements Runnable {


    private OutputStream os;
    // 输出队列
    private Queue<CmdCouple> outQueue;

    // 备份
    private Queue<CmdCouple> backUpQueue = new LinkedList();

    // 收到返回结果队列
    private Queue<String> inQueue;


    public ExchangeThread() {

    }

    private List<String> results = new ArrayList<>();

    private boolean live = false;

    @Override
    public void run() {
        backUpQueue.clear();
        results.clear();
        live = true;

        CmdCouple couple = null;
        String cmd = "";
        String content = "";
        // 单个指令 执行情况
        boolean point = true;
        try {
            while (true) {
                if (!inQueue.isEmpty()) {
                    content = inQueue.poll();
                    results.add(content);

                    System.out.println(content);
                } else {
                    if (!point || outQueue.isEmpty()) {
                        write(" ", os);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        couple = outQueue.poll();
                        backUpQueue.add(couple);

                        cmd = couple.getCmd();

                        System.out.println("输入:" + cmd);
                        write(cmd, os);
                        point = false;
                    }
                }

                if (!point) {
                    String endFlag = couple.getEndFlag();
                    if (content.contains(endFlag) &&
                            !content.contains(endFlag + " " + cmd.split(" ")[0])) {

                        //System.out.println("输入完成!");
                        point = true;
                    }

                    if (point && outQueue.isEmpty()) {
                        live = false;

                        System.out.println("指令结束!");
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                TelnetUtil telnetUtil = SpringUtil.getBean("telnetUtil");
                telnetUtil.connect();

                CmdCouple[] couples = telnetUtil.getCmdPool();
                Queue<CmdCouple> queue = new LinkedList();
                // 登录指令
                queue.addAll(Arrays.asList(couples));
                // 操作指令
                queue.addAll(backUpQueue);
                queue.addAll(outQueue);

                telnetUtil.run(queue);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    public void setOutQueue(Queue<CmdCouple> outQueue) {
        this.outQueue = outQueue;
    }

    public void setInQueue(Queue<String> inQueue) {
        this.inQueue = inQueue;
    }

    public void setResults(List<String> results) {
        this.results = results;
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

    public List<String> getResults() {
        return results;
    }
}
