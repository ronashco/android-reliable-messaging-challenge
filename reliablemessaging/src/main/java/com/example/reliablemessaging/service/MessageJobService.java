package com.example.reliablemessaging.service;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.example.reliablemessaging.BuildConfig;
import com.example.reliablemessaging.BundleKey;
import com.example.reliablemessaging.network.ApiClient;
import com.example.reliablemessaging.network.NetworkAPIService;
import com.example.reliablemessaging.network.Url;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class MessageJobService extends JobService {
    private static final String TAG = MessageJobService.class.getSimpleName();
    CompositeDisposable disposable = new CompositeDisposable();
    NetworkAPIService networkAPIService;
    ApiClient apiClient;

    public MessageJobService() {
        Log.i(TAG, "MessageJobService");
        networkAPIService = new Retrofit.Builder()
                .baseUrl(Url.BASE_URL)
                .client(provideClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create()).build().create(NetworkAPIService.class);
        apiClient = new ApiClient(networkAPIService);

    }

    OkHttpClient provideClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        }
        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .readTimeout(45, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .build();
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.i(TAG, "onStartJob: ");

        disposable.add(apiClient.sendMessage(jobParameters.getExtras().getString(BundleKey.URL), jobParameters.getExtras().getString(BundleKey.DATA)).subscribeWith(new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody s) {
                Log.i(TAG, "onNext: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: ");
                Log.i(TAG,"There was an error except HttpException");
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete: ");
                // TODO: 8/14/2019 send notification
                Toast.makeText(getApplicationContext(),"Message successfully sent",Toast.LENGTH_LONG).show();
                jobFinished(jobParameters, false);
            }
        }));
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.i(TAG, "onStopJob: ");
        return true;
    }
}
