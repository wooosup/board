package hello.board.service.user;

import hello.board.infrastructure.web.user.response.UserResponse;
import hello.board.domain.like.Like;
import hello.board.domain.user.User;
import hello.board.domain.comment.CommentRepository;
import hello.board.domain.like.LikeRepository;
import hello.board.domain.post.PostRepository;
import hello.board.domain.user.UserRepository;
import hello.board.service.EntityFinder;
import hello.board.infrastructure.web.comment.response.CommentDto;
import hello.board.infrastructure.web.post.response.LikedPostDto;
import hello.board.infrastructure.web.post.response.MyPagePostDto;
import hello.board.service.user.dto.UserForm;
import hello.board.service.user.dto.UserLikedPostsDto;
import hello.board.service.user.dto.UserPostsAndCommentsDto;
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
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final EntityFinder entityFinder;

    @Transactional
    public UserResponse saveUser(UserForm form) {
        duplicate(form);

        String encodedPassword = bCryptPasswordEncoder.encode(form.getPassword());
        User savedUser = form.toEntity(encodedPassword);

        userRepository.save(savedUser);
        return UserResponse.of(savedUser);
    }

    public UserPostsAndCommentsDto getUserPostsAndComments(String username) {
        User user = entityFinder.getLoginUser(username);

        List<MyPagePostDto> posts = getMyPagePostDto(user);
        List<CommentDto> comments = getCommentDto(user);

        return UserPostsAndCommentsDto.of(posts, comments);
    }

    public UserLikedPostsDto getUserLikedPosts(String username) {
        User user = entityFinder.getLoginUser(username);
        List<Like> userLikes = likeRepository.findByUserOrderByCreateDateTimeDesc(user);

        List<LikedPostDto> likePosts = getLikePosts(userLikes);

        return UserLikedPostsDto.of(likePosts);
    }

    // 입력 폼 검증
    public boolean isUsernameDuplicate(String username) {
        return userRepository.existsByUsername(username);
    }
    public boolean isNicknameDuplicate(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    private void duplicate(UserForm form) {
        if (userRepository.existsByUsername(form.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }
        if (userRepository.existsByNickname(form.getNickname())) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }
    }

    private List<MyPagePostDto> getMyPagePostDto(User user) {
        return postRepository.findByUserOrderByCreateDateTimeDesc(user).stream()
                .map(MyPagePostDto::of)
                .toList();
    }

    private static List<LikedPostDto> getLikePosts(List<Like> userLikes) {
        return userLikes.stream()
                .map(LikedPostDto::of)
                .toList();
    }

    private List<CommentDto> getCommentDto(User user) {
        return commentRepository.findByUserOrderByCreateDateTimeDesc(user).stream()
                .map(CommentDto::of)
                .toList();
    }
}
