package cn.duckr.aliypay.persenter;

import cn.duckr.aliypay.OrderEntity;

/**
 * Created by Dizner on 2016/11/16.
 */

public interface AliPayPersenter<E> {
    void setData(E result);
    void getData(OrderEntity orderEntity);
}
