package com.burov.game.three.server.service;

import com.burov.game.three.shared.model.Player;
import org.junit.Test;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class MapBasedPlayerServiceTest {

    public static final String PLAYER_NAME = "user name";

    private PlayerService testedInstance = new MapBasedPlayerService();

    @Test
    public void shouldReturnPlayer() {
        Player player = testedInstance.registerPlayer(PLAYER_NAME);

        assertThat(player.getId().length(), greaterThan(1));
        assertThat(player.getName(), is(PLAYER_NAME));
    }

    @Test
    public void shouldFindPlayerById() {
        Player player = testedInstance.registerPlayer(PLAYER_NAME);

        Player savedPlayer = testedInstance.getPlayer(player.getId());
        assertThat(savedPlayer.getName(), is(PLAYER_NAME));
    }
}