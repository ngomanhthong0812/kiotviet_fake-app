package com.example.kiotviet_fake.database.UpdateNameTable;

import com.example.kiotviet_fake.database.BasicAuthInterceptor;
import com.example.kiotviet_fake.database.updateOrderTableById.UpdateOrderTableByIdService;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class UpdateNameTableAPI {
    private static final String BASE_URL = "http://gaycoffee-001-site1.jtempurl.com/";

    public static UpdateNameTableService  createService(String username, String password) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor(username, password))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        return retrofit.create(UpdateNameTableService .class);
    }
}
