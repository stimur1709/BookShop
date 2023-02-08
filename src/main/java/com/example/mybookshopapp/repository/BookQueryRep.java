package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.data.entity.BookQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookQueryRep extends JpaRepository<BookQuery, Integer> {

    @Query(value = "select b.id, b.description, b.discount, " +
            "       b.image,  b.is_bestseller, b.popularity, " +
            "       b.price, b.slug, b.title, b.pub_date, " +
            "       t.code, br.rating, coalesce(avg(br1.rating), 0) as rate, " +
            "       count(r1) as count1, count(r2) as count2," +
            "       count(r3) as count3, count(r4) as count4, " +
            "       count(r5) as count5 " +
            "from books b " +
            "         left join book2user b2u on b.id = b2u.book_id " +
            "         left join book2user_type t on b2u.type_id = t.id " +
            "         left join book_rating br on b.id = br.book_id " +
            "         left join book_rating br1 on b.id = br1.book_id " +
            "         left join users u on br.user_id = u.id " +
            "         left join book_rating r1 on b.id = r1.book_id and r1.rating = 1 " +
            "         left join book_rating r2 on b.id = r2.book_id and r2.rating = 2 " +
            "         left join book_rating r3 on b.id = r3.book_id and r3.rating = 3 " +
            "         left join book_rating r4 on b.id = r4.book_id and r4.rating = 4 " +
            "         left join book_rating r5 on b.id = r5.book_id and r5.rating = 5 " +
            "where (b2u.user_id is null or b2u.user_id = 1) and (u.id is null or u.id = 1) " +
            "group by b.id, b.description, b.discount, b.image, b.is_bestseller, b.popularity, b.price, b.slug, b.title, t.code, br.rating", nativeQuery = true)
    Page<BookQuery> getBooks(int userId, Pageable pageable);
}
