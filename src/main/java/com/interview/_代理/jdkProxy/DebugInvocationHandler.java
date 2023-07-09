package com.interview._代理.jdkProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Author HaosionChiang
 * @Date 2023/6/27
 **/
public class DebugInvocationHandler implements InvocationHandler {
    //被代理的真实对象
    private Object object;

    public DebugInvocationHandler(Object object) {
        this.object = object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("----before-----");

        Object invoke = method.invoke(object, args);

        System.out.println("----end-----");

        return invoke;
    }
}
