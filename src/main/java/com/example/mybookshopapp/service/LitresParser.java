package com.example.mybookshopapp.service;

import com.example.mybookshopapp.data.entity.Author;
import com.example.mybookshopapp.data.entity.Genre;
import com.example.mybookshopapp.data.entity.Image;
import com.example.mybookshopapp.data.entity.TagBook;
import com.example.mybookshopapp.data.entity.books.Book;
import com.example.mybookshopapp.data.outher.litres.Data;
import com.example.mybookshopapp.data.outher.litres.Instance;
import com.example.mybookshopapp.data.outher.litres.Litres;
import com.example.mybookshopapp.data.outher.litres.Person;
import com.example.mybookshopapp.repository.AuthorRepository;
import com.example.mybookshopapp.repository.BookRepository;
import com.example.mybookshopapp.repository.GenreRepository;
import com.example.mybookshopapp.repository.TagRepository;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Component
@Slf4j
public class LitresParser {

    private static final String LITRES = "https://www.litres.ru/";

    private final ImageServiceImpl imageService;
    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final TagRepository tagRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public LitresParser(ImageServiceImpl imageService, BookRepository bookRepository, GenreRepository genreRepository, TagRepository tagRepository, AuthorRepository authorRepository) {
        this.imageService = imageService;
        this.bookRepository = bookRepository;
        this.genreRepository = genreRepository;
        this.tagRepository = tagRepository;
        this.authorRepository = authorRepository;
    }

    @Transactional
    public List<Book> parserBook(Litres exchange) {
        List<Book> books = new ArrayList<>();
        if ((exchange != null ? exchange.getPayload() : null) != null) {
            for (Data data : exchange.getPayload().getData()) {
                log.info("Скачана книга {}", data.getInstance().getTitle());

                Optional<Book> optionalBook = bookRepository.findFirstByTitleIgnoreCase(data.getInstance().getTitle());
                Optional<String> first = books.stream()
                        .map(Book::getTitle)
                        .filter(t -> t.contains(data.getInstance().getTitle()))
                        .findFirst();
                if (optionalBook.isEmpty() && first.isEmpty()) {
                    books.add(createBook(data.getInstance()));
                    threadSleep();
                }
            }
        }
        return bookRepository.saveAll(books);
    }

    private Book createBook(Instance instance) {
        Book book = new Book();
        book.setSlug(UUID.randomUUID().toString());
        book.setTitle(instance.getTitle());
        book.setIsBestseller(instance.getLabels().isBestseller() ? 1 : 0);
        String imageUrl = "https://cv7.litres.ru";
        Image image = imageService.parsingImage(imageUrl + instance.getCoverUrl());
        book.setImage(image);

        Document doc = htmlParse(LITRES + instance.getUrl());
        String description = getDescription(doc, 1);
        book.setDescription(description);

        book.setAuthorList(parserAuthor(instance.getPersons()));
        book.setGenreList(parseGenre(doc));
        book.setTagList(parseTag(doc));

        book.setPubDate(new Date());
        book.setPrice(500);
        book.setDiscount(0.0);
        book.setPopularity(0.0);
        return book;
    }

    private List<Author> parserAuthor(List<Person> persons) {
        List<Author> authors = new ArrayList<>();
        for (Person person : persons) {
            if (person.getRole().equals("author") && !checkContainsAuthor(authors, person.getFullName())) {
                Optional<Author> optionalAuthor = authorRepository.findFirstByNameIgnoreCase(person.getFullName());
                Author newAuthor = optionalAuthor
                        .map(author -> refreshAuthor(author, person))
                        .orElseGet(() -> createAuthor(person));
                authors.add(newAuthor);
                threadSleep();
            }
        }
        return authorRepository.saveAll(authors);
    }

    private List<Genre> parseGenre(Document doc) {
        List<Genre> genres = new ArrayList<>();
        if (doc != null) {
            Elements elems = doc.select("a[href~=\\/genre]");

            for (Element elem : elems) {
                if (!checkContainsGenre(genres, elem.text())) {
                    Optional<Genre> optionalGenre = genreRepository.findFirstByNameIgnoreCase(elem.text());
                    Genre genre = optionalGenre
                            .orElseGet(() -> new Genre(elem.text(), UUID.randomUUID().toString()));
                    genres.add(genre);
                }
            }
        }
        return genreRepository.saveAll(genres);
    }

    private List<TagBook> parseTag(Document doc) {
        List<TagBook> tagBooks = new ArrayList<>();

        if (doc != null) {
            Elements elems = doc.select("a[href~=\\/tags]");

            for (Element elem : elems) {
                if (!checkContainsTag(tagBooks, elem.text())) {
                    Optional<TagBook> optionalTagBook = tagRepository.findFirstByNameIgnoreCase(elem.text());
                    TagBook tagBook = optionalTagBook
                            .orElseGet(() -> new TagBook(elem.text(), UUID.randomUUID().toString()));
                    tagBooks.add(tagBook);
                }
            }
        }
        return tagRepository.saveAll(tagBooks);
    }

    private Author createAuthor(Person person) {
        Author author = new Author();
        author.setName(person.getFullName());
        String description = getDescription(htmlParse(LITRES + person.getUrl() + "ob-avtore/"), 2);
        Image image = downloadAuthorImage(htmlParse(LITRES + person.getUrl()));
        author.setImage(image);
        author.setDescription(description);
        author.setSlug(UUID.randomUUID().toString());
        return author;
    }

    private Author refreshAuthor(Author author, Person person) {
        if (author.getDescription() == null || author.getDescription().isBlank()) {
            String description = getDescription(htmlParse(LITRES + person.getUrl() + "ob-avtore/"), 2);
            author.setDescription(description);
        }
        if (author.getImage() == null || author.getImage().getId() == 1) {
            Image image = downloadAuthorImage(htmlParse(LITRES + person.getUrl()));
            author.setImage(image);
        }
        return author;
    }

    private Document htmlParse(String url) {
        try {
            return Jsoup.connect(url).get();
        } catch (IOException e) {
            return null;
        }
    }

    private String getDescription(Document doc, int type) {
        if (doc == null) {
            return null;
        }
        return type == 1 ?
                doc.select(".biblio_book_descr_publishers")
                        .select("p").html().trim()
                : doc.select(".person-page__html")
                .select("p").html();
    }

    private void threadSleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error("Ошибка");
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    private Image downloadAuthorImage(Document doc) {
        if (doc == null) {
            return null;
        } else {
            String src = doc.select(".biblio_author_image").select("img").first().absUrl("src");
            return imageService.parsingImage(src);
        }
    }

    private boolean checkContainsGenre(List<Genre> genres, String name) {
        return genres.stream().map(Genre::getName).anyMatch(n -> n.equalsIgnoreCase(name));
    }

    private boolean checkContainsAuthor(List<Author> authors, String name) {
        return authors.stream().map(Author::getName).anyMatch(n -> n.equalsIgnoreCase(name));
    }

    private boolean checkContainsTag(List<TagBook> tagBooks, String name) {
        return tagBooks.stream().map(TagBook::getName).anyMatch(n -> n.equalsIgnoreCase(name));
    }

}