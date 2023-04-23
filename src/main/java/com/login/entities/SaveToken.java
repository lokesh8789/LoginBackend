package com.login.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "save_token")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaveToken {
    @Id
    @Column(name = "user_id")
    Integer userId;
    String token;
}
