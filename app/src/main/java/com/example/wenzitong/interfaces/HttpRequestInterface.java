package com.example.wenzitong.interfaces;

import com.example.wenzitong.entity.BaseResponse;
import com.example.wenzitong.entity.LoginResponseData;
import com.example.wenzitong.untils.MapGenerator;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by 邹特强 on 2018/2/8.
 * Retrofit公用网络请求方法接口
 */
public interface HttpRequestInterface {

    /**
     * 登录界面
     */

    String LOGIN_URL = "sign_in";

    /**
     * 登录的post方法
     *
     * @return 返回LoginResponseData
     */
    @POST(LOGIN_URL)
    Call<BaseResponse<LoginResponseData>> loginCall(@Body MapGenerator postData);

    /**
     * 注册界面
     */
    String REGISTER_URL = "sign_up";

    /**
     * 注册的post方法
     *
     * @return 这里因为不需要获取data, 所以用Object作为data类型
     * 取值时只取code和msg
     */
    @POST(REGISTER_URL)
    Call<BaseResponse<Object>> registerCall(@Body MapGenerator registerData);

    /**
     * 检测用户是否注册接口
     */
    String REGISTER_CHECK_URL = "registion";

    /**
     * 检测账号是否注册的post方法
     *
     * @return 只需获取msg
     */
    @POST(REGISTER_CHECK_URL)
    Call<BaseResponse<Object>> registerCheckCall(@Body MapGenerator regsiterCheckData);

    /**
     * 获取验证码接口
     */
    String VERIFICATION_CODE_URL = "code";

    /**
     * 获取验证码的post方法
     *
     * @return 这里也不需要获取data，所以返回BaseResponse<Object></Object>
     */
    @POST(VERIFICATION_CODE_URL)
    Call<BaseResponse<Object>> verificationCodeCall(@Body MapGenerator verificationCodeData);

    /**
     * 忘记密码界面
     */
    String FORGETPASSWORD_URL = "reset";
    /**
     * 修改密码的post方法
     */
    @POST(FORGETPASSWORD_URL)
    Call<BaseResponse<Object>> forgetPasswordCall(@Body MapGenerator foregetPasswordData);

    /**
     * 新的获取验证码接口
     */
    String NEW_VERIFICATION_CODE_URL  = "appSysUser/sendMail";
    @POST(NEW_VERIFICATION_CODE_URL)
    Call<BaseResponse<Object>>  newVerificationCodeCall(@Body MapGenerator newVerificationCodeData);

    /**
     * 新的注册接口
     */
    String NEW_REGISTER_URL = "appSysUser/registerSysUser.do";
    @POST(NEW_REGISTER_URL)
    Call<BaseResponse<Object>> newRegisterCall(@Body MapGenerator newRegisterData);

    /**
     * 新的登录接口
     */
    String NEW_LOGIN_URL = "login_login";
    @POST(NEW_LOGIN_URL)
    Call<BaseResponse<Object>> newLoginCall(@Body MapGenerator newLoginData);

    /**
     * 新的忘记密码接口
     */
    String NEW_FORGETPASSWORD_URL = "repwd";
    @POST(NEW_FORGETPASSWORD_URL)
    Call<BaseResponse<Object>> newForgetPasswordCall(@Body MapGenerator newForgetPasswordData);
}
