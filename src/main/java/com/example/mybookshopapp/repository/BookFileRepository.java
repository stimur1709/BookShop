package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.data.entity.books.BookFile;
import org.springframework.stereotype.Repository;

@Repository
public interface BookFileRepository extends ModelRepository<BookFile> {

    BookFile findBookFileByHash(String hash);
}
