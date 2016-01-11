package com.burov.game.three.client.service.http;

import com.burov.game.three.shared.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import retrofit2.Call;

import java.util.Optional;

@Service
public class PlayerService {
    private final RetrofitPlayerService playerService;

    @Autowired
    public PlayerService(RetrofitPlayerService playerService) {
        this.playerService = playerService;
    }

    public Optional<Player> registerPlayer(String userName) {
        Call<Player> registerCall = playerService.register(userName);
        return new CallProcessor<>(HttpStatus.CREATED, registerCall, "You weren't registered: ")
                .process();
    }
}
