package com.diyiliu.model;

/**
 * Description: Pair
 * Author: DIYILIU
 * Update: 2017-08-16 19:16
 */
public class Pair implements Comparable<Pair>{

    private String protocol;
    private Host inside;
    private Host outside;

    public Host getInside() {
        return inside;
    }

    public void setInside(Host inside) {
        this.inside = inside;
    }

    public Host getOutside() {
        return outside;
    }

    public void setOutside(Host outside) {
        this.outside = outside;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public int compareTo(Pair o) {

        return o.getInside().compareTo(inside);
    }

    public Object[] toArray(int i){

        Object[] objects = new Object[6];
        objects[0] = i;
        objects[1] = protocol;
        objects[2] = inside.getIp();
        objects[3] = inside.getPort();
        objects[4] = outside.getIp();
        objects[5] = outside.getPort();

        return objects;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer("static (inside,outside) ");
        buffer.append(protocol).append(" ");
        buffer.append(outside.getIp()).append(" ").append(outside.getPort()).append(" ");
        buffer.append(inside.getIp()).append(" ").append(inside.getPort()).append(" ");
        buffer.append("netmask").append(" ").append("255.255.255.255");

        return  buffer.toString();
    }
}
