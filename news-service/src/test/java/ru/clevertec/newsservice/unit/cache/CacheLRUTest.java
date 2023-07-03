package ru.clevertec.newsservice.unit.cache;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.newsservice.cache.Cache;
import ru.clevertec.newsservice.dao.Comment;
import ru.clevertec.newsservice.cache.impl.LFUCache;
import ru.clevertec.newsservice.dao.Entity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.clevertec.newsservice.util.Constants.ID;
import static ru.clevertec.newsservice.util.TestData.getComment;
import static ru.clevertec.newsservice.util.TestData.getCommentList;

class CacheLRUTest {

    private static Cache commentCacheLRU;

    @BeforeAll
    static void setUp() {
        commentCacheLRU = LFUCache.getInstance();
    }
    @BeforeEach
    void init() {
        commentCacheLRU.clear();
        getCommentList().stream().forEach(commentCacheLRU::save);
    }

    @Test
    void findCommentByIdAndTypeClass() {
        Comment expectedComment = getCommentList().get(0);
        Comment commentData = (Comment)commentCacheLRU.find(ID, Comment.class).get();
        assertThat(commentData.getId()).isEqualTo(expectedComment.getId());
        assertThat(commentData.getText()).isEqualTo(expectedComment.getText());
        assertThat(commentData.getUserName()).isEqualTo(expectedComment.getUserName());
    }

    @Test
    void findCommentByIdShouldReturnEmpty() {
        Optional <Entity> commentData = commentCacheLRU.find(4L, Comment.class);
        assertThat(commentData).isEmpty();
    }

    @Test
    void saveCommentInLruCache() {
        Comment expectedComment = getComment();
        commentCacheLRU.save(expectedComment);
        Comment actualCommentData = (Comment) commentCacheLRU.find(expectedComment.getId(), Comment.class).get();
        assertThat(actualCommentData).isEqualTo(expectedComment);
    }

    @Test
    void updateCommentInLruCache() {
        Comment comment = (Comment) commentCacheLRU.find(1L, Comment.class).get();
        comment.setText("New text");
        comment.setUserName("New user");
        commentCacheLRU.update(comment);
        Comment updatedComment = (Comment) commentCacheLRU.find(1L, Comment.class).get();
        assertThat(updatedComment.getText()).isEqualTo("New text");
        assertThat(updatedComment.getUserName()).isEqualTo("New user");
    }

    @Test
    void deleteCommentInLruCache() {
        Comment comment = getCommentList().get(0);
        commentCacheLRU.delete(comment.getId(), Comment.class);
        Optional<Entity> actualCommentData = commentCacheLRU.find(comment.getId(), Comment.class);
        assertThat(actualCommentData).isEmpty();
    }

    @Test
    void replacingCommentInCacheThatHasNotBeenYUedForLongTime() {
        Comment comment = Comment.builder().id(4L).text("New text").userName("New user").build();
        commentCacheLRU.save(comment);
        assertThat(commentCacheLRU.find(3L, Comment.class)).isEmpty();
    }
}