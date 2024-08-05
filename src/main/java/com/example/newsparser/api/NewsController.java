/**
 * <==================================>
 * Copyright (c) 2024 Ilya Sukhina.*
 * <=================================>
 */

package com.example.newsparser.api;

import com.example.newsparser.service.NewsService;
import com.example.newsparser.table.NewsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

/**
 * Controller for handling HTTP requests related to news.
 * Provides methods for retrieving a list of all news and news by date.
 */
@RestController
public class NewsController {

    private final NewsService newsService;

    /**
     * Constructor for the NewsController.
     *
     * @param newsService The service for handling news-related operations.
     */
    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    /**
     * Retrieves a list of news entities for a specific date.
     *
     * @param date The date for which to retrieve news entities. Example: 2024-08-01
     * @return A ResponseEntity containing a list of news entities published on the specified date,
     *         or a 404 Not Found response if the date parameter is invalid or empty.
     */
    @GetMapping("/news-by-date")
    public ResponseEntity<List<NewsEntity>> getNewsByDate(@RequestParam LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String dateAsString = date.format(formatter);

        try {
            LocalDate parsedDate = LocalDate.parse(dateAsString, formatter);
            if (!parsedDate.equals(date))
                throw new DateTimeParseException("Date format mismatch", dateAsString, 0);
        }
        catch (DateTimeParseException ex) {
            return ResponseEntity.notFound().build();
        }

        List<NewsEntity> newsList = newsService.getNewsByDate(date);
        return ResponseEntity.ok(newsList);
    }


    /**
     * Creates a new news entity.
     *
     * @param news The news entity to create, containing details such as headline and description.
     * @return A ResponseEntity containing the created news entity, or a 400 Bad Request response if there is an error.
     */
    @PostMapping("/news")
    public ResponseEntity<NewsEntity> createNews(@RequestBody NewsEntity news) {

        try {
            NewsEntity newsToAdd = NewsEntity.builder()
                    .headline(news.getHeadline())
                    .description(news.getDescription())
                    .publicationTime(LocalDateTime.now()).build();

            this.newsService.saveNewsToRepository(newsToAdd);

            return ResponseEntity.ok(newsToAdd);
        }
        catch (Exception exception)
        {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Updates an existing news entity by its ID.
     *
     * @param id The ID of the news entity to update.
     * @param newsDetails The new details to update the news entity with.
     * @return A ResponseEntity containing the updated news entity, or a 404 Not Found response if the entity with the specified ID does not exist.
     */
    @PutMapping("/news/{id}")
    public ResponseEntity<NewsEntity> updateNews(@PathVariable int id, @RequestBody NewsEntity newsDetails) {
        Optional<NewsEntity> optionalNews = this.newsService.findNewsById(id);

        if (optionalNews.isPresent()) {
            NewsEntity newsToUpdate = optionalNews.get();

            newsToUpdate.setHeadline(newsDetails.getHeadline());
            newsToUpdate.setDescription(newsDetails.getDescription());
            newsToUpdate.setPublicationTime(LocalDateTime.now());

            this.newsService.saveNewsToRepository(newsToUpdate);
            return ResponseEntity.ok(newsToUpdate);
        }
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Deletes a news entity by ID.
     *
     * @param id The ID of the news entity to delete.
     * @return Response indicating whether the deletion was successful.
     */
    @DeleteMapping("/news/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable Integer id) {
        Optional<NewsEntity> optionalNews = newsService.findNewsById(id);

        if (optionalNews.isPresent()) {
            this.newsService.deleteNewsById(id);
            return ResponseEntity.noContent().build();
        }
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Handles {@link MethodArgumentTypeMismatchException} when request parameters do not match the expected type.
     * Responds with a 404 Not Found status and a message indicating the expected date format is yyyy-MM-dd.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid date format. Expected format: yyyy-MM-dd");
    }

}

