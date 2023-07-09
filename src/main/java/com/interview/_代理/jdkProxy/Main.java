package com.interview._代理.jdkProxy;

/**
 * @Author HaosionChiang
 * @Date 2023/6/27
 **/
public class Main {
    public static void main(String[] args) {
        MyInterfaceImpl myInterface = new MyInterfaceImpl();
        MyInterface proxy =(MyInterface) MyProxyFactory.getProxy(myInterface);
        proxy.sendMsg();
    }
}
