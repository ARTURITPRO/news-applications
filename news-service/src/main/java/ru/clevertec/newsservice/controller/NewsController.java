package ru.clevertec.newsservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.newsservice.dao.News;
import ru.clevertec.newsservice.dto.NewsDTO;
import ru.clevertec.newsservice.mapper.NewsMapper;
import ru.clevertec.newsservice.service.impl.NewsServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

import static ru.clevertec.newsservice.controller.constants.Constants.*;

/**
 * <d> This class is a controller that was created using the REST technology. It works with the news service, which,
 * in turn, implements operations for working with entities (News).</d>
 *
 * @author Artur Malashkov
 * @since 17
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(NEWS_URL)
public class NewsController {

    private final NewsMapper newsMapper;
    private final NewsServiceImpl newsService;

    /**
     * This controller method searches and returns all found entities (news) from the database. Also, its functionality
     * includes pagination indicating the number of entities on one page in sorted order. Also implemented - full-text
     * search by various parameters, such as the topic of the news and the content of the news. This is implemented
     * using regular expressions. A partial query that includes part of the expected string, such as keyword=%25City%25,
     * will return all headlines and news content that contain that word "City".
     * Example: http://localhost:8080/news_applications/v1/news?keyword=%25i%25&pageNo=0&pageSize=30&sortBy=id
     *
     * @param keyword  this parameter takes a regular expression and the method looks for matches using it.
     *                 It's not a required parameter.
     * @param pageNo   this parameter is specified to set the page we want to view.
     * @param pageSize this parameter is specified to set the number of entities (news) that will be placed on one page.
     * @param sortBy   this parameter indicates the type of sorting of found entities (news) on the page.
     * @return a list of all news that could be found by the specified search parameters from the database.
     */
    @Operation(
            summary = "GET all News ",
            description = "This controller method searches and returns all found entities (news) from the database")
    @ApiResponses({
            @ApiResponse(
                    description = "News is found",
                    responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = NewsDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)})})
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<NewsDTO>> findAll(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(defaultValue = PAGE_NO) Integer pageNo,
            @RequestParam(defaultValue = PAGE_SIZE) Integer pageSize,
            @RequestParam(defaultValue = SORT_BY) String sortBy) {

        List<News> news = newsService.findAll(keyword, pageNo, pageSize, sortBy);
        List<NewsDTO> responseList = news.stream()
                .map(newsMapper::newsToNewsDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    /**
     * Search for news by ID in the database.
     * Example: http://localhost:8080/news_applications/v1/news/5
     *
     * @param id parameter for the id of a find news.
     * @return news that was found in the database by ID.
     */
    @Operation(
            summary = "GET News by Id",
            description = "Search for news by ID in the database")
    @ApiResponses({
            @ApiResponse(
                    description = "news is found",
                    responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = NewsDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(
                    description = "news not found", responseCode = "404",
                    content = {@Content(schema = @Schema())})})
    @GetMapping(value = "/{id}")
    public ResponseEntity<NewsDTO> findById(@PathVariable Long id) {
        return new ResponseEntity<>(newsMapper.newsToNewsDTO(newsService.findById(id)), HttpStatus.OK);
    }

    /**
     * This controller method is used to save the entity (News) in the Database.
     *
     * @param newsDto DTO object to save in the Database.
     * @param token   this is a unique string that is used to authenticate and authorize the user when making
     *                requests to protected API resources.
     * @return NewsDTO stored in the database.
     */
    @Operation(
            summary = "POST new News",
            description = "Save  news in the Database")
    @ApiResponses({
            @ApiResponse(responseCode = "201",
                    content = {@Content(schema = @Schema(implementation = NewsDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)})})
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NewsDTO> create(
            @RequestBody @Valid NewsDTO newsDto,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return new ResponseEntity<>(newsMapper.newsToNewsDTO(newsService.save(newsDto, token)), HttpStatus.CREATED);
    }

    /**
     * This controller method is used to update the entity (News) in the Database.
     *
     * @param newsDto DTO object to update in the Database.
     * @param id      this is the id of the news we want to update.
     * @param token   this is a unique string that is used to authenticate and authorize the user when making
     *                requests to protected API resources.
     * @return entity (News) that has been updated in the database with new fields.
     */
    @Operation(
            summary = "UPDATE News",
            description = "Update update the entity (News) in the Database")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = News.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(
                    description = "the news you want to update was not found",
                    responseCode = "404", content = {@Content(schema = @Schema())})})
    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NewsDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid NewsDTO newsDto,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return new ResponseEntity<>(newsMapper.newsToNewsDTO(newsService.update(id, newsDto, token)), HttpStatus.OK);
    }

    /**
     * This controller method is used to delete the entity (News) in the Database.
     *
     * @param id    this is a unique parameter by which the entity will be searched in the database.
     * @param token this is a unique string that is used to authenticate and authorize the user when making
     *              requests to protected API resources.
     * @return response about the successful deletion of the news.
     */
    @Operation(summary = "DELETE news", description = "DELETE news by id")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})})
    @DeleteMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(
            @PathVariable Long id,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        newsService.delete(id, token);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
