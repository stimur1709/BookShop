package com.example.mybookshopapp.data.entity.user;

import com.example.mybookshopapp.data.entity.Models;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "user_role")
@Entity
@Getter
@Setter
public class UserRole extends Models {

    private String role;
}
