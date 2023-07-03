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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import ru.clevertec.newsservice.controller.NewsController;
import ru.clevertec.newsservice.dto.NewsDTO;
import ru.clevertec.newsservice.mapper.NewsMapper;
import ru.clevertec.newsservice.dao.News;
import ru.clevertec.newsservice.service.NewsService ;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static ru.clevertec.newsservice.util.Constants.*;
import static ru.clevertec.newsservice.util.TestData.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ControllerNewsTest {

    @InjectMocks
    private NewsController newsController;

    @Mock
    private NewsService newsService;

    @Mock
    private NewsMapper newsMapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void buildMockMVC() {
        mockMvc = MockMvcBuilders.standaloneSetup(newsController)
                .build();
    }

    @AfterEach
    public void verifyNoMoreInteractionsCommentService() {
        Mockito.verifyNoMoreInteractions(newsService);
    }

    @Test
    void findAllNewsWithMockMVC() throws Exception {
        News news = getNews();
        NewsDTO newsDto = getNewsDto();
        doReturn(Collections.singletonList(news))
                .when(newsService).findAll(any(), anyInt(), anyInt(), anyString());
        doReturn(newsDto)
                .when(newsMapper).newsToNewsDTO(news);

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("search", SEARCH_STR);
        requestParams.add("pageNo", String.valueOf(PAGE_NO));
        requestParams.add("pageSize", String.valueOf(PAGE_SIZE));
        requestParams.add("sortBy", SORTING);
        mockMvc.perform(MockMvcRequestBuilders.get("/news_applications/v1/news")
                .params(requestParams)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is("1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title", Matchers.is("Title1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].text", Matchers.is("Text1")));

        verify(newsService).findAll(SEARCH_STR, PAGE_NO, PAGE_SIZE, SORTING);
        verify(newsMapper).newsToNewsDTO(news);
    }

    @Test
    void findByIdNewsWithMockMVC() throws Exception {
        News news = getNews();
        NewsDTO newsDto = getNewsDto();
        doReturn(news)
                .when(newsService).findById(anyLong());
        doReturn(newsDto)
                .when(newsMapper).newsToNewsDTO(news);

        mockMvc.perform(MockMvcRequestBuilders.get("/news_applications/v1/news/1")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("id", Matchers.is("1")))
                .andExpect(MockMvcResultMatchers.jsonPath("title", Matchers.is("Title1")))
                .andExpect(MockMvcResultMatchers.jsonPath("text", Matchers.is("Text1")));

        verify(newsService).findById(ID);
        verify(newsMapper).newsToNewsDTO(news);
    }

    @Test
    void createNewsWithMockMVC() throws Exception {
        News news = getNews();
        NewsDTO newsDto = getNewsDto();
        doReturn(news)
                .when(newsService).save(any());
        doReturn(newsDto)
                .when(newsMapper).newsToNewsDTO(news);

        mockMvc.perform(MockMvcRequestBuilders.post("/news_applications/v1/news")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(getNewsDtoRequest()))
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("id", Matchers.is("1")))
                .andExpect(MockMvcResultMatchers.jsonPath("title", Matchers.is("Title1")))
                .andExpect(MockMvcResultMatchers.jsonPath("text", Matchers.is("Text1")));

        verify(newsService).save(newsDto);
        verify(newsMapper).newsToNewsDTO(news);
    }

    @Test
    void updateNewsWithMockMVC() throws Exception {
        News news = getNews();
        NewsDTO newsDto = getNewsDto();
        doReturn(news)
                .when(newsService).update(anyLong(), any());
        doReturn(newsDto)
                .when(newsMapper).newsToNewsDTO(news);

        mockMvc.perform(MockMvcRequestBuilders.put("/news_applications/v1/news/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(getNewsDtoRequest()))
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("id", Matchers.is("1")))
                .andExpect(MockMvcResultMatchers.jsonPath("title", Matchers.is("Title1")))
                .andExpect(MockMvcResultMatchers.jsonPath("text", Matchers.is("Text1")));

        verify(newsService).update(ID, newsDto);
        verify(newsMapper).newsToNewsDTO(news);
    }

    @Test
    void deleteNewsWithMockMVC() throws Exception {
        doNothing().when(newsService).delete(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete("/news_applications/v1/news/1")
                .header(HttpHeaders.AUTHORIZATION)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(newsService).delete(ID);
    }

}