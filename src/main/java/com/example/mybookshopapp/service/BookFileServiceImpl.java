package com.example.mybookshopapp.service;

import com.example.mybookshopapp.data.dto.BookFileDto;
import com.example.mybookshopapp.data.entity.books.BookFile;
import com.example.mybookshopapp.data.query.Query;
import com.example.mybookshopapp.repository.BookFileRepository;
import com.example.mybookshopapp.service.userService.UserProfileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class BookFileServiceImpl extends ModelServiceImpl<BookFile, Query, BookFileDto, BookFileRepository> {

    @Value("${download.path}")
    private String downloadPath;

    @Autowired
    protected BookFileServiceImpl(BookFileRepository repository, UserProfileService userProfileService,
                                  ModelMapper modelMapper, HttpServletRequest request) {
        super(repository, BookFileDto.class, BookFile.class, userProfileService, modelMapper, request);
    }

    public Path getBookFilePath(String hash) {
        BookFile bookFile = repository.findBookFileByHash(hash);
        return Paths.get(bookFile.getPath());
    }

    public MediaType getBookFileName(String hash) {
        BookFile bookFile = getHash(hash);
        String mimeType = URLConnection.guessContentTypeFromName(Paths.get(bookFile.getPath()).getFileName().toString());
        if (mimeType != null) {
            return MediaType.parseMediaType(mimeType);
        } else {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

    public byte[] getBookFileByteArray(String hash) throws IOException {
        BookFile bookFile = getHash(hash);
        Path path = Paths.get(downloadPath, bookFile.getPath());
        return Files.readAllBytes(path);
    }

    public BookFile getHash(String hash) {
        return repository.findBookFileByHash(hash);
    }
}
