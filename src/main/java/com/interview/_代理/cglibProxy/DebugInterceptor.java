package com.interview._代理.cglibProxy;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @Author HaosionChiang
 * @Date 2023/6/27
 **/
public class DebugInterceptor implements MethodInterceptor {
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("---befors---");
        Object res = methodProxy.invokeSuper(o, objects);
        System.out.println("---afters---");
        return res;
    }
}
