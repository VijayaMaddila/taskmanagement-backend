package com.taskmanagement.controller;

import com.taskmanagement.dto.CommentDto;
import com.taskmanagement.model.Comment;
import com.taskmanagement.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    
    @PostMapping
    public ResponseEntity<Comment> addComment(@RequestBody CommentDto dto) {
        return ResponseEntity.ok(commentService.addComment(dto));
    }
    @GetMapping("/task/{taskId}")
    public ResponseEntity<Page<Comment>> getCommentsByTask(
            @PathVariable Long taskId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(commentService.getCommentsByTask(taskId, page, size));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(commentService.updateComment(id, body.get("content")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok("Comment deleted successfully");
    }
}
