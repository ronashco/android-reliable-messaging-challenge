package com.example.reliablemessaging.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.reliablemessaging.BuildConfig;
import com.example.reliablemessaging.BundleKey;
import com.example.reliablemessaging.MessageSender;
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

public class MessageService extends Service {

    private static final String TAG = MessageService.class.getSimpleName();
    CompositeDisposable disposable = new CompositeDisposable();
    NetworkAPIService networkAPIService;
    ApiClient apiClient;
    int count;

    @androidx.annotation.Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: ");
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
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: ");
        count++;
        if (intent.hasExtra(BundleKey.URL) && intent.hasExtra(BundleKey.DATA))
            disposable.add(apiClient.sendMessage(intent.getExtras().getString(BundleKey.URL), intent.getExtras().getString(BundleKey.DATA)).subscribeWith(new DisposableObserver<ResponseBody>() {
                @Override
                public void onNext(ResponseBody s) {
                    Log.i(TAG, "onNext: ");
                }

                @Override
                public void onError(Throwable e) {
                    Log.i(TAG, "onError: ");
                    Log.i(TAG, "There was an error except HttpException");
                }

                @Override
                public void onComplete() {
                    Log.i(TAG, "onComplete: ");
                    // TODO: 8/14/2019 send notification
                    Toast.makeText(getApplicationContext(), "Message successfully sent", Toast.LENGTH_LONG).show();
                    count--;
                    if (count == 0)
                        stopSelf();

                }
            }));

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }
}
