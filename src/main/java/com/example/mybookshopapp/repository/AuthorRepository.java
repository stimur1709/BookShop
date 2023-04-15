package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.data.entity.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends ModelRepository<Author> {

    @Query(value = "select distinct a.id, a.description, a.name, a.image_id, a.slug " +
            "from authors a" +
            "         left join book2author b2a on b2a.author_id = a.id " +
            "         left join books b on b.id = b2a.book_id " +
            "where (upper(a.name) like upper(concat('%', ?1, '%')) " +
            "   or upper(b.title) like upper(concat('%', ?1, '%'))) " +
            "   and case when ?2 is not null then a.id not in (?2) else true end ", nativeQuery = true)
    Page<Author> findAuthors(String name, List<Integer> ids, Pageable pageable);

    Author findAuthorBySlug(String slug);

}