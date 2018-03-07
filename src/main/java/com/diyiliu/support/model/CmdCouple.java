package com.diyiliu.support.model;

/**
 * Description: CmdCouple
 * Author: DIYILIU
 * Update: 2018-03-07 13:52
 */
public class CmdCouple {

    private String cmd;

    private String endFlag;

    public CmdCouple(String cmd, String endFlag) {
        this.cmd = cmd;
        this.endFlag = endFlag;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getEndFlag() {
        return endFlag;
    }

    public void setEndFlag(String endFlag) {
        this.endFlag = endFlag;
    }
}