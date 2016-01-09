package com.burov.game.three.service;

import com.burov.game.three.model.Player;
import org.junit.Test;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class MapBasedPlayerServiceTest {

    public static final String PLAYER_NAME = "user name";

    @Test
    public void shouldReturnPlayer() {
        PlayerService testedInstance = new MapBasedPlayerService();

        Player player = testedInstance.registerPlayer(PLAYER_NAME);

        assertThat(player.getId().length(), greaterThan(1));
        assertThat(player.getName(), is(PLAYER_NAME));
    }

}