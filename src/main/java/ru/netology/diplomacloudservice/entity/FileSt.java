package ru.netology.diplomacloudservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FileSt {
    @Id
    private String id;
    private String name;
    private String type;
    private Long size;
    @Lob
    private byte[] data;
    @ManyToOne
    private MyUser myUser;
}


