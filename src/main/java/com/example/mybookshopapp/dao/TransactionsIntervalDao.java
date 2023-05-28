package com.example.mybookshopapp.dao;


<<<<<<< HEAD
import com.example.mybookshopapp.data.dao.TransactionsInterval;
=======
import com.example.mybookshopapp.data.daoEntity.TransactionsInterval;
>>>>>>> origin/dev
import com.example.mybookshopapp.data.query.BTQuery;
import com.example.mybookshopapp.util.ParseDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Component
public class TransactionsIntervalDao extends BaseDao<TransactionsInterval> {

    @Autowired
    public TransactionsIntervalDao(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate, TransactionsInterval.class);
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
        Map<String, Object> paramMap = Map.of(
                "interval", query.getInterval(),
                "interval1", "1 " + query.getInterval(),
                "dateS", ParseDateFormat.parseDate(query.getDateS()),
                "dateE", ParseDateFormat.parseDate(query.getDateE())
        );
        return super.getContent(sql, paramMap);
    }

}
