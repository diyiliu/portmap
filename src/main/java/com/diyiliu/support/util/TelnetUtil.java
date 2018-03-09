package com.diyiliu.support.util;

import com.diyiliu.support.model.CmdCouple;
import com.diyiliu.support.thread.BackMsgThread;
import com.diyiliu.support.thread.ExchangeThread;
import org.apache.commons.net.telnet.TelnetClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Description: TelnetUtil
 * Author: DIYILIU
 * Update: 2017-08-17 09:49
 */
public class TelnetUtil {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String host;
    private int port;

    private String password;
    private String enPassword;

    private InputStream in;
    private OutputStream os;

    private BackMsgThread backMsgThread = new BackMsgThread();
    private ExchangeThread exchangeThread = new ExchangeThread();

    private TelnetClient client = new TelnetClient();
    private Queue<CmdCouple> cmdPool = new LinkedList();

    public void init() {
        logger.info("初始化连接...");

        cmdPool.add(new CmdCouple(password, ">"));
        cmdPool.add(new CmdCouple("en", "Password"));
        cmdPool.add(new CmdCouple(enPassword, "#"));
        try {
            // 打开连接
            client.connect(host, port);
            in = client.getInputStream();
            os = client.getOutputStream();

            backMsgThread.setInputStream(in);
            new Thread(backMsgThread).start();

            exchangeThread.setOs(os);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!cmdPool.isEmpty()) {
            CmdCouple couple = cmdPool.poll();
            doRunning(couple.getEndFlag(), couple.getCmd());
        }
    }

    public synchronized void doRunning(String endFlag, String input) {
        try {
            while (isRunning()) {
                Thread.sleep(1000);
            }
            run(endFlag, input);
            while (isOver()) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void run(String endFlag, String value) {
        exchangeThread.setQueue(backMsgThread.getBackMsg());
        exchangeThread.setEndFlag(endFlag);
        exchangeThread.setInputValue(value);
        // 手动设置启动(线程启动不同步)
        exchangeThread.setLive(true);

        new Thread(exchangeThread).start();
    }

    public List<String> getResults() {

        return exchangeThread.getResults();
    }


    public boolean isRunning() {
        if (exchangeThread != null) {

            return exchangeThread.isLive();
        }

        return false;
    }


    public boolean isOver() {
        if (exchangeThread != null) {

            return exchangeThread.isFlag();
        }

        return false;
    }

    public boolean isOK() {

        return client.isConnected() && client.isAvailable();
    }


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEnPassword() {
        return enPassword;
    }

    public void setEnPassword(String enPassword) {
        this.enPassword = enPassword;
    }
}
