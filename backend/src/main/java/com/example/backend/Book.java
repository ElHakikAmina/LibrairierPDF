package com.example.backend;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@NoArgsConstructor
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private String description;
    private String file_path; // Assuming this field stores the file name

    // No need to explicitly define constructors, getters, and setters
    public Book( String title, String author, String description, String file_path) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.file_path = file_path;
    }


    public void setFilePath(String file_path) {
        this.file_path = file_path;
    }
}

