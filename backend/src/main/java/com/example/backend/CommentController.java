package com.example.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/add-comment")
    public ResponseEntity<String> addCommentToBook(
            @RequestParam Long bookId,
            @RequestParam String commentContent) {

        commentService.addCommentAndAssociateWithBook(bookId, commentContent);
        return ResponseEntity.ok("Comment added successfully");
    }
}

