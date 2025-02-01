package hello.board.service;

import hello.board.domain.entity.comment.Comment;
import hello.board.domain.entity.message.Message;
import hello.board.domain.entity.post.Post;
import hello.board.domain.entity.user.User;
import hello.board.exception.EntityNotFoundException;
import hello.board.domain.repository.CommentRepository;
import hello.board.domain.repository.MessageRepository;
import hello.board.domain.repository.PostRepository;
import hello.board.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntityFinder {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final MessageRepository messageRepository;

    public Post getPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
    }

    public Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));
    }

    public User getLoginUser(String loginId) {
        return userRepository.findByUsername(loginId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }

    public Message getMessage(Long messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new EntityNotFoundException("해당 쪽지를 찾을 수 없습니다."));
    }

    //  id를 닉네임으로 반환
    public String getUserNickname(String username) {
        return userRepository.findByUsername(username)
                .map(User::getNickname)
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다."));
    }
}
