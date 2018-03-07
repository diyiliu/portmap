package com.diyiliu.support.model;

/**
 * Description: Host
 * Author: DIYILIU
 * Update: 2017-08-16 14:44
 */
public class Host implements Comparable<Host>{

    private String ip;
    private String port;

    public Host() {

    }

    public Host(String ip, String port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    @Override
    public int compareTo(Host o) {
        int i = compareIp(o.getIp(), ip);

        if (i == 0){
            return o.getPort().compareTo(port);
        }
        return i;
    }

    public int compareIp(String ip1, String ip2){

        int result = 0;
        if (!ip1.contains(".")){

            result = -1;
        }else if (!ip2.contains(".")){

            result = 1;
        }else {
            String[] array1 = ip1.split("\\.");
            String[] array2 = ip2.split("\\.");


            for (int i = 0; i < array1.length; i++){
                if (Integer.parseInt(array1[i]) < Integer.parseInt(array2[i])){

                    result = -1;
                }else if (Integer.parseInt(array1[i]) > Integer.parseInt(array2[i])){

                    result = 1;
                }
            }
        }

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Host){
            Host h = (Host) obj;
            if (h.getIp().equals(ip) && h.port.equals(port)){
                return true;
            }else{
                return false;
            }
        }

        return super.equals(obj);
    }
}
