package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.data.entity.TagBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends ModelRepository<TagBook> {

    TagBook findTagEntityBySlug(String slug);

    @Query(value = "select distinct t.id, t.name, t.slug " +
            "from tags t " +
            "left join book2tag b2t on b2t.tag_id = t.id " +
            "left join books b on b.id = b2t.book_id " +
            "where (upper(b.title) like upper(concat('%', ?1, '%')) " +
            "   or upper(t.name) like upper(concat('%', ?1, '%'))) " +
            "   and case when ?2 is not null then t.id not in (?2) else true end ", nativeQuery = true)
    Page<TagBook> findTags(String name, List<Integer> ids, Pageable pageable);

}
