package com.adyun.labelbus.bus;

import com.adyun.labelbus.MainActivity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Zachary
 * on 2019/3/2.
 */
public class PSBus {

    private static volatile PSBus instance;
    //缓存表
    private Map<Class,List<SubscribeMethod>> METHOD_CACHE = new HashMap<>();
    // 执行表
    private Map<String,List<SubscribeOption>> SUBSRCIBES = new HashMap<>();
    // 反注册 辅助表
    private Map<Class,List<String>> REGISTER = new HashMap<>();

    private PSBus(){

    }

    public static PSBus getDefault() {
        if (instance == null){
            synchronized (PSBus.class){
                if (instance == null){
                    instance = new PSBus();
                }
            }
        }
        return instance;
    }

    /**
     * 注册
     * @param object
     */
    public void register(Object object){
        Class<?> subscribeClass = object.getClass();
        List<SubscribeMethod> subscribeMethods = findSubScribe(subscribeClass);

        List<String> labels = REGISTER.get(object.getClass());
        if (labels==null){
            labels = new ArrayList<>();
            REGISTER.put(subscribeClass,labels);
        }

        // 开始制作执行表
        for (SubscribeMethod subscribeMethod : subscribeMethods) {
            // 先查询执行表里是否存在
            String label = subscribeMethod.getLabel();
            if (!labels.contains(label)){
                labels.add(label);
            }

            List<SubscribeOption> subscribeOptions = SUBSRCIBES.get(label);
            if (subscribeOptions==null){
                subscribeOptions = new ArrayList<>();
                SUBSRCIBES.put(label,subscribeOptions);
            }
            subscribeOptions.add(new SubscribeOption(subscribeMethod,object));
        }

    }

    private List<SubscribeMethod> findSubScribe(Class<?> subscribeClass) {
        List<SubscribeMethod> subscribeMethods = METHOD_CACHE.get(subscribeClass);
        // 缓存中没有 为空
        if (subscribeMethods==null){
            subscribeMethods = new ArrayList<>();
            Method[] methods = subscribeClass.getDeclaredMethods();
            for (Method method : methods) {
                method.setAccessible(true);
                Subscribe subscribe = method.getAnnotation(Subscribe.class);
                if (subscribe !=null){
                    String[] value = subscribe.value();
                    for (String label : value) {
                        subscribeMethods.add(new SubscribeMethod(label,method,method.getParameterTypes()));
                    }
                }
            }

        }
        return subscribeMethods;

    }

    /**
     * 发送事件
     */
    public void post(String label,Object... params){
        List<SubscribeOption> subscribeOptions = SUBSRCIBES.get(label);
        if (subscribeOptions==null){
            return;
        }
        for (SubscribeOption subscribeOption : subscribeOptions) {
            SubscribeMethod subscribeMethod = subscribeOption.getSubscribeMethod();
            Class[] paramterClass = subscribeMethod.getParamterClass();
            Object[] realParams = new Object[paramterClass.length];
            if (params!=null){
                for (int i = 0; i < paramterClass.length; i++) {
                    if (i < params.length && paramterClass[i].isInstance(params[i])){
                        realParams[i] = params[i];
                    }else {
                        realParams[i] = null;
                    }
                }
            }
            try {
                subscribeMethod.getMethod().invoke(subscribeOption.getSubscribe(),realParams);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 反注册
     * @param object
     */
    public void unregister(Object object) {

        List<String> labels = REGISTER.get(object.getClass());
        if (labels!=null){
            for (String label : labels) {
                List<SubscribeOption> subscribeOptions = SUBSRCIBES.get(label);
                if (subscribeOptions!=null){
                    Iterator<SubscribeOption> iterator = subscribeOptions.iterator();
                    while (iterator.hasNext()){
                        SubscribeOption next = iterator.next();
                        if (next.getSubscribe()== object){
                            iterator.remove();
                        }
                    }
                }
            }
        }

    }
    public void clear(){
        METHOD_CACHE.clear();
        SUBSRCIBES.clear();
        REGISTER.clear();
    }
}
