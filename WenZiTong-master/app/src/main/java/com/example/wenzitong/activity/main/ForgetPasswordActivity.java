package com.example.wenzitong.activity.main;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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
 * 忘记密码活动
 */
public class ForgetPasswordActivity extends AppCompatActivity {
    @BindView(R.id.forget_password_account_edt)
    EditText edtForgetAccount;
    @BindView(R.id.forget_password_input_verification_code_edt)
    EditText edtInputVerCode;
    @BindView(R.id.forget_password_get_verification_code_bt)
    Button btGetVerCode;
    @BindView(R.id.forget_password_password_edt)
    EditText edtNewPassword;
    @BindView(R.id.forget_password_finish_bt)
    Button btFinish;
    @BindView(R.id.forget_password_eye_img)
    ImageView imgForgetEye;
    private MyTimer mc;
    private String account;
    private String password;
    private String verificationCode;
    private boolean isTouched = false;
    private boolean isEyeImgOpened = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        hideActionBar();
        initViews();
    }

    private void hideActionBar(){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
    }


    private void initViews() {
        ButterKnife.bind(this);
        mc = new MyTimer(60000, 1000);
    }

    @OnClick(R.id.forget_password_get_verification_code_bt)
    void onClickGetVerCodeBt() {
        getEditInformation();
        /**
         * 先检测用户是否注册
         */
        Call<BaseResponse<Object>> registerCheckCall = RetroHttpUtil.build().registerCheckCall(MapGenerator.generate().add("email", account));
        RetroHttpUtil.sendRequest(registerCheckCall, new AbstractCommonHttpCallback<BaseResponse<Object>>() {
            @Override
            public void onSuccess(BaseResponse<Object> result) {
                /**
                 * 账号已注册才可修改密码
                 */
                if (result.getCode() == 1) {
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
                            if(!btGetVerCode.isClickable()){
                                btGetVerCode.setClickable(true);
                            }
                            btGetVerCode.setText(R.string.reget_code);
                            isTouched = false;
                        }
                    } else {
                        ToastUtil.ToastShortShow("请输入正确的邮箱地址！", ForgetPasswordActivity.this);
                    }
                } else {
                    ToastUtil.ToastShortShow("账号未注册，不可修改密码！", ForgetPasswordActivity.this);
                }

            }

            @Override
            public void onFail() {
                //TODO:待处理
                ToastUtil.ToastShortShow("网络连接失败！", ForgetPasswordActivity.this);
            }
        });
    }

    @OnClick(R.id.forget_password_finish_bt)
    void onClickFinishBt() {
        if (checkInformation()) {
            Call<BaseResponse<Object>> forgetPasswordCall = RetroHttpUtil.build().forgetPasswordCall(MapGenerator.generate().add("email", account).add("password", password).add("code",verificationCode));
            RetroHttpUtil.sendRequest(forgetPasswordCall, new AbstractRegisterHttpCallback<BaseResponse<Object>>() {
                @Override
                public void onSuccess(BaseResponse<Object> result) {
                    ToastUtil.ToastShortShow("修改成功！", ForgetPasswordActivity.this);
                    Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                    intent.putExtra("account",account).putExtra("password","");
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onUnknownError() {
                    ToastUtil.ToastShortShow("修改失败，未知错误！", ForgetPasswordActivity.this);
                }

                @Override
                public void onFinal() {

                }
            });
        }
    }

    /**
     * 点击眼睛图片显示密码
     */
    @OnClick(R.id.forget_password_eye_img)
    void onClickEyeImg() {
        if (!isEyeImgOpened) {
            imgForgetEye.setImageResource(R.drawable.login_register_eye_open_img);
            edtNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            isEyeImgOpened = true;
            edtNewPassword.setSelection(edtNewPassword.getText().length());
        } else {
            imgForgetEye.setImageResource(R.drawable.login_register_eye_close_img);
            edtNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            isEyeImgOpened = false;
            edtNewPassword.setSelection(edtNewPassword.getText().length());
        }
    }

    /**
     * 获取用户的输入信息
     */
    private void getEditInformation() {
        account = edtForgetAccount.getText().toString();
        password = edtNewPassword.getText().toString();
        verificationCode = edtInputVerCode.getText().toString().trim();
    }

    /**
     * 检测输入的手机号和密码是否有效
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
                    if(!btGetVerCode.isClickable()){
                        btGetVerCode.setClickable(true);
                    }
                    btGetVerCode.setText(R.string.reget_code);
                    mc.cancel();
                }
            });
        }

        @Override
        public void onTick(long millisUntilFinished) {
            this.millisUntilFinished = "" + millisUntilFinished / 1000;
            btGetVerCode.setClickable(false);
            btGetVerCode.setText(this.millisUntilFinished + "秒后重新获取");
        }
    }
}
