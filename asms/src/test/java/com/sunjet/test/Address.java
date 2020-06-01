package com.sunjet.test;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Administrator on 2016/10/19.
 */
public class Address {
    public void main(String[] args) {
        try {                  //使用try语句捕获可能出现的异常
            InetAddress ip;                          //创建对象
            ip = InetAddress.getLocalHost();         //实例化对象
            String localname = ip.getHostName();      //获取本机主机名
            String localip = ip.getHostAddress();     //获取本机IP地址
            System.out.println("本机名：" + localname);      //将本机名输出
            System.out.println("本地IP地址：" + localip);    //将本机名IP地址输出
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
