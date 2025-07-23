package hello.board.infrastructure.persistence.repository.comment;

import hello.board.domain.comment.Comment;
import hello.board.domain.comment.CommentRepository;
import hello.board.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class CommentRepositoryImpl implements CommentRepository {

    private final CommentJpaRepository commentJpaRepository;

    @Override
    public List<Comment> findByUserOrderByCreateDateTimeDesc(User user) {
        return commentJpaRepository.findByUserOrderByCreateDateTimeDesc(user);
    }

    @Override
    public long countByParentIdAndDeletedFalse(Long parentId) {
        return commentJpaRepository.countByParentIdAndDeletedFalse(parentId);
    }

    @Override
    public Comment save(Comment comment) {
        return commentJpaRepository.save(comment);
    }

    @Override
    public Optional<Comment> findById(Long id) {
        return commentJpaRepository.findById(id);
    }

    @Override
    public void delete(Comment comment) {
        commentJpaRepository.delete(comment);
    }

    @Override
    public List<Comment> saveAll(List<Comment> comments) {
        return commentJpaRepository.saveAll(comments);
    }

    @Override
    public void flush() {
        commentJpaRepository.flush();
    }
}
