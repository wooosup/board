package hello.board.service.user;

import hello.board.domain.comment.CommentDto;
import hello.board.domain.post.MyPagePostDto;
import hello.board.domain.user.User;
import hello.board.domain.user.UserForm;
import hello.board.domain.user.UserPostsAndCommentsDto;
import hello.board.repository.CommentRepository;
import hello.board.repository.PostRepository;
import hello.board.repository.UserRepository;
import hello.board.service.EntityFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final EntityFinder entityFinder;

    @Transactional
    public Long saveUser(UserForm form) {

        String encodedPassword = bCryptPasswordEncoder.encode(form.getPassword());
        User savedUser = form.toEntity(encodedPassword);

        userRepository.save(savedUser);
        return savedUser.getId();
    }

    public UserPostsAndCommentsDto getUserPostsAndComments(String username) {
        User user = entityFinder.getLoginUser(username);

        List<MyPagePostDto> posts = getMyPagePostDto(user);

        List<CommentDto> comments = getCommentDto(user);

        return new UserPostsAndCommentsDto(posts, comments);
    }

    public boolean isUsernameDuplicate(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean isNicknameDuplicate(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    private List<MyPagePostDto> getMyPagePostDto(User user) {
        return postRepository.findByUserOrderByCreateDateTimeDesc(user).stream()
                .map(MyPagePostDto::of)
                .toList();
    }

    private List<CommentDto> getCommentDto(User user) {
        return commentRepository.findByUserOrderByCreateDateTimeDesc(user).stream()
                .map(CommentDto::of)
                .toList();
    }

}
