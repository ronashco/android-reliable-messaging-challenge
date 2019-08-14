package com.example.reliablemessaging.network;

import android.util.Log;

import com.google.gson.JsonSyntaxException;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLException;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by IT-10 on 10/16/2018.
 */

public class ApiClient implements Consumer<Throwable> {
    private final NetworkAPIService networkApiService;

    @SuppressWarnings("unchecked")
    private final ObservableTransformer apiCallTransformer =
            observable ->
                    observable.map(new Function<Object, Object>() {
                        @Override
                        public Object apply(Object appResponse) throws Exception {

                            return appResponse;
                        }
                    })
                            .subscribeOn(Schedulers.io())
                            .retryWhen(new RetryWithDelay()).doOnError(this)
                            .observeOn(AndroidSchedulers.mainThread());

    public ApiClient(NetworkAPIService networkApiService) {
        this.networkApiService = networkApiService;
    }

    @Override
    public void accept(Throwable throwable) throws Exception {
        final Class<?> throwableClass = throwable.getClass();
        // TODO: 8/14/2019 handle other errors here

        if (SocketException.class.isAssignableFrom(throwableClass) || SSLException.class.isAssignableFrom(throwableClass)) {
        } else if (throwableClass.equals(SocketTimeoutException.class) || UnknownHostException.class.equals(throwableClass)) {
        } else if (JsonSyntaxException.class.isAssignableFrom(throwableClass)) {
        }
    }

    @SuppressWarnings("unchecked")
    private <T> ObservableTransformer<T, T> configureApiCallObserver() {
        return (ObservableTransformer<T, T>) apiCallTransformer;
    }

    public Observable<ResponseBody> sendMessage(String url, String message) {
        return networkApiService.sendMessage(url, message).compose(configureApiCallObserver());
    }


    class RetryWithDelay implements
            Function<Observable<? extends Throwable>, ObservableSource<?>> {
        private int retryCount = 0;

        @Override
        public Observable<?> apply(Observable<? extends Throwable> attempts) throws Exception {
            return attempts
                    .flatMap((Function<Throwable, Observable<?>>) throwable -> {
                        if (throwable instanceof HttpException) {
                                return Observable.timer(
                                        (long) Math.pow(2, ++retryCount),
                                        TimeUnit.SECONDS);

                        }
                        return Observable.error(throwable);
                    });
        }
    }
}
