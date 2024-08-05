/**
 * <==================================>
 * Copyright (c) 2024 Ilya Sukhina.*
 * <=================================>
 */

package com.example.newsparser.repository;

import com.example.newsparser.table.NewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for accessing news entities.
 * Extends JpaRepository to provide CRUD operations.
 */
@Repository
public interface NewsRepository extends JpaRepository<NewsEntity, Integer> {

    /**
     * Finds news entities by their publication date.
     *
     * @param date The date to filter news entities by.
     * @return A list of news entities published on the specified date.
     */
    @Query("SELECT n FROM NewsEntity n WHERE DATE(n.publicationTime) = :date")
    List<NewsEntity> findByPublicationDate(@Param("date") LocalDate date);
}
