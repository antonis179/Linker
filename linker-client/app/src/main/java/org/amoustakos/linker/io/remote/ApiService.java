package org.amoustakos.linker.io.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.amoustakos.linker.BuildConfig;
import org.amoustakos.linker.io.models.base.BaseResponse;
import org.amoustakos.linker.io.models.request.LinkRequest;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ApiService {
    /*
     * Headers
     */
    String HEADER_CONTENT_TYPE = "Content-Type: application/json";

    /*
     * Endpoints
     */
    String DUMMY_ENDPOINT = "http://127.0.0.1/";
    String STATUS_ENDPOINT = "http://%s:%s/Status";
    String LINK_ENDPOINT = "http://%s:%s/Link";


    @Headers({HEADER_CONTENT_TYPE})
    @POST
    Observable<BaseResponse> sendLink(
                                        @Url String url,
                                        @Body LinkRequest req
                                      );

    @Headers({HEADER_CONTENT_TYPE})
    @GET
    Observable<BaseResponse> getStatus(
                                        @Url String url
                                       );


    /******** Helper class that sets up new services *******/
    class Creator {
        public static ApiService newApiService() {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .create();

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            if(BuildConfig.DEBUG)
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            else
                interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
            OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            Retrofit retrofit = new Retrofit.Builder()
                    //use a dummy endpoint as a base url as it is required by retrofit
                    //afterwards use @Url to override this in every request with a dynamic url
                    .baseUrl(ApiService.DUMMY_ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(httpClient)
                    .build();
            return retrofit.create(ApiService.class);
        }
    }
}
