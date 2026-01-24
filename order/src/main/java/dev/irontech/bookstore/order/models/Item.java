package dev.irontech.bookstore.order.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(indexes = {
        @Index(name = "IDX_ITEM_ARTICLE_NUMBER", columnList = "articleNumber")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, name = "article_number")
    private String articleNumber;

    @Column(nullable = false)
    private String title;
}