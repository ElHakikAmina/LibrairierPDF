package com.example.backend;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    @JsonBackReference // Use @JsonBackReference here
    private Book book;

    @Column(nullable = false)
    private String content;

    // Constructors
    public Comment(String content) {
        this.content = content;
    }
}
