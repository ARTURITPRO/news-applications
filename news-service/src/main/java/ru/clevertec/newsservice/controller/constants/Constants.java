package ru.clevertec.newsservice.controller.constants;

import ru.clevertec.newsservice.controller.CommentController;
import ru.clevertec.newsservice.controller.NewsController;

/**
 * <d> This class simply provides constants for given controllers.</d>
 *
 * @see CommentController
 * @see NewsController
 * @author Artur Malashkov
 * @since 17
 */
public class Constants {

    public static final String NEWS_URL = "/news_applications/v1/news";
    public static final String COMMENTS_URL = "/news_applications/v1/comment";
    public static final String PAGE_NO = "0";
    public static final String PAGE_SIZE = "10";
    public static final String SORT_BY = "id";
}
