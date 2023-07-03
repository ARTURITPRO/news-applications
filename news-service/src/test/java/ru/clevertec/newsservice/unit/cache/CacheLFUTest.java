package ru.clevertec.newsservice.unit.cache;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.newsservice.cache.Cache;
import ru.clevertec.newsservice.cache.impl.LFUCache;
import ru.clevertec.newsservice.dao.Comment;
import ru.clevertec.newsservice.dao.Entity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.clevertec.newsservice.util.Constants.ID;
import static ru.clevertec.newsservice.util.TestData.getComment;
import static ru.clevertec.newsservice.util.TestData.getCommentList;

class CacheLFUTest {

    private static Cache LfuCacheForComment;

    @BeforeEach
    void init() {
        LfuCacheForComment.clear();
        getCommentList().stream().forEach(LfuCacheForComment::save);
    }

    @BeforeAll
    static void setUp() {
        LfuCacheForComment = LFUCache.getInstance();
    }

    @Test
    void findCommentByIdAndTypeClass() {
        Comment expectedComment = getCommentList().get(0);
        Comment actualComment = (Comment) LfuCacheForComment.find(ID, Comment.class).get();
        assertThat(actualComment).isEqualTo(expectedComment);
    }

    @Test
    void saveCommentInLfuCache() {
        Comment expectedComment = getComment();
        LfuCacheForComment.save(expectedComment);
        Comment commentData = (Comment) LfuCacheForComment.find(expectedComment.getId(), Comment.class).get();
        assertThat(commentData).isEqualTo(expectedComment);
    }

    @Test
    void updateCommentInLfuCache() {
        Comment comment = (Comment) LfuCacheForComment.find(1L, Comment.class).get();
        comment.setText("New text");
        comment.setUserName("New user");
        LfuCacheForComment.update(comment);
        Comment updatedComment = (Comment) LfuCacheForComment.find(1L, Comment.class).get();
        assertThat(updatedComment.getText()).isEqualTo("New text");
        assertThat(updatedComment.getUserName()).isEqualTo("New user");
    }

    @Test
    void deleteCommentInLfuCache() {
        Comment comment = getCommentList().get(0);
        LfuCacheForComment.delete(comment.getId(), Comment.class);
        Optional<Entity> actualCommentData =  LfuCacheForComment.find(comment.getId(), Comment.class);
        assertThat(actualCommentData).isEmpty();
    }

    @Test
    void checkingCommentWithSmallFrequencyThatShouldBeRemovedFromCacheIfThereIsNotEnoughCache() {
        LfuCacheForComment.find(1L, Comment.class );
        LfuCacheForComment.find(2L, Comment.class );

        Comment comment = Comment.builder().id(4L).text("New text").userName("New user").build();
        LfuCacheForComment.save(comment);
        assertThat(LfuCacheForComment.find(3L, Comment.class )).isEmpty();
    }
}
