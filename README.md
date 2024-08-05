# NewsParserAppServer
NewsParserAppServer

# News Parser API

## Description

**News Parser API** is a RESTful service that provides endpoints to manage news articles. It allows for creating, updating, deleting, and retrieving news by date. The application also uses a scheduler to periodically parse news every 20 minutes.

## Features

- **Get News by Date**: `GET /news-by-date?date=yyyy-MM-dd`
- **Create News**: `POST /news`
- **Update News by ID**: `PUT /news/{id}`
- **Delete News by ID**: `DELETE /news/{id}`

## Database Connection

The application connects to a MySQL database hosted on Google Cloud:

```properties
spring.datasource.url=jdbc:mysql://35.223.128.60:3306/news_db
