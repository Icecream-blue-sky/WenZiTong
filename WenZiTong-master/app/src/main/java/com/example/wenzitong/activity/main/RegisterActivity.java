package com.example.wenzitong.activity.main;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.wenzitong.R;
import com.example.wenzitong.application.MyApp;
import com.example.wenzitong.entity.BaseResponse;
import com.example.wenzitong.untils.EmailCheckUtil;
import com.example.wenzitong.untils.MapGenerator;
import com.example.wenzitong.untils.PasswordCheckUtil;
import com.example.wenzitong.untils.ToastUtil;
import com.example.wenzitong.untils.retrofitUtil.RetroHttpUtil;
import com.example.wenzitong.untils.retrofitUtil.callback.AbstractCommonHttpCallback;
import com.example.wenzitong.untils.retrofitUtil.callback.AbstractRegisterHttpCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * 注册对应活动
 * @author 邹特强
 */
public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.register_account_edt)
    EditText edtRegisterAccount;
    @BindView(R.id.register_password_edt)
    EditText edtRegisterPassword;
    @BindView(R.id.register_bt)
    Button btRegister;
    @BindView(R.id.register_visitor_mode_bt)
    Button btVistorMode;
    private String account;
    private String password;
    @BindView(R.id.register_all_layout)
    LinearLayout llyRegisterAll;
    @BindView(R.id.register_get_vertification_code_bt)
    Button btGetVertificationCode;
    private String verificationCode;
    @BindView(R.id.register_input_vertification_code_edt)
    EditText edtVertificationCode;
    private MyTimer mc;
    private boolean isTouched = false;
    private boolean isEyeImgOpened = false;
    @BindView(R.id.register_eye_img)
    ImageView imgRegisterEye;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        hideActionBar();
        initViews();

    }
    private void hideActionBar(){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
    }


    /**
     * 初始化布局
     */
    private void initViews() {
        ButterKnife.bind(this);
        /**
         *倒计时功能
         */
        mc = new MyTimer(60000, 1000);
    }

    /**
     * 命令服务器发送的验证码
     */
    private void getVertificationCode() {
        /**
         * TODO:这里接入后端，获取验证码
         * TODO:卧槽，这里是异步获取信息，线程是并行的，可能response还没获取到，你就将信息返回了，这里有潜在的延迟问题
         */
        Call<BaseResponse<Object>> verificationCodeCall = RetroHttpUtil.build().verificationCodeCall(MapGenerator.generate().add("email", account));
        RetroHttpUtil.sendRequest(verificationCodeCall, new AbstractRegisterHttpCallback<BaseResponse<Object>>() {
            @Override
            public void onSuccess(BaseResponse<Object> result) {
                ToastUtil.ToastShortShow("验证码已发送！", MyApp.getGlobalContext());
            }

            @Override
            public void onFinal() {

            }
        });
    }

    /**
     * 获取用户的输入信息
     */
    private void getEditInformation() {
        account = edtRegisterAccount.getText().toString();
        password = edtRegisterPassword.getText().toString();
        verificationCode = edtVertificationCode.getText().toString().trim();
    }

    /**
     * 检测用户的输入是否有效
     *
     * @return 返回输入是否有效
     */
    private boolean checkInformation() {
        getEditInformation();
        if (!EmailCheckUtil.checkEmail(account)) {
            ToastUtil.ToastShortShow(getString(R.string.input_right_email), this);
            return false;
        } else if (!PasswordCheckUtil.checkPassword(password)) {
            return false;
        } else if (TextUtils.isEmpty(verificationCode)) {
            ToastUtil.ToastShortShow(getString(R.string.vercode_empty_error), this);
            return false;
        }
        return true;
    }

    /**
     * 注册按钮点击事件
     */
    @OnClick(R.id.register_bt)
    void onClickRegisterBt() {
        if (checkInformation()) {
            /**
             * 注册网络请求
             */
            Call<BaseResponse<Object>> registerCall = RetroHttpUtil.build().registerCall(MapGenerator.generate().add("email", account).add("password", password).add("code", verificationCode));
            RetroHttpUtil.sendRequest(registerCall, new AbstractRegisterHttpCallback<BaseResponse<Object>>() {
                @Override
                public void onSuccess(BaseResponse<Object> result) {
                    ToastUtil.ToastShortShow("注册成功",RegisterActivity.this);
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    intent.putExtra("account",account).putExtra("password",password);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFinal() {

                }
            });


        }
    }

    /**
     * 点击随便看看按钮
     */
    @OnClick(R.id.register_visitor_mode_bt)
    void onClickVisitorModeBt() {
        Intent intent2 = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent2);
        finish();
    }

    /**
     * 点击获取验证码按钮
     */
    @OnClick(R.id.register_get_vertification_code_bt)
    void onClickGetVercodeBt() {
        getEditInformation();
        /**
         * 先检测用户是否注册
         */
        Call<BaseResponse<Object>> registerCheckCall = RetroHttpUtil.build().registerCheckCall(MapGenerator.generate().add("email", account));
        RetroHttpUtil.sendRequest(registerCheckCall, new AbstractCommonHttpCallback<BaseResponse<Object>>() {
            @Override
            public void onSuccess(BaseResponse<Object> result) {
                if(result.getCode() == 0){
                    /**
                     * 所填邮箱有效的情况
                     */
                    if (EmailCheckUtil.checkEmail(account)) {
                        /**
                         * 第一次点击，获取验证码
                         */
                        if (!isTouched) {
                            /**
                             * 计时器开始计时
                             */
                            mc.start();
                            getVertificationCode();
                            isTouched = true;
                        } else {
                            if(!btGetVertificationCode.isClickable()){
                                btGetVertificationCode.setClickable(true);
                            }
                            btGetVertificationCode.setText(R.string.reget_code);
                            isTouched = false;
                        }
                    } else {
                        ToastUtil.ToastShortShow("请输入正确的邮箱地址！", RegisterActivity.this);
                    }
                }else{
                    ToastUtil.ToastShortShow("账号已注册！",RegisterActivity.this);
                }

            }

            @Override
            public void onFail() {
                //TODO:待处理
                ToastUtil.ToastShortShow("网络连接失败！", RegisterActivity.this);
            }
        });
    }

    /**
     * 点击眼睛图片显示密码
     */
    @OnClick(R.id.register_eye_img)
    void onClickEyeImg() {
        if (!isEyeImgOpened) {
            imgRegisterEye.setImageResource(R.drawable.login_register_eye_open_img);
            edtRegisterPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            isEyeImgOpened = true;
            edtRegisterPassword.setSelection(edtRegisterPassword.getText().length());
        } else {
            imgRegisterEye.setImageResource(R.drawable.login_register_eye_close_img);
            edtRegisterPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            isEyeImgOpened = false;
            edtRegisterPassword.setSelection(edtRegisterPassword.getText().length());
        }
    }

    /**
     * 计时器，用来倒计时 TODO:可优化为一个独立的计时工具
     */
    private class MyTimer extends CountDownTimer {
        private String millisUntilFinished;

        private MyTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            /**
             * 在Ui线程中能够立即运行的线程
             */
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(!btGetVertificationCode.isClickable()){
                        btGetVertificationCode.setClickable(true);
                    }
                    btGetVertificationCode.setText(R.string.reget_code);
                    mc.cancel();
                }
            });
        }

        @Override
        public void onTick(long millisUntilFinished) {
            this.millisUntilFinished = "" + millisUntilFinished / 1000;
            btGetVertificationCode.setClickable(false);
            btGetVertificationCode.setText(this.millisUntilFinished + "秒后重新获取");
        }
    }
}