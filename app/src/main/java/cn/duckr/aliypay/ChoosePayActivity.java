package cn.duckr.aliypay;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.duckr.aliypay.AliPay.PayResult;
import cn.duckr.aliypay.persenter.AliPayPersenter;
import cn.duckr.aliypay.persenter.AliPayPersenterImpl;
import cn.duckr.aliypay.view.Alipay_view;

public class ChoosePayActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener,Alipay_view{
    private CheckBox pay_wechat,pay_alipay;
    private static int payType=0;
    private AliPayPersenter persenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化微信支付
        final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
        //注册APP到微信
        msgApi.registerApp("wxd930ea5d5a258f4f");
        setContentView(R.layout.activity_choose_pay);
        pay_wechat= (CheckBox) findViewById(R.id.pay_wechat);
        pay_alipay= (CheckBox) findViewById(R.id.pay_alipay);
        setData();
    }

    private void setData() {
        persenter=new AliPayPersenterImpl(this,this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_pay:
                switch (payType) {
                    case 0:

                        break;
                    case 1:
                        break;
                }
                break;
            case R.id.pay_back:
                finish();
                break;
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.pay_wechat:
                payType=0;
                break;
            case R.id.pay_alipay:
                payType=1;
                break;
        }
    }

    @Override
    public void getDate(PayResult result) {
        String result1 = result.getResult();
        Toast.makeText(ChoosePayActivity.this,result1.equals("")?"用户取消":result1,Toast.LENGTH_LONG).show();
    }

    public void pay(View view) {

    }
    SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmss", Locale.getDefault());
    Date date = new Date();
    String key = format.format(date);
    public void zhifu(View view) {
        persenter.getData(new OrderEntity("测试商品",0.01f,key,"购物车2"));
    }



}
