package org.amoustakos.linker.io.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public interface ApiService {
    String ENDPOINT = "https://dummy.io/";

//    @GET("/")
//    Observable<List> get();

    /******** Helper class that sets up a new services *******/
    class Creator {
        public static ApiService newApiService() {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiService.ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            return retrofit.create(ApiService.class);
        }
    }
}
