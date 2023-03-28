package com.example.mybookshopapp.repository.news;

import com.example.mybookshopapp.data.entity.news.Models;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModelRepository<M extends Models> extends JpaRepository<M, Integer> {

}
