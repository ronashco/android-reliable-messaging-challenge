package david.hosseini.androidreliablemessaginglibrary

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Url


object HttpManager {

    private var retrofit = Retrofit.Builder()
        .baseUrl("https://challenge.ronash.co/reliable-messaging/")
        .build()

    internal val service: MessagingService = retrofit.create(MessagingService::class.java)
}

internal interface MessagingService {

    @POST
    @FormUrlEncoded
    fun postMessage(@Url url: String, @FieldMap data: Map<String, String>): Call<ResponseBody>
}
