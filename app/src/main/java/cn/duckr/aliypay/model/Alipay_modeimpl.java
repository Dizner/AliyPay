package cn.duckr.aliypay.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.AuthTask;
import com.alipay.sdk.app.PayTask;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import cn.duckr.aliypay.AliPay.AuthResult;
import cn.duckr.aliypay.AliPay.H5PayDemoActivity;
import cn.duckr.aliypay.AliPay.OrderInfoUtil2_0;
import cn.duckr.aliypay.AliPay.PayResult;
import cn.duckr.aliypay.OrderEntity;

/**
 * Created by Dizner on 2016/11/13.
 */

public class Alipay_modeimpl implements Alipay_model {


    private Activity mActivity=null;

    public Alipay_modeimpl(Activity mActivity) {
        this.mActivity = mActivity;
    }

    /** 支付宝支付业务：入参app_id */
    public static final String APPID = "2016111302781693";

    /** 支付宝账户登录授权业务：入参pid值 */
    public static final String PID = "2088102181331778";
    /** 支付宝账户登录授权业务：入参target_id值 */
    public static final String TARGET_ID = "cvuvxo0226@sandbox.com";

    /** 商户私钥，pkcs8格式 */
    public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALv3AkanhF9mSnU+" +
            "U6sL9DimLrLVnC5IHzX7SHNyP3+NA63BcXRBoDjv0CYD1FkqI2C2aUPVx5ShOpgX" +
            "iDLKGJVYEkSZ3v7Gkg9L/cbigGEGzuUsYNzPqUrYvhYnQFDehnEpvvaNl5iX6EXx" +
            "VmSiM8HXMd8+zaxLr6qKcgxZwxZVAgMBAAECgYEAj3vjdOPznI+NTyqg2/VoHMZe" +
            "541Ah+PuT1re16Hi7w9KO51raf+eu1f1YM+Tje0ozG7ytCHBFKhu/iMYiQzU7fqa" +
            "1QnMcFuCYwJbse9utxtrUx7U2FFNdOje4LvsOa3dGGhtuvZ3p930r58vjBVsED5n" +
            "0uLRJ4XfH5uHNX+BFhECQQD4wCU24nFVFs4ejYh/BiBASyEJ4kByM0cnJ+y633fG" +
            "x+xw+HTdJh+/rLsMXA++5KDDockXCJKWdPpgvlxD9bNzAkEAwXFcAuESnKK0flD3" +
            "XgJV2NzPz63WCNCD1dZiRUdLisieR3ibQOBWknPX6w+vG7g6A9J62NfRMWC7SMI8" +
            "V5htFwJBAOo/Z8wrqjZK54f2GJgmGLDzDwXTvoTMrTC+dDv2vUDAfHRWRRcNE9DY" +
            "xLx65YkKnCoebNP4CrMNLct0+EY3VukCQCZc2deEVVGU/uCSJAyTv/yrBdd07R6A" +
            "Y7TTuFdu5S9Xy/P2miuKkgCnUqm+SYbgyTia1FwOqOD3Aw1R3++rPe8CQC2UIVSo" +
            "LVwTa0UIO1nsZ/HYDil5OSZzd/aPy2NEpDhZlG/51JrWTFYpj5lMMNVkWX+moTFi" +
            "FbdndQJ+1znZcP0=";
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(mActivity, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(mActivity, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(mActivity,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(mActivity,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                default:
                    break;
            }
        };
    };


    /**
     * 支付宝支付业务
     *
     * @param
     */
    public void payV2() {
//        if (TextUtils.isEmpty(APPID) || TextUtils.isEmpty(RSA_PRIVATE)) {
//            new AlertDialog.Builder(mActivity).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
//                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialoginterface, int i) {
//                            //
//                            mActivity.finish();
//                        }
//                    }).show();
//            return;
//        }

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
        String sign = OrderInfoUtil2_0.getSign(params, RSA_PRIVATE);
        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(mActivity);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 支付宝账户授权业务
     *
     * @param v
     */
    public void authV2(View v) {
        if (TextUtils.isEmpty(PID) || TextUtils.isEmpty(APPID) || TextUtils.isEmpty(RSA_PRIVATE)
                || TextUtils.isEmpty(TARGET_ID)) {
            new AlertDialog.Builder(mActivity).setTitle("警告").setMessage("需要配置PARTNER |APP_ID| RSA_PRIVATE| TARGET_ID")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                        }
                    }).show();
            return;
        }

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * authInfo的获取必须来自服务端；
         */
        Map<String, String> authInfoMap = OrderInfoUtil2_0.buildAuthInfoMap(PID, APPID, TARGET_ID);
        String info = OrderInfoUtil2_0.buildOrderParam(authInfoMap);
        String sign = OrderInfoUtil2_0.getSign(authInfoMap, RSA_PRIVATE);
        final String authInfo = info + "&" + sign;
        Runnable authRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造AuthTask 对象
                AuthTask authTask = new AuthTask(mActivity);
                // 调用授权接口，获取授权结果
                Map<String, String> result = authTask.authV2(authInfo, true);

                Message msg = new Message();
                msg.what = SDK_AUTH_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread authThread = new Thread(authRunnable);
        authThread.start();
    }

    /**
     * get the sdk version. 获取SDK版本号
     *
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(mActivity);
        String version = payTask.getVersion();
        Toast.makeText(mActivity, version, Toast.LENGTH_SHORT).show();
    }

    /**
     * 原生的H5（手机网页版支付切natvie支付） 【对应页面网页支付按钮】
     *
     * @param v
     */
    public void h5Pay(View v) {
        Intent intent = new Intent(mActivity, H5PayDemoActivity.class);
        Bundle extras = new Bundle();
        /**
         * url是测试的网站，在app内部打开页面是基于webview打开的，demo中的webview是H5PayDemoActivity，
         * demo中拦截url进行支付的逻辑是在H5PayDemoActivity中shouldOverrideUrlLoading方法实现，
         * 商户可以根据自己的需求来实现
         */
        String url = "http://m.taobao.com";
        // url可以是一号店或者淘宝等第三方的购物wap站点，在该网站的支付过程中，支付宝sdk完成拦截支付
        extras.putString("url", url);
        intent.putExtras(extras);
        mActivity.startActivity(intent);
    }

    @Override
    public void pay(OrderEntity orderEntity) throws UnsupportedEncodingException {

    }
}
