package com.example.mybookshopapp.service;

import com.example.mybookshopapp.dao.TMLitresDao;
import com.example.mybookshopapp.data.dao.TMLitres;
import com.example.mybookshopapp.data.entity.books.Book;
import com.example.mybookshopapp.data.outher.litres.Litres;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
public class ExternalBookLoader {

    private final RestTemplate restTemplate;
    private final LitresParser litresParser;
    private final TMLitresDao tmLitresDao;

    @Autowired
    public ExternalBookLoader(RestTemplate restTemplate, LitresParser litresParser, TMLitresDao tmLitresDao) {
        this.restTemplate = restTemplate;
        this.litresParser = litresParser;
        this.tmLitresDao = tmLitresDao;
    }

    @Scheduled(fixedDelayString = "PT12H")
    public void loaderBooks() {
        List<TMLitres> searchList = tmLitresDao.getTagWithMinBook();
        for (TMLitres tmLitres : searchList) {
            if (tmLitres.isActive()) {
                log.info("Начался парсинг книг по тэгу {}", tmLitres.getSearchName());
                String url = getUrl(tmLitres);
                Litres exchange = restTemplate.getForObject(url, Litres.class);
                List<Book> items = litresParser.parserBook(exchange);
                tmLitres.setLastStart(tmLitres.getLastStart() + items.size());
                tmLitres.setTotal(tmLitres.getTotal() + items.size());
                tmLitresDao.saveTMLitres(tmLitres);
                log.info("Закончился парсинг книг");
            }
        }
    }

    private String getUrl(TMLitres tmLitres) {
        String url = "https://api.litres.ru/foundation/api/search?limit=%d&offset=%d&q=%s&types=text_book&types=audiobook&types=podcast&types=podcast_episode";
        return String.format(url, 5, tmLitres.getLastStart(), tmLitres.getSearchName());
    }

}
