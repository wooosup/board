package hello.board.service.user;

import hello.board.domain.user.User;
import hello.board.domain.user.UserRepository;
import hello.board.global.validator.UserValidator;
import hello.board.infrastructure.web.user.request.UserForm;
import hello.board.infrastructure.web.user.response.UserLikedPostsDto;
import hello.board.infrastructure.web.user.response.UserPostsAndCommentsDto;
import hello.board.infrastructure.web.user.response.UserResponse;
import hello.board.service.EntityFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityFinder entityFinder;
    private final UserValidator userValidator;

    @Transactional
    public UserResponse saveUser(UserForm form) {
        userValidator.validateDuplicateUser(form);

        User user = User.create(form, passwordEncoder);

        userRepository.save(user);
        return UserResponse.of(user);
    }

    @Transactional
    public void deleteMember(String username) {
        User user = entityFinder.getLoginUser(username);

        userRepository.delete(user);
    }

    public UserPostsAndCommentsDto getUserPostsAndComments(String username) {
        User user = entityFinder.getLoginUser(username);

        return UserPostsAndCommentsDto.of(user);
    }

    public UserLikedPostsDto getUserLikedPosts(String username) {
        User user = entityFinder.getLoginUser(username);

        return UserLikedPostsDto.of(user.getLikes());
    }

}
