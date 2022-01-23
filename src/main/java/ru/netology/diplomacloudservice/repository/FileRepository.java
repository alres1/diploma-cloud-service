package ru.netology.diplomacloudservice.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.netology.diplomacloudservice.entity.FileSt;
import ru.netology.diplomacloudservice.entity.MyUser;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FileSt, String> {

    FileSt findByNameAndMyUser(String name, MyUser myUser);

    List<FileSt> findAllByMyUser(MyUser myUser);

    @Modifying
    @Query("UPDATE FileSt f SET f.name = :newName WHERE f.name = :name and f.myUser = :myUser")
    void updateNameByNameAndMyUser(@Param("newName") String newName, @Param("name") String name, @Param("myUser") MyUser myUser);

    void deleteByNameAndMyUser(String filename, MyUser myUser);

}