package com.interview._代理.jdkProxy;

import java.lang.reflect.Proxy;

/**
 * @Author HaosionChiang
 * @Date 2023/6/27
 **/
public class MyProxyFactory {


    public static Object getProxy(Object target){
        return Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces()
                ,new DebugInvocationHandler(target)
        );
    }
}
