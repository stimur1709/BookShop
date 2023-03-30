package com.example.mybookshopapp.repository.news;

import com.example.mybookshopapp.data.entity.Models;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModelRepository<M extends Models> extends JpaRepository<M, Integer> {
}
