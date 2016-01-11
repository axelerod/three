package com.burov.game.three.client.service.http;

import org.springframework.http.HttpStatus;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.Optional;

public class CallProcessor<T> {
    private final HttpStatus status;
    private final Call<T> call;
    private final String wrongCodeMessage;

    public CallProcessor(HttpStatus status, Call<T> call, String wrongCodeMessage) {
        this.status = status;
        this.call = call;
        this.wrongCodeMessage = wrongCodeMessage;
    }

    public Optional<T> process() {
        Response<T> callResponse = null;
        try {
            callResponse = call.execute();
            if (callResponse.code() != status.value()) {
                System.out.println(wrongCodeMessage + callResponse.message());
                return Optional.empty();
            }
            return Optional.of(callResponse.body());
        } catch (IOException e) {
            System.out.println("Server doesn't respond: " + e.getMessage());
        }
        return Optional.empty();
    }
}
