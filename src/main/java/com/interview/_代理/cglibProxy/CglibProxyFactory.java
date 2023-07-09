package com.interview._代理.cglibProxy;

import net.sf.cglib.proxy.Enhancer;

/**
 * @Author HaosionChiang
 * @Date 2023/6/27
 **/
public class CglibProxyFactory {

    public static Object getProxy(Class<?>  obj) {
        Enhancer enhancer = new Enhancer();
        enhancer.setClassLoader(obj.getClassLoader());
        enhancer.setSuperclass(obj);
        enhancer.setCallback(new DebugInterceptor());
        return enhancer.create();
    }
}
