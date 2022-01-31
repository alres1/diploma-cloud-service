package ru.netology.diplomacloudservice.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class AuthRepositoryTest {

    private AuthRepository authRepository;
    private final Map<String, String> tokensAndUsers = new ConcurrentHashMap<>();

    final String TOKEN1 = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsImV4cCI6MTY0MzU2OTMzOCwiaWF0IjoxNjQzNDQ5MzM4LCJhdXRob3JpdGllcyI6IlJPTEVfVVNFUiJ9.iobQv_IsQbxqKH8CM5AJy71Bg9kClCqdkaco-e9G9vA";
    final String TOKEN2 = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsImV4cCI6MTY0MzU2OTMzOCwiaWF0IjoxNjQzNDQ5MzM4LCJhdXRob3JpdGllcyI6IlJPTEVfVVNFUiJ9.iobQv_IsQbxqKH8CM5AJy71Bg9kClCqdkaco-htr572";
    final String USERNAME1 = "User1";
    final String USERNAME2 = "User2";


    @BeforeEach
    void setUp() {
        authRepository = new AuthRepository();
        authRepository.putTokenAndUsername(TOKEN1, USERNAME1);
        tokensAndUsers.clear();
        tokensAndUsers.put(TOKEN1, USERNAME1);
    }


    @Test
    void putTokenAndUsername() {
        String beforePut = authRepository.getUserLoginByToken(TOKEN2);
        assertNull(beforePut);
        authRepository.putTokenAndUsername(TOKEN2, USERNAME2);
        String afterPut = authRepository.getUserLoginByToken(TOKEN2);
        assertEquals(USERNAME2, afterPut);
    }

    @Test
    void removeUserByToken() {
        String beforeRemove = authRepository.getUserLoginByToken(TOKEN1);
        assertNotNull(beforeRemove);
        authRepository.removeUserByToken(TOKEN1);
        String afterRemove = authRepository.getUserLoginByToken(TOKEN1);
        assertNull(afterRemove);
    }

    @Test
    void getUserLoginByToken() {
        assertEquals(tokensAndUsers.get(TOKEN1), authRepository.getUserLoginByToken(TOKEN1));
    }
}