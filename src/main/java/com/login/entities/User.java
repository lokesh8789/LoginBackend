package com.login.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;


    
    private String email;
    private String mobile;
    private String password;
    @OneToOne(cascade = CascadeType.ALL,mappedBy = "user")
    private Address address;
}
