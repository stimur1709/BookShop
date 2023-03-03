package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.data.entity.BooksQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface BooksQueryRepository extends JpaRepository<BooksQuery, Integer> {

    @Query(value = "select * from get_books(?1)", nativeQuery = true)
    Page<BooksQuery> getBooks(Integer userId, Pageable pageable);

    @Query(value = "select * from get_books(?1) where pub_date between ?2 and ?3 ", nativeQuery = true)
    Page<BooksQuery> findBookEntityByPubDateBetween(Integer userId, Date from, Date to, Pageable nextPage);

    @Query(value = "select * from get_books(?1) where upper(title) like upper(concat('%', ?2, '%'))", nativeQuery = true)
    Page<BooksQuery> findBookEntityByTitleContainingAllIgnoreCase(Integer userId, String bookTitle, Pageable nextPage);

    @Query(value = "select * from get_books_by_tag_slug(?1, ?2) ", nativeQuery = true)
    Page<BooksQuery> findByTagList_Slug(Integer userId, String slug, Pageable pageable);

    @Query(value = "select * from get_books_by_genre_slug(?1, ?2) ", nativeQuery = true)
    Page<BooksQuery> getByGenreList_Slug(Integer userId, String slug, Pageable pageable);

    @Query(value = "select * from get_books_by_author_slug(?1, ?2) ", nativeQuery = true)
    Page<BooksQuery> getByAuthorList_Slug(Integer userId, String slug, Pageable nextPage);

    @Query(value = "select * from get_books(?1) where code = ?2 ", nativeQuery = true)
    List<BooksQuery> getBooksUser(Integer userId, String name);

    @Query(value = "select count(*) " +
            "    from books b " +
            "             join book2user b2u on b.id = b2u.book_id and b2u.user_id = ?1 " +
            "    where b2u.type_id in (?2) ", nativeQuery = true)
    long getCountBooksForUser(Integer userId, List<Integer> ids);

    @Query(value = "select * from get_books_viewed(?1)", nativeQuery = true)
    Page<BooksQuery> getBooksRecentlyViewed(Integer userId, Pageable nextPage);

    @Query(value = "select id, discount, image, is_bestseller, popularity, price, slug, title, pub_date, code, rate " +
            "from get_recommended_books(?1) " +
            "group by id, discount, image, is_bestseller, popularity, price, slug, title, pub_date, code, rate " +
            "order by max(sort_index) desc ", nativeQuery = true)
    Page<BooksQuery> getRecommendedBooks(Integer userId, Pageable nextPage);
}
