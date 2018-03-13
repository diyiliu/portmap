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
import java.util.Arrays;
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

    private InputStream in;
    private OutputStream os;

    private BackMsgThread backMsgThread = new BackMsgThread();
    private ExchangeThread exchangeThread = new ExchangeThread();

    private TelnetClient client = new TelnetClient();
    private CmdCouple[] cmdPool;

    public TelnetUtil(String host, int port, String password, String enPassword) {
        this.host = host;
        this.port = port;

        cmdPool = new CmdCouple[]{new CmdCouple(password, ">"),
                new CmdCouple("en", "Password"),
                new CmdCouple(enPassword, "#")};
    }

    /**
     * 初始化登录操作
     */
    public void init() {
        // 建立连接
        connect();
        // 账号登录
        login();
    }

    /**
     * 建立Telnet 连接
     */
    public void connect() {
        logger.info("建立Telnet连接...");

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
    }

    /**
     * 登录防火墙
     */
    public void login() {
        logger.info("登录防火墙...");

        doRunning(cmdPool);
    }


    public synchronized void doRunning(CmdCouple[] cmdCouples) {
        try {
            Queue couples = new LinkedList();
            couples.addAll(Arrays.asList(cmdCouples));

            run(couples);
            while (isRunning()) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run(Queue outQueue) {
        exchangeThread.setInQueue(backMsgThread.getBackMsg());
        exchangeThread.setOutQueue(outQueue);

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

    public boolean isOK() {

        return client.isConnected() && client.isAvailable();
    }

    public void shutdown() throws IOException {

        client.disconnect();
    }

    public CmdCouple[] getCmdPool() {
        return cmdPool;
    }
}
