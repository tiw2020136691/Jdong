package com.example.myflow.utils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public interface RequestCallback {

    void failure(Call call, IOException e);

    void onResponse(Call call, Response response);
}
