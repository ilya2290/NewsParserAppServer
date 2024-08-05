/**
 * <==================================>
 * Copyright (c) 2024 Ilya Sukhina.*
 * <=================================>
 */

package com.example.newsparser.service;

import com.example.newsparser.table.NewsEntity;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Service class for parsing and saving news from Yahoo.
 * This class fetches news headlines and descriptions from the Yahoo News website,
 * stores them in a map, and then saves them to the database.
 */
@Service
public class NewsParser {

    private static final Logger parserLogger = LogManager.getLogger(NewsParser.class);
    private static final String BASE_URL = "https://news.yahoo.com/";
    private static final String HEADLINE_XPATH = "//h3[contains(@class, 'LineClamp(2,2.6em)')]" +
                                                    "/a[contains(@class, 'js-content-viewer')]";
    private static final String DESCRIPTION_XPATH = "//p[contains(@class, 'finance-ticker-fetch-success_D(n)')]";

    public final NewsService newsService;

    @Getter
    private Map<String, String> parsedNewsMap = new LinkedHashMap<>();

    /**
     * Constructor for the NewsParser.
     *
     * @param newsService The service for accessing news data.
     */
    @Autowired
    public NewsParser(NewsService newsService) {
        this.newsService = newsService;
    }


    /**
     * Initializes the news parser and saves the parsed news.
     * This method is called after the bean is constructed.
     */
    @PostConstruct
    public void init()
    {
        this.newsParser();
        this.saveNews();
    }

    /**
     * Parses news headlines and descriptions from the Yahoo News website.
     * The parsed data is stored in the newsMap.
     */
    private void newsParser(){

        try {
            Document document = Jsoup.connect(BASE_URL).get();

            Elements titleElement = document.selectXpath(HEADLINE_XPATH);
            Elements contentElement = document.selectXpath(DESCRIPTION_XPATH);

            for (int i = 0; i < titleElement.size(); i++)
                this.parsedNewsMap.put(titleElement.get(i).text(), contentElement.get(i).text());
        }
        catch (Exception e)
        {
            parserLogger.error(e);
        }

    }

    /**
     * Saves the parsed news to the database.
     * If the newsMap is empty, logs an error message.
     */
    private void saveNews(){
        if (!parsedNewsMap.isEmpty())
            for (Map.Entry<String, String> entry : this.parsedNewsMap.entrySet())
                this.newsService.saveNewsToRepository(NewsEntity.builder()
                        .headline(entry.getKey())
                        .description(entry.getValue())
                        .build());
        else
            parserLogger.error("There no any news!");

        this.parsedNewsMap.clear();
    }

    /**
     * Checks if news headlines from the parsed news map exist in the list of existing news for today.
     * <p>
     * This method performs the following operations:
     * - Retrieves all news entities published today from the news service.
     * - Iterates through the parsed news map, checking if any of its headlines are present in the existing news.
     * - If a headline from the parsed news map is found in the existing news, it removes the corresponding entry from the parsed news map.
     * <p>
     * This ensures that the `parsedNewsMap` only contains headlines that are not already present in the news repository.
     */
    private void checkNewsExisting() {
        List<NewsEntity> existingNewsToday = this.newsService.getNewsByDate(LocalDate.now());

        boolean notEmpty = !this.parsedNewsMap.isEmpty() && !existingNewsToday.isEmpty();
        boolean notNull = existingNewsToday != null;

        if (notNull && notEmpty) {
            List<String> keysToRemove = new ArrayList<>();
            for (Map.Entry<String, String> entry : this.parsedNewsMap.entrySet()) {
                for (NewsEntity newsEntity : existingNewsToday) {
                    if (newsEntity.getHeadline().contains(entry.getKey())) {
                        keysToRemove.add(entry.getKey());
                        break;
                    }
                }
            }
            for (String key : keysToRemove) {
                this.parsedNewsMap.remove(key);
            }
        }
    }


    /**
     * Scheduled task that runs every 20 minutes.
     * <p>
     * This method performs the following operations:
     * - Parses news data.
     * - Checks for the existence of news items.
     * - Saves news items to the repository.
     */
    @Scheduled(cron = "0 0/20 * * * *")
    private void parseWithDelay(){
        this.newsParser();
        this.checkNewsExisting();
        this.saveNews();
     }


}
