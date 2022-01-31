package ru.netology.diplomacloudservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.netology.diplomacloudservice.entity.MyUser;
import ru.netology.diplomacloudservice.repository.MyUserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CustomUserDetailsServiceTest {

    final String USERNAME = "User1";
    final MyUser MY_USER = new MyUser(1L,"User1", "Password", "USER");

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private MyUserRepository myUserRepository;

    @Test
    void loadUserByUsername() {
        when(myUserRepository.findByLogin(USERNAME)).thenReturn(MY_USER);
        assertEquals(MY_USER.getLogin(), customUserDetailsService.loadUserByUsername(USERNAME).getUsername());
    }

}