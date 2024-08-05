package com.example.newsparser.table;
/**
 * <==================================>
 * Copyright (c) 2024 Ilya Sukhina.*
 * <=================================>
 */

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Entity class representing a news item.
 * This class is mapped to the "news" table in the database.
 */
@Table(name = "news")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String headline;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "publication_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime publicationTime;


    /**
     * Automatically sets the current date and time before persisting the entity.
     * This method is annotated with @PrePersist to ensure the requestDate field
     * is populated with the creation timestamp.
     */
    @PrePersist
    protected void onCreate() {
        publicationTime = LocalDateTime.now();
    }

}
