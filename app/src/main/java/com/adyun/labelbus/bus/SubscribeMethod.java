package com.adyun.labelbus.bus;

import java.lang.reflect.Method;

/**
 * Created by Zachary
 * on 2019/3/2.
 */
public class SubscribeMethod {

    private String label;
    // 方法
    private Method method;
    // 参数
    private Class[] paramterClass;

    public SubscribeMethod(String label, Method method, Class[] paramterClass) {
        this.label = label;
        this.method = method;
        this.paramterClass = paramterClass;
    }

    public String getLabel() {
        return label;
    }

    public Method getMethod() {
        return method;
    }

    public Class[] getParamterClass() {
        return paramterClass;
    }
}
