package ru.netology.diplomacloudservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.netology.diplomacloudservice.entity.MyUser;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface MyUserRepository extends JpaRepository<MyUser, String> {
    MyUser findByLogin(String login);
}
