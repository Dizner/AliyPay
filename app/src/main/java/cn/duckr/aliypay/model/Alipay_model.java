package cn.duckr.aliypay.model;

import java.io.UnsupportedEncodingException;

import cn.duckr.aliypay.OrderEntity;

/**
 * Created by Dizner on 2016/11/13.
 */

public interface Alipay_model {
    void pay(OrderEntity orderEntity) throws UnsupportedEncodingException;
}
