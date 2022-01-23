package ru.netology.diplomacloudservice.repository;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class AuthRepository {

    private final Map<String, String> tokensAndUsers = new ConcurrentHashMap<>();

    public void putTokenAndUsername(String token, String username) {
        tokensAndUsers.put(token, username);
    }

    public void removeUserByToken(String token) {
        tokensAndUsers.remove(token);
    }

    public String getUserLoginByToken(String token) {
        return tokensAndUsers.get(token);
    }

}
