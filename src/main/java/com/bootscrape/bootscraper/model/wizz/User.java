package com.bootscrape.bootscraper.model.wizz;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "wizz_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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


    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    @JoinTable(name = "wizz_users_airports", joinColumns = { @JoinColumn(name = "userId", referencedColumnName = "id") },
               inverseJoinColumns = { @JoinColumn(name = "airportId", referencedColumnName = "id") })
    private Set<Airport> airports= new HashSet<>();

}
