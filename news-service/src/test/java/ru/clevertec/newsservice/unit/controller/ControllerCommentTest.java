package ru.clevertec.newsservice.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import ru.clevertec.newsservice.controller.CommentController;
import ru.clevertec.newsservice.dto.CommentDTO;
import ru.clevertec.newsservice.mapper.CommentMapper ;
import ru.clevertec.newsservice.dao.Comment;
import ru.clevertec.newsservice.service.CommentService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static ru.clevertec.newsservice.util.Constants.*;
import static ru.clevertec.newsservice.util.TestData.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ControllerCommentTest {
    @InjectMocks
    private CommentController commentController;
    @Mock
    private CommentService commentService;
    @Mock
    private CommentMapper commentMapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void buildMockMVC() {
        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
    }

    @AfterEach
    public void verifyNoMoreInteractionsCommentService() {
        Mockito.verifyNoMoreInteractions(commentService);
    }

    @Test
    void findAllCommentsWithMockMVC() throws Exception {
        Comment comment = getComment();
        CommentDTO commentDto = getCommentDTO();
        doReturn(Collections.singletonList(comment))
                .when(commentService).findAll(any(), anyInt(), anyInt(), anyString());
        doReturn(commentDto)
                .when(commentMapper).commentToCommentDTO(comment);

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("search", SEARCH_STR);
        requestParams.add("pageNo", String.valueOf(PAGE_NO));
        requestParams.add("pageSize", String.valueOf(PAGE_SIZE));
        requestParams.add("sortBy", SORTING);
        mockMvc.perform(MockMvcRequestBuilders.get("/news_applications/v1/comment")
                .params(requestParams))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is("1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].text", Matchers.is("Text1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userName", Matchers.is("User1")));

        verify(commentService).findAll(SEARCH_STR, PAGE_NO, PAGE_SIZE, SORTING);
        verify(commentMapper).commentToCommentDTO(comment);
    }

    @Test
    void findByIdCommentsWithMockMVC() throws Exception {
        Comment comment = getComment();
        CommentDTO commentDto = getCommentDTO();
        doReturn(comment)
                .when(commentService).findById(anyLong());
        doReturn(commentDto)
                .when(commentMapper).commentToCommentDTO(comment);

        mockMvc.perform(MockMvcRequestBuilders.get("/news_applications/v1/comment/1")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("id", Matchers.is("1")))
                .andExpect(MockMvcResultMatchers.jsonPath("text", Matchers.is("Text1")))
                .andExpect(MockMvcResultMatchers.jsonPath("userName", Matchers.is("User1")));

        verify(commentService).findById(ID);
        verify(commentMapper).commentToCommentDTO(comment);
    }

    @Test
    void findAllCommentsByNewsIdWithMockMVC() throws Exception {
        Comment comment = getComment();
        CommentDTO commentDto = getCommentDTO();
        doReturn(Collections.singletonList(comment))
                .when(commentService).findAllByNewsId(anyLong(), anyInt(), anyInt(), anyString());
        doReturn(commentDto)
                .when(commentMapper).commentToCommentDTO(comment);

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("pageNo", String.valueOf(PAGE_NO));
        requestParams.add("pageSize", String.valueOf(PAGE_SIZE));
        requestParams.add("sortBy", SORTING);
        mockMvc.perform(MockMvcRequestBuilders.get("/news_applications/v1/comment/1/comment")
                .params(requestParams)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is("1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].text", Matchers.is("Text1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userName", Matchers.is("User1")));

        verify(commentService).findAllByNewsId(ID, PAGE_NO, PAGE_SIZE, SORTING);
        verify(commentMapper).commentToCommentDTO(comment);
    }

    @Test
    void createCommentsWithMockMVC() throws Exception {
        Comment comment = getComment();
        CommentDTO commentDto = getCommentDTO();
        doReturn(comment)
                .when(commentService).save(anyLong(), any());
        doReturn(commentDto)
                .when(commentMapper).commentToCommentDTO(comment);

        mockMvc.perform(MockMvcRequestBuilders.post("/news_applications/v1/comment/1/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(commentDto))
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("id", Matchers.is("1")))
                .andExpect(MockMvcResultMatchers.jsonPath("text", Matchers.is("Text1")))
                .andExpect(MockMvcResultMatchers.jsonPath("userName", Matchers.is("User1")));

        verify(commentService).save(ID, commentDto);
        verify(commentMapper).commentToCommentDTO(comment);
    }

    @Test
    void updateCommentsWithMockMVC() throws Exception {
        Comment comment = getComment();
        CommentDTO commentDto = getCommentDTO();
        doReturn(comment)
                .when(commentService).update(anyLong(), any());
        doReturn(commentDto)
                .when(commentMapper).commentToCommentDTO(comment);

        mockMvc.perform(MockMvcRequestBuilders.put("/news_applications/v1/comment/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(commentDto))
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("id", Matchers.is("1")))
                .andExpect(MockMvcResultMatchers.jsonPath("text", Matchers.is("Text1")))
                .andExpect(MockMvcResultMatchers.jsonPath("userName", Matchers.is("User1")));

        verify(commentService).update(ID, commentDto);
        verify(commentMapper).commentToCommentDTO(comment);
    }

    @Test
    void deleteCommentsWithMockMVC() throws Exception {
        doNothing().when(commentService).delete(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete("/news_applications/v1/comment/1")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(commentService).delete(ID);
    }
}
