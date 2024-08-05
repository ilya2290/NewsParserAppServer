/**
 * <==================================>
 * Copyright (c) 2024 Ilya Sukhina.*
 * <=================================>
 */

package com.example.newsparser.service;

import com.example.newsparser.repository.NewsRepository;
import com.example.newsparser.table.NewsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service class for handling operations related to news entities.
 * Provides methods to retrieve news by specific date.
 */
@Service
public class NewsService {

    private final NewsRepository newsRepository;

    /**
     * Constructor for NewsService.
     *
     * @param newsRepository The repository for accessing news data.
     */
    @Autowired
    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    /**
     * Retrieves a list of news entities for a specific date.
     *
     * @param date The date for which to retrieve news entities.
     * @return A list of news entities published on the specified date.
     */
    public List<NewsEntity> getNewsByDate(LocalDate date) {
        return this.newsRepository.findByPublicationDate(date);
    }

    /**
     * Saves a news entity to the repository.
     *
     * @param news The news entity to save. It includes details such as headline, description, and publication time.
     */
    public void saveNewsToRepository(NewsEntity news){
        this.newsRepository.save(news);
    }

    /**
     * Retrieves a news entity by its ID.
     *
     * @param id The ID of the news entity to find.
     * @return An Optional containing the news entity if it exists, or an empty Optional if no entity with the specified ID is found.
     */
    public Optional<NewsEntity> findNewsById(int id){

      return this.newsRepository.findById(id);
    }

    /**
     * Deletes a news entity from the repository by its ID.
     *
     * @param id The ID of the news entity to delete.
     */
    public void deleteNewsById(int id){
        this.newsRepository.deleteById(id);
    }
}
