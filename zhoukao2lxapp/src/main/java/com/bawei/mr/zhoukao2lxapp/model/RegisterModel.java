package com.bawei.mr.zhoukao2lxapp.model;

import android.os.Handler;
import android.os.Message;

import com.bawei.mr.zhoukao2lxapp.api.Api;
import com.bawei.mr.zhoukao2lxapp.bean.UserBean;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * author:Created by WangZhiQiang on 2018/8/11.
 */
public class RegisterModel {

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };


    public void register(String mobile, String pwd, final RegCallback regCallback) {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(5000, TimeUnit.SECONDS)
                .writeTimeout(5000, TimeUnit.SECONDS)
                .connectTimeout(5000, TimeUnit.SECONDS)
                .build();

        FormBody formBody = new FormBody.Builder()
                .add("mobile", mobile)
                .add("password", pwd)
                .build();

         Request request = new Request.Builder()
                .url(Api.REG_URL)
                .post(formBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (regCallback != null) {
                    regCallback.failure("请求失败");
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    String result = response.body().string();
                    parseJsonResult(result, regCallback);

                }
            }
        });
    }


    private void parseJsonResult(final String result, final RegCallback regCallback) {
        if (result != null) {
            final UserBean userBean = new Gson().fromJson(result, UserBean.class);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (regCallback != null) {
                        regCallback.success(userBean);
                    }
                }
            });
        }
    }
    public interface RegCallback {
        void failure(String ErrorMsg);

        void success(UserBean userBean);
    }
}