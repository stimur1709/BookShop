package com.example.mybookshopapp.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Map;

public abstract class BaseDao<E> {

    protected final JdbcTemplate jdbcTemplate;
    protected final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final Class<E> object;

    public BaseDao(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate, Class<E> object) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.object = object;
    }

    public List<E> getContent(String sql, Map<String, Object> paramMap) {
        return namedParameterJdbcTemplate.query(sql, paramMap, new BeanPropertyRowMapper<>(object));
    }

    public List<E> getContent(String sql) {
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(object));
    }


    public E getData(String sql) {
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(object))
                .stream()
                .findFirst()
                .orElse(null);
    }

    public void saveOrUpdate(String sql, Map<String, Object> paramMap) {
        namedParameterJdbcTemplate.update(sql, paramMap);
    }
}
