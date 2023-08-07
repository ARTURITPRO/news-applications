package ru.clevertec.newsservice.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * <d>An entity that is mapped to with a comments table in the database.</d>
 *
 * @author Artur Malashkov
 * @since 17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comments")
@jakarta.persistence.Entity
public class Comment implements Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime time;

    private String text;

    @Column(name = "username")
    private String userName;

    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "news_id", nullable = false)
    private News news;

}
