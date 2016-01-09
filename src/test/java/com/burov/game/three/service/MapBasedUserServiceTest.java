package com.burov.game.three.service;

import org.junit.Test;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.*;

public class MapBasedUserServiceTest {

    @Test
    public void shouldReturnUserId() {
        UserService testedInstance = new MapBasedUserService();

        String register = testedInstance.register("user name");
        assertThat(register.length(), greaterThan(1));
    }

}