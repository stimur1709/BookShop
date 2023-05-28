package com.example.mybookshopapp.dao;

import com.example.mybookshopapp.data.dao.BooksTransactionsCount;
import com.example.mybookshopapp.data.query.BTQuery;
import com.example.mybookshopapp.util.ParseDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class BooksTransactionsDao extends BaseDao<BooksTransactionsCount> {

    @Autowired
    public BooksTransactionsDao(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate, BooksTransactionsCount.class);
    }

    public List<BooksTransactionsCount> getBooksTransactionsCount(BTQuery query) throws ParseException {
        String sql = "select b.title, count(bt.id) from balance_transaction bt " +
                "join books b on b.id = bt.book_id " +
                "where bt.time between :dateS and :dateE " +
                "group by b.title " +
                "order by count(bt.id) desc";
        Map<String, Date> dateS = Map.of("dateS", ParseDateFormat.parseDate(query.getDateS()),
                "dateE", ParseDateFormat.parseDate(query.getDateE()));
        return namedParameterJdbcTemplate.query
                (
                        sql,
                        dateS,
                        new BeanPropertyRowMapper<>(BooksTransactionsCount.class)
                );
    }
}
