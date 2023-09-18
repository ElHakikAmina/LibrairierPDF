package com.example.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "http://localhost:4200") // Allow requests from Angular app
public class BookController {
    @Autowired
    private StorageService storageService;

    @Autowired
    private BookRepository bookRepository;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam("title") String title,
                                             @RequestParam("author") String author,
                                             @RequestParam("description") String description) {
        try {
            String filePath = storageService.storeFile(file);
            Book book = new Book();
            book.setTitle(title);
            book.setAuthor(author);
            book.setDescription(description);
            book.setFilePath(filePath);
            bookRepository.save(book);

            return ResponseEntity.ok("File uploaded successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file.");
        }
    }

    @GetMapping
    public List<Book> getAllBooks() {
        //return bookRepository.findAll();
        Sort sortByDescId = Sort.by(Sort.Order.desc("id"));

        // Use the custom sorting in the repository method
        return bookRepository.findAll(sortByDescId);
    }

    @GetMapping("/files/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = storageService.loadFileAsResource(fileName);

        // Determine the content type of the file
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            // Handle or log this exception if needed
        }

        // If the content type is unknown, set it to "application/octet-stream"
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        // Build the response
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
        return ResponseEntity.noContent().build(); // Return a no content response
    }


    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateBook(@PathVariable Long id, @RequestBody Book updatedBook) {
        Book existingBook = bookRepository.findById(id).orElse(null);
        if (existingBook == null) {
            return ResponseEntity.notFound().build();
        }

        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setDescription(updatedBook.getDescription());
        bookRepository.save(existingBook);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Book updated successfully");

        return ResponseEntity.ok(response);
    }

}