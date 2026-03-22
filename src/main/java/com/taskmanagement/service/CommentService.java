package com.taskmanagement.service;

import com.taskmanagement.dto.CommentDto;
import com.taskmanagement.model.Comment;
import com.taskmanagement.model.Task;
import com.taskmanagement.model.User;
import com.taskmanagement.repository.CommentRepository;
import com.taskmanagement.repository.TaskRepository;
import com.taskmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SlackService slackService;

    public Comment addComment(CommentDto dto) {
        Task task = taskRepository.findById(dto.getTaskId())
                .orElseThrow(() -> new RuntimeException("Task not found: " + dto.getTaskId()));
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found: " + dto.getUserId()));

        Comment comment = new Comment();
        comment.setContent(dto.getContent());
        comment.setTask(task);
        comment.setUser(user);
        Comment saved = commentRepository.save(comment);
        slackService.notifyCommentAdded(saved);
        return saved;
    }

    public Page<Comment> getCommentsByTask(Long taskId, int page, int size) {
        return commentRepository.findByTaskIdOrderByCreatedAtAsc(taskId, PageRequest.of(page, size));
    }

    public Comment updateComment(Long id, String content) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found: " + id));
        String oldContent = comment.getContent();
        comment.setContent(content);
        Comment updated = commentRepository.save(comment);
        slackService.notifyCommentUpdated(updated, oldContent);
        return updated;
    }

    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found: " + id));
        slackService.notifyCommentDeleted(comment);
        commentRepository.delete(comment);
    }
}
