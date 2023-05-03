package com.example.mybookshopapp.dao;


import com.example.mybookshopapp.data.daoEntity.BooksTransactionsCount;
import com.example.mybookshopapp.data.daoEntity.TransactionsInterval;
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
public class TransactionsDao extends BaseDao {


    @Autowired
    public TransactionsDao(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }

    public List<TransactionsInterval> getTransactionsInterval(BTQuery query) throws ParseException {
        String sql = "select date_list.interval_time, " +
                "       count(time) as transaction_count " +
                "from (select generate_series( " +
                "                     date_trunc(:interval, min(:dateS::timestamp)), " +
                "                     date_trunc(:interval, max(:dateE::timestamp)), " +
                "                     :interval1::interval " +
                "                 ) as interval_time " +
                "      from balance_transaction) date_list " +
                "         left join balance_transaction b " +
                "                   on date_trunc(:interval, b.time) = date_list.interval_time " +
                "group by date_list.interval_time " +
                "order by date_list.interval_time";
        return namedParameterJdbcTemplate.query
                (
                        sql,
                        Map.of("interval", query.getInterval(), "interval1", "1 " + query.getInterval(),
                                "dateS", ParseDateFormat.parseDate(query.getDateS()),
                                "dateE", ParseDateFormat.parseDate(query.getDateE())),
                        new BeanPropertyRowMapper<>(TransactionsInterval.class)
                );
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
