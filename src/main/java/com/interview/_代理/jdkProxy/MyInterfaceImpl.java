package com.interview._代理.jdkProxy;

/**
 * @Author HaosionChiang
 * @Date 2023/6/27
 **/
public class MyInterfaceImpl implements MyInterface{
    @Override
    public void sendMsg() {
        System.out.println("发送消息!");
    }
}
