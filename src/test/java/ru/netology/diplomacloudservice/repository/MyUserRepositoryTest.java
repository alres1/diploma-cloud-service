package ru.netology.diplomacloudservice.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.netology.diplomacloudservice.entity.MyUser;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MyUserRepositoryTest {

    @Mock
    MyUserRepository myUserRepository;

    final String USERNAME = "User1";
    final MyUser MY_USER = new MyUser(1L,"User1", "$2y$10$ucRxNm00UVAwLvc7BOWHHuSuRZaMGNItMqDajdLI8N2r.bSNO1xp2","USER");

    @Test
    void findByLogin() {
        when(myUserRepository.findByLogin(USERNAME)).thenReturn(MY_USER);
        assertEquals(MY_USER, myUserRepository.findByLogin(USERNAME));
    }
}