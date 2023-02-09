package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.data.entity.BookQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface BookQueryRepository extends JpaRepository<BookQuery, Integer> {

    @Query(value = "select b.id, b.description, b.discount, " +
            "       b.image,  b.is_bestseller, b.popularity, " +
            "       b.price, b.slug, b.title, b.pub_date, " +
            "       coalesce(t.code, '') as code, br.rating as user_rating, avg(br1.rating) as rate, " +
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
            "where (b2u.user_id is null or b2u.user_id = ?1) and (u.id is null or u.id = ?1) " +
            "group by b.id, b.description, b.discount, b.image, b.is_bestseller, b.popularity, b.price, b.slug, b.title, b.pub_date, t.code, br.rating", nativeQuery = true)
    Page<BookQuery> getBooks(Integer userId, Pageable pageable);


    @Query(value = "select b.id, b.description, b.discount, " +
            "       b.image,  b.is_bestseller, b.popularity, " +
            "       b.price, b.slug, b.title, b.pub_date, " +
            "       coalesce(t.code, '') as code, br.rating as user_rating, avg(br1.rating) as rate, " +
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
            "where (b2u.user_id is null or b2u.user_id = ?1) and (u.id is null or u.id = ?1) " +
            "   and b.pub_date between ?2 and ?3 " +
            "group by b.id, b.description, b.discount, b.image, b.is_bestseller, b.popularity, b.price, b.slug, b.title, b.pub_date, t.code, br.rating", nativeQuery = true)
    Page<BookQuery> findBookEntityByPubDateBetween(Integer userId, Date from, Date to, Pageable nextPage);


    @Query(value = "select b.id, b.description, b.discount, " +
            "       b.image,  b.is_bestseller, b.popularity, " +
            "       b.price, b.slug, b.title, b.pub_date, " +
            "       coalesce(t.code, '') as code, br.rating as user_rating, avg(br1.rating) as rate, " +
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
            "where (b2u.user_id is null or b2u.user_id = ?1) and (u.id is null or u.id = ?1) " +
            "   and upper(b.title) like upper(concat('%', ?2, '%'))" +
            "group by b.id, b.description, b.discount, b.image, b.is_bestseller, b.popularity, b.price, b.slug, b.title, b.pub_date, t.code, br.rating", nativeQuery = true)
    Page<BookQuery> findBookEntityByTitleContainingAllIgnoreCase(Integer userId, String bookTitle, Pageable nextPage);

    @Query(value = "select b.id, b.description, b.discount, " +
            "       b.image,  b.is_bestseller, b.popularity, " +
            "       b.price, b.slug, b.title, b.pub_date, " +
            "       coalesce(t.code, '') as code, br.rating as user_rating, avg(br1.rating) as rate, " +
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
            "         join book2tag b2t on b.id = b2t.book_id\n" +
            "         join tags tg on b2t.tag_id = tg.id " +
            "where (b2u.user_id is null or b2u.user_id = ?1) and (u.id is null or u.id = ?1) " +
            "   and tg.slug = ?2 " +
            "group by b.id, b.description, b.discount, b.image, b.is_bestseller, b.popularity, b.price, b.slug, b.title, b.pub_date, t.code, br.rating", nativeQuery = true)
    Page<BookQuery> findByTagList_Slug(Integer userId, String slug, Pageable pageable);

    @Query(value = "select b.id, b.description, b.discount, " +
            "       b.image,  b.is_bestseller, b.popularity, " +
            "       b.price, b.slug, b.title, b.pub_date, " +
            "       coalesce(t.code, '') as code, br.rating as user_rating, avg(br1.rating) as rate, " +
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
            "         join book2genre b2g on b.id = b2g.book_id " +
            "         join genre g on b2g.genre_id = g.id " +
            "where (b2u.user_id is null or b2u.user_id = ?1) and (u.id is null or u.id = ?1) " +
            "   and g.slug = ?2 " +
            "group by b.id, b.description, b.discount, b.image, b.is_bestseller, b.popularity, b.price, b.slug, b.title, b.pub_date, t.code, br.rating", nativeQuery = true)
    Page<BookQuery> getByGenreList_Slug(Integer userId, String slug, Pageable pageable);

    @Query(value = "select b.id, b.description, b.discount, " +
            "       b.image,  b.is_bestseller, b.popularity, " +
            "       b.price, b.slug, b.title, b.pub_date, " +
            "       coalesce(t.code, '') as code, br.rating as user_rating, avg(br1.rating) as rate, " +
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
            "         join book2author b2a on b.id = b2a.book_id " +
            "         join authors a on b2a.author_id = a.id " +
            "where (b2u.user_id is null or b2u.user_id = ?1) and (u.id is null or u.id = ?1)" +
            "   and a.slug = ?2 " +
            "group by b.id, b.description, b.discount, b.image, b.is_bestseller, b.popularity, b.price, b.slug, b.title, b.pub_date, t.code, br.rating", nativeQuery = true)
    Page<BookQuery> getByAuthorList_Slug(Integer userId, String slug, Pageable nextPage);

    @Query(value = "select b.id, b.description, b.discount, " +
            "       b.image,  b.is_bestseller, b.popularity, " +
            "       b.price, b.slug, b.title, b.pub_date, " +
            "       coalesce(t.code, '') as code, br.rating as user_rating, avg(br1.rating) as rate, " +
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
            "where (b2u.user_id is null or b2u.user_id = ?1) and (u.id is null or u.id = ?1) " +
            "   and b.slug = ?2 " +
            "group by b.id, b.description, b.discount, b.image, b.is_bestseller, b.popularity, b.price, b.slug, b.title, b.pub_date, t.code, br.rating", nativeQuery = true)
    BookQuery findBookEntityBySlug(Integer userId, String slug);

    @Query(value = "select b.id, b.description, b.discount, " +
            "       b.image,  b.is_bestseller, b.popularity, " +
            "       b.price, b.slug, b.title, b.pub_date, " +
            "       coalesce(t.code, '') as code, br.rating as user_rating, avg(br1.rating) as rate, " +
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
            "where (b2u.user_id is null or b2u.user_id = ?1) and (u.id is null or u.id = ?1) " +
            "   and code = ?2 " +
            "group by b.id, b.description, b.discount, b.image, b.is_bestseller, b.popularity, b.price, b.slug, b.title, b.pub_date, t.code, br.rating", nativeQuery = true)
    List<BookQuery> getBooksUser(Integer userId, String name);
}
