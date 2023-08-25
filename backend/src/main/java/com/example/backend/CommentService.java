package com.example.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BookRepository bookRepository;

    public void addCommentAndAssociateWithBook(Long bookId, String commentContent) {
        Book book = bookRepository.findById(bookId).orElse(null);

        if (book != null) {
            Comment comment = new Comment(commentContent);
            comment.setBook(book); // Associate the comment with the book

            commentRepository.save(comment); // Save the comment
        }
    }
}

