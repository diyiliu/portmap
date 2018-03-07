package com.diyiliu.util;

import com.diyiliu.model.CmdCouple;
import com.diyiliu.thread.BackMsgThread;
import com.diyiliu.thread.ExchangeThread;
import org.apache.commons.net.telnet.TelnetClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Queue;

/**
 * Description: TelnetUtil
 * Author: DIYILIU
 * Update: 2017-08-17 09:49
 */
public class TelnetUtil {
    private String host;
    private int port;

    private BackMsgThread backMsgThread;
    private ExchangeThread exchangeThread;

    private TelnetClient client = new TelnetClient();

    private Queue<CmdCouple> cmdPool = new LinkedList();

    public TelnetUtil(Properties properties) {
        this.host = (String) properties.get("host");
        this.port = Integer.parseInt((String) properties.get("port"));

        cmdPool.add(new CmdCouple((String) properties.get("pw"), ">"));
        cmdPool.add(new CmdCouple("en", "Password"));
        cmdPool.add(new CmdCouple((String) properties.get("pw.en"), "#"));
    }

    public void init() {
        try {
            client.connect(host, port);
            InputStream in = client.getInputStream();
            OutputStream os = client.getOutputStream();

            backMsgThread = new BackMsgThread(in);
            new Thread(backMsgThread).start();

            exchangeThread = new ExchangeThread(os);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!cmdPool.isEmpty()) {
            CmdCouple couple = cmdPool.poll();
            doRunning(couple.getEndFlag(), couple.getCmd());
        }
    }

    public void doRunning(String endFlag, String input){
        run(endFlag, input);
        while (isRunning()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void run(String endFlag, String value) {
        exchangeThread.setQueue(backMsgThread.getBackMsg());
        exchangeThread.setEndFlag(endFlag);
        exchangeThread.setInputValue(value);

        new Thread(exchangeThread).start();
        exchangeThread.setLive(true);
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

    public boolean isAlive() {

        return client.isAvailable();
    }
}
