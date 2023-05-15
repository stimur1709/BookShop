package com.example.mybookshopapp.dao;

import com.example.mybookshopapp.data.daoEntity.TMLitres;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TMLitresDao extends BaseDao<TMLitres> {

    @Autowired
    public TMLitresDao(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate, TMLitres.class);
    }

    public List<TMLitres> getTagWithMinBook() {
        String sqlGetTag = "(select tm.id, " +
                "        tm.active, " +
                "        t.name                    as search_name, " +
                "        t.id                      as search_id, " +
                "        coalesce(l.last_start, 0) as last_start, " +
                "        coalesce(l.total, 0)      as total, " +
                "        1                         as type " +
                " from task_manager tm " +
                "          cross join tags t " +
                "          left join book2tag b2t on t.id = b2t.tag_id " +
                "          left join books b on b2t.book_id = b.id " +
                "          left join tm_litres l on l.task_id = tm.id and l.tag_id = t.id " +
                " where name_task = 'litres' " +
                " group by tm.id, tm.name_task, tm.active, t.name, t.id, l.last_start, l.total " +
                " order by count(b) " +
                " limit 1) " +
                "union " +
                "(select tm.id, " +
                "        tm.active, " +
                "        a.name, " +
                "        a.id, " +
                "        coalesce(l.last_start, 0), " +
                "        coalesce(l.total, 0), " +
                "        2 " +
                " from task_manager tm " +
                "          cross join authors a " +
                "          left join book2author b2a on a.id = b2a.author_id " +
                "          left join books b on b2a.book_id = b.id " +
                "          left join tm_litres l on l.task_id = tm.id and l.author_id = a.id " +
                " where name_task = 'litres' " +
                " group by tm.id, tm.name_task, tm.active, a.name, a.id, l.last_start, l.total " +
                " order by count(b) " +
                " limit 1) " +
                "union " +
                "(select tm.id, " +
                "        tm.active, " +
                "        g.name, " +
                "        g.id, " +
                "        coalesce(l.last_start, 0), " +
                "        coalesce(l.total, 0), " +
                "        3 " +
                " from task_manager tm " +
                "          cross join genre g " +
                "          left join book2genre b2g on g.id = b2g.genre_id " +
                "          left join books b on b2g.book_id = b.id " +
                "          left join tm_litres l on l.task_id = tm.id and l.genre_id = g.id " +
                " where name_task = 'litres' " +
                " group by tm.id, tm.name_task, tm.active, g.name, g.id, l.last_start, l.total " +
                " order by count(b) " +
                " limit 1)";
        return super.getContent(sqlGetTag);
    }

    public void saveTMLitres(TMLitres tmLitres) {
        String sql = "insert into tm_litres(task_id, tag_id, last_start, total) " +
                "values (:id, :tagId, :last_start, :total) " +
                "on conflict(task_id, tag_id) do update set last_start = :last_start, " +
                "                                           total      = :total";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", tmLitres.getId());
        paramMap.put("tagId", tmLitres.getSearchId());
        paramMap.put("last_start", tmLitres.getLastStart());
        paramMap.put("total", tmLitres.getTotal());
        super.saveOrUpdate(sql, paramMap);
    }
}
