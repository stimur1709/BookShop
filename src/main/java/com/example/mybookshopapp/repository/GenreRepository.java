package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.data.entity.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreRepository extends ModelRepository<Genre> {

    Genre findGenreEntityBySlug(String slug);

    @Query(value = "select distinct g.id, g.name, g.slug, g.parent_id " +
            "from genre g " +
            "left join book2genre b2g on b2g.genre_id = g.id " +
            "left join books b on b.id = b2g.book_id " +
            "where (upper(b.title) like upper(concat('%', ?1, '%')) " +
            "   or upper(g.name) like upper(concat('%', ?1, '%'))) " +
            "   and case when ?2 is not null then g.id not in (?2) else true end ", nativeQuery = true)
    Page<Genre> findGenres(String name, List<Integer> ids, Pageable pageable);

    @Query(value = "select * from genre where parent_id is null", nativeQuery = true)
    List<Genre> getParentGenreList();

}
