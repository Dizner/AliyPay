package cn.duckr.aliypay.persenter;

import android.app.Activity;

import cn.duckr.aliypay.AliPay.PayResult;
import cn.duckr.aliypay.model.AlipayService;
import cn.duckr.aliypay.OrderEntity;
import cn.duckr.aliypay.view.Alipay_view;

/**
 * Created by Dizner on 2016/11/16.
 */

public class AliPayPersenterImpl implements AliPayPersenter {
    AlipayService service;
    Alipay_view view;
//    Alipay_modeimpl alipay_model;
    private Activity mActivity;

    public AliPayPersenterImpl(Alipay_view view,Activity mActivity) {
        this.view = view;
        this.mActivity=mActivity;
        service=new AlipayService(this,mActivity);
//        alipay_model=new Alipay_modeimpl(mActivity);
    }

    @Override
    public void setData(Object result) {
        view.getDate((PayResult) result);
    }

    @Override
    public void getData(OrderEntity orderEntity) {
        try {
            service.pay(orderEntity);
//            alipay_model.payV2();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
