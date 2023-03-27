package com.example.mybookshopapp.repository.news;

import com.example.mybookshopapp.data.entity.news.Model;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModelRepository<M extends Model> extends JpaRepository<M, Integer> {
}
