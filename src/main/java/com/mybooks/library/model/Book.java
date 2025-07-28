package com.mybooks.library.model;

import jakarta.persistence.*;
import lombok.*;


@Setter
@Getter
@NoArgsConstructor
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 20)
    private String genre;

    @Column(name = "publication_year", nullable = false, length = 4)
    private String publicationYear;

    @Column(nullable = false)
    private Integer pages;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @Column(name = "cover_photo_url", length = 512)
    private String coverPhotoUrl;
}
