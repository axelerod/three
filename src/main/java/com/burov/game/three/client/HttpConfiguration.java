package com.burov.game.three.client;

import com.burov.game.three.client.service.http.RetrofitGameService;
import com.burov.game.three.client.service.http.RetrofitPlayerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.JacksonConverterFactory;
import retrofit2.Retrofit;

@Configuration
public class HttpConfiguration {

    @Bean
    Retrofit httpFactory(@Value("${three.client.api.http.baseUrl}") String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    @Bean
    RetrofitPlayerService retrofitPlayerService(Retrofit factory) {
        return factory.create(RetrofitPlayerService.class);
    }

    @Bean
    RetrofitGameService retrofitGameService(Retrofit factory) {
        return factory.create(RetrofitGameService.class);
    }
}
