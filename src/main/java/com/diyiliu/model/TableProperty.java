package com.diyiliu.model;

/**
 * Description: TableProperty
 * Author: DIYILIU
 * Update: 2018-03-06 16:36
 */
public class TableProperty {
    private Integer index;
    private String protocol;
    private String insideHost;
    private String insidePort;
    private String outsideHost;
    private String outsidePort;

    public TableProperty(Integer index, String protocol,
                         String insideHost, String insidePort,
                         String outsideHost, String outsidePort) {
        this.index = index;
        this.protocol = protocol;
        this.insideHost = insideHost;
        this.insidePort = insidePort;
        this.outsideHost = outsideHost;
        this.outsidePort = outsidePort;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getProtocol() {
        return protocol.toUpperCase();
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getInsideHost() {
        return insideHost;
    }

    public void setInsideHost(String insideHost) {
        this.insideHost = insideHost;
    }

    public String getInsidePort() {
        return insidePort;
    }

    public void setInsidePort(String insidePort) {
        this.insidePort = insidePort;
    }

    public String getOutsideHost() {
        return outsideHost;
    }

    public void setOutsideHost(String outsideHost) {
        this.outsideHost = outsideHost;
    }

    public String getOutsidePort() {
        return outsidePort;
    }

    public void setOutsidePort(String outsidePort) {
        this.outsidePort = outsidePort;
    }
}
