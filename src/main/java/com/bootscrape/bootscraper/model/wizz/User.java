package com.bootscrape.bootscraper.model.wizz;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Data
@Table(name = "wizz_user")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @NonNull
    private String name;
    @NonNull
    private String username;
    @NonNull
    private String email;

    public User() {
    }
}
