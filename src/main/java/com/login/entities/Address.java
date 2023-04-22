package com.login.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "user_address")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String town;
    private String district;
    private String state;
    private String country;
    private Integer pinCode;
    @OneToOne
    private User user;
}
