package ru.clevertec.newsservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.newsservice.dao.Comment;
import ru.clevertec.newsservice.dto.CommentDTO;
import ru.clevertec.newsservice.mapper.CommentMapper;
import ru.clevertec.newsservice.service.impl.CommentServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

import static ru.clevertec.newsservice.controller.constants.Constants.*;

/**
 * <d>This class is a controller that was created using the REST technology. It works with the news service, which,
 *  * in turn, implements operations for working with entities (comment).</d>
 *
 *  @author Artur Malashkov
 *  @since 17
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(COMMENTS_URL)
public class CommentController {
    private final CommentServiceImpl commentService;
    private final CommentMapper commentMapper;

    /**
     * This controller method searches and returns all found entities (comment) from the database. Also, its functionality
     * includes pagination indicating the number of entities on one page in sorted order. Also implemented - full-text
     * search by various parameters, such as the topic of the news and the content of the news. This is implemented
     * using regular expressions. A partial query that includes part of the expected string, such as keyword=%25City%25,
     * will return all headlines and news content that contain that word "City".
     * Example: http://localhost:8080/news_applications/v1/comment?keyword=%25i%25&pageNo=0&pageSize=30&sortBy=id
     *
     * @param keyword  this parameter takes a regular expression and the method looks for matches using it.
     *                 It's not a required parameter.
     * @param pageNo   this parameter is specified to set the page we want to view
     * @param pageSize this parameter is specified to set the number of entities (comment) that will be placed on one page
     * @param sortBy   this parameter indicates the type of sorting of found entities (comment) on the page.
     * @return a list of all news that could be found by the specified search parameters from the database
     */
    @Operation(
            summary = "search all comment ",
            description = "This controller method searches and returns all found entities (comment) from the database")
    @ApiResponses({
            @ApiResponse(
                    description = "comment is found",
                    responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = CommentDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)})})
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<CommentDTO>> findAll(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(defaultValue = PAGE_NO) Integer pageNo,
            @RequestParam(defaultValue = PAGE_SIZE) Integer pageSize,
            @RequestParam(defaultValue = SORT_BY) String sortBy) {

        List<Comment> comments = commentService.findAll(keyword, pageNo, pageSize, sortBy);
        List<CommentDTO> responseList = comments.stream()
                .map(commentMapper::commentToCommentDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    /**
     * Search for comment by ID in the database.
     * Example: http://localhost:8080/news_applications/v1/comment/5
     *
     * @param id parameter for the id of a find comment.
     * @return comment that was found in the database by ID
     */
    @Operation(summary = "search comment by Id", description = "Search for comment by ID in the database")
    @ApiResponses({
            @ApiResponse(
                    description = "comment is found",
                    responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = CommentDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(
                    description = "comment not found", responseCode = "404",
                    content = {@Content(schema = @Schema())})})
    @GetMapping(value = "/{id}")
    public ResponseEntity<CommentDTO> findById(@PathVariable Long id) {
        return new ResponseEntity<>(commentMapper.commentToCommentDTO(commentService.findById(id)), HttpStatus.OK);
    }

    /**
     * This controller method is used to save the entity (Comment) in the Database.
     *
     * @param commentDto DTO object to save in the Database
     * @return commentDTO stored in the database
     */
    @Operation(
            summary = "POST new Comment",
            description = "Save  comment in the Database")
    @ApiResponses({
            @ApiResponse(responseCode = "201",
                    content = {@Content(schema = @Schema(implementation = CommentDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)})})
    @PostMapping(
            value = "/{newsId}/comments",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentDTO> create(@PathVariable Long newsId,
                                             @RequestBody @Valid CommentDTO commentDto) {
        log.info("newsId = :{}", newsId);
        log.info("commentDto = :{}", commentDto);
        return new ResponseEntity<>(commentMapper.commentToCommentDTO(commentService.save(newsId, commentDto)),
                HttpStatus.CREATED);
    }

    /**
     * This controller method is used to update the entity (Comment) in the Database.
     *
     * @param commentDto DTO object to update in the Database.
     * @param id         this is the id of the comment we want to update.
     * @return entity (Comment) that has been updated in the database with new fields.
     */
    @Operation(
            summary = "UPDATE Comment",
            description = "Update  the entity (Comment) in the Database")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = CommentDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(
                    description = "the Comment you want to update was not found",
                    responseCode = "404", content = {@Content(schema = @Schema())})})
    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentDTO> update(@PathVariable Long id, @RequestBody @Valid CommentDTO commentDto) {
        return new ResponseEntity<>(commentMapper.commentToCommentDTO(commentService.update(id, commentDto)),
                HttpStatus.OK);
    }

    /**
     * This controller method is used to delete the entity (Comment) in the Database.
     *
     * @param id this is a unique parameter by which the entity will be searched in the database
     * @return response about the successful deletion of the Comment
     */
    @Operation(summary = "DELETE Comment", description = "DELETE Comment by id")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})})
    @DeleteMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        commentService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * API Point for returning all comments by specifying news id.
     * @param keyword parameter for the id of a certain news
     * @param pageNo parameter for a specific page
     * @param pageSize parameter for page size
     * @param sortBy parameter for sorting the result
     * @return comment that was found in the database by NewsID
     */
    @Operation(summary = "Retrieve all Comments by news ID", description = "Get list of all comments by news id")
    @ApiResponses({@ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = CommentDTO.class),
            mediaType = "application/json") }),
            @ApiResponse(description = "Such news not found", responseCode = "404", content = { @Content(schema = @Schema()) })})
    @GetMapping(value =  "/{newsId}/comment", produces = MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<List<CommentDTO>>  findAllCommentsByNewsId(
            @PathVariable(value = "newsId") Long keyword,
            @RequestParam(defaultValue = PAGE_NO) Integer pageNo,
            @RequestParam(defaultValue = PAGE_SIZE) Integer pageSize,
            @RequestParam(defaultValue = SORT_BY) String sortBy) {
        List<Comment> comments = commentService.findAllByNewsId(keyword, pageNo, pageSize, sortBy);
        List<CommentDTO> responseList = comments.stream()
                .map( commentMapper::commentToCommentDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }
}
