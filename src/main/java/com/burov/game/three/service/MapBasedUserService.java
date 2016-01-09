package com.burov.game.three.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MapBasedUserService implements UserService {

    private Map<String,String> users = new ConcurrentHashMap<>();

    @Override
    public String register(String name) {
        String userUuid = UUID.randomUUID().toString();
        users.put(userUuid, name);

        return userUuid;
    }
}
