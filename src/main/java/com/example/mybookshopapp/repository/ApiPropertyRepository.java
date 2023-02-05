package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.data.entity.config.Api;
import com.example.mybookshopapp.data.entity.config.Property;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiPropertyRepository extends JpaRepository<Api, Integer> {

    Api findByIsMainAndProperty(boolean isMain, Property property);

}
