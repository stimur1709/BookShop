package com.example.mybookshopapp.repository.news;

import com.example.mybookshopapp.data.entity.books.BooksF;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface BooksQueryRepository extends ModelRepository<BooksF> {

    @Query(value = "select * from get_books(?1)", nativeQuery = true)
    Page<BooksF> getBooks(Integer userId, Pageable pageable);

    @Query(value = "select * from find_books(?1, ?2, ?3, ?4, ?5, ?6) ", nativeQuery = true)
    Page<BooksF> findBooks(Integer userId, String search, boolean bestseller, boolean discount, Date from, Date to, Pageable nextPage);

    @Query(value = "select * from get_books_by_tag_slug(?1, ?2) ", nativeQuery = true)
    Page<BooksF> getBooksByTagSlug(Integer userId, String slug, Pageable pageable);

    @Query(value = "select * from get_books_by_genre_slug(?1, ?2) ", nativeQuery = true)
    Page<BooksF> getBooksByGenreSlug(Integer userId, String slug, Pageable pageable);

    @Query(value = "select * from get_books_by_author_slug(?1, ?2) ", nativeQuery = true)
    Page<BooksF> getBooksByAuthorSlug(Integer userId, String slug, Pageable nextPage);

    @Query(value = "select * from get_books(?1) where code = ?2 ", nativeQuery = true)
    List<BooksF> getBooksUser(Integer userId, String name);

    @Query(value = "select count(*) " +
            "    from books b " +
            "             join book2user b2u on b.id = b2u.book_id and b2u.user_id = ?1 " +
            "    where b2u.type_id in (?2) ", nativeQuery = true)
    long getCountBooksForUser(Integer userId, List<Integer> ids);

    @Query(value = "select * from get_books_viewed(?1)", nativeQuery = true)
    Page<BooksF> getBooksRecentlyViewed(Integer userId, Pageable nextPage);

    @Query(value = "select id, discount, image, is_bestseller, popularity, price, slug, title, pub_date, code, rate " +
            "from get_recommended_books(?1) " +
            "group by id, discount, image, is_bestseller, popularity, price, slug, title, pub_date, code, rate " +
            "order by max(sort_index) desc ", nativeQuery = true)
    Page<BooksF> getRecommendedBooks(Integer userId, Pageable nextPage);

}
