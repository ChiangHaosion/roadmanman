package com.interview._代理.cglibProxy;

import com.interview._代理.jdkProxy.MyInterfaceImpl;

/**
 * @Author HaosionChiang
 * @Date 2023/6/27
 **/
public class Main {

    public static void main(String[] args) {
        MyDebugCls proxy = (MyDebugCls)CglibProxyFactory.getProxy(MyDebugCls.class);
        proxy.send();

    }
}
