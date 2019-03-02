package com.adyun.labelbus.bus;

/**
 * Created by Zachary
 * on 2019/3/2.
 */
public class SubscribeOption {

    private SubscribeMethod subscribeMethod;

    private Object subscribe;

    public SubscribeOption(SubscribeMethod subscribeMethod, Object subscribe) {
        this.subscribeMethod = subscribeMethod;
        this.subscribe = subscribe;
    }

    public SubscribeMethod getSubscribeMethod() {
        return subscribeMethod;
    }

    public Object getSubscribe() {
        return subscribe;
    }
}
