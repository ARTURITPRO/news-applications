package ru.clevertec.newsservice.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <d>An entity that is mapped to with a news table in the database.</d>
 *
 * @author Artur Malashkov
 * @since 17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "news")
@jakarta.persistence.Entity
public class News implements Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime time;

    private String title;
    private String text;

    @Column(name = "username")
    private String userName;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "news")
    private List<Comment> comments;

}
