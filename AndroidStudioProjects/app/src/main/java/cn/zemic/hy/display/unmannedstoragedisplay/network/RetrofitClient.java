package cn.zemic.hy.display.unmannedstoragedisplay.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author fxs
 */
public class RetrofitClient {
    private RetrofitClient() {
    }


    private static class RetroClickHandler {
        private static Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        private static Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(URLFactory.smartShelfOfficialUrl)
//                .baseUrl("http://192.168.0.2:25535/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static Retrofit getInstance() {
        return RetroClickHandler.mRetrofit;
    }
}

