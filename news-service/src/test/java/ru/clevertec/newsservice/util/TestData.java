package ru.clevertec.newsservice.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import ru.clevertec.newsservice.client.dto.Role;
import ru.clevertec.newsservice.client.dto.RoleEnum;
import ru.clevertec.newsservice.client.dto.User;
import ru.clevertec.newsservice.dao.Comment;
import ru.clevertec.newsservice.dao.News;
import ru.clevertec.newsservice.dto.CommentDTO;
import ru.clevertec.newsservice.dto.NewsDTO;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestData {

    private static final ObjectMapper mapper = JsonMapper.builder()
            .findAndAddModules()
            .build();

   private static LocalDateTime getCurrentTime(){
        LocalDateTime fixedTime = LocalDateTime.of(2023, 6, 29, 15, 10);
        return fixedTime;
    }

    public static List<News> getNewsList() {
        List<News> news = new ArrayList<>();
        news.add(News.builder().id(1L).title("Title1").text("Text1").time(getCurrentTime()).userName("User1").build());
        news.add(News.builder().id(2L).title("Title2").text("Text2").time(getCurrentTime()).userName("User2").build());
        news.add(News.builder().id(3L).title("Title3").text("Text3").time(getCurrentTime()).userName("User3").build());
        return news;
    }

    public static List<NewsDTO> getNewsListDTO() {
        List<NewsDTO> news = new ArrayList<>();
        news.add(NewsDTO.builder().id(1L).title("Title1").text("Text1").build());
        news.add(NewsDTO.builder().id(2L).title("Title2").text("Text2").build());
        news.add(NewsDTO.builder().id(3L).title("Title3").text("Text3").build());
        return news;
    }

    public static List<Comment> getCommentList() {
        List<Comment> comments = new ArrayList<>();
        comments.add(Comment.builder().id(1L).text("Text1").time(getCurrentTime())
                .userName("User1").build());
        comments.add(Comment.builder().id(2L).text("Text2").time(getCurrentTime())
                .userName("User2").build());
        comments.add(Comment.builder().id(3L).text("Text3").time(getCurrentTime())
                .userName("User3").build());
        return comments;
    }

    public static List<CommentDTO> getCommentDTOList() {
        List<CommentDTO> comments = new ArrayList<>();
        comments.add(CommentDTO.builder().text("Text1").userName("User1").build());
        comments.add(CommentDTO.builder().text("Text2").userName("User2").build());
        comments.add(CommentDTO.builder().text("Text3").userName("User3").build());
        return comments;
    }


    public static String buildUserAdminResponse() throws IOException {
        System.out.println("IN buildUserAdminResponse() !!!!!!!!!!!");
        String s = mapper.writeValueAsString(mapper.readValue(Paths.get("src/test/resources/json/personWithAnimal.json").toFile(),
                Comment.class));
        System.out.println(s + "!!!!!!!!!!!!");
        return s;
    }

    public static User getUserAdmin() {
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(1L, RoleEnum.ADMIN));
        return User.builder().id(1L).username("User1").password("1234").firstName("Ivan").lastName("Ivanov")
                .roles(roles).build();
    }

    public static User getUserSubscriber() {
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(1L, RoleEnum.SUBSCRIBER));
        return User.builder().id(1L).username("User1").password("1234").firstName("Ivan").lastName("Ivanov")
                .roles(roles).build();
    }

    public static User getUserJournalist() {
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(1L, RoleEnum.JOURNALIST));
        return User.builder().id(1L).username("User1").password("1234").firstName("Ivan").lastName("Ivanov")
                .roles(roles).build();
    }

    public static User getUserWithoutRole() {
        return User.builder().id(1L).username("User1").password("1234").firstName("Ivan").lastName("Ivanov")
                .roles(new HashSet<>()).build();
    }


    public static News getNews() {
        return getNewsList().get(0);
    }

    public static NewsDTO getNewsDto() {
        return getNewsListDTO().get(0);
    }
    public static NewsDTO getNewsDtoRequest() {
        return NewsDTO.builder().title("Title1").text("Text1").build();
    }

    public static Comment getComment() {
        return getCommentList().get(0);
    }

    public static CommentDTO getCommentDTO() {
        return getCommentDTOList().get(0);
    }
}
