package com.example.reliablemessaging.network;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface NetworkAPIService {

    @POST
    @FormUrlEncoded
    Observable<ResponseBody> sendMessage(@retrofit2.http.Url String url, @Field("message") String message);
}
