package com.diyiliu.model;

/**
 * Description: CmdCouple
 * Author: DIYILIU
 * Update: 2018-03-07 09:59
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
