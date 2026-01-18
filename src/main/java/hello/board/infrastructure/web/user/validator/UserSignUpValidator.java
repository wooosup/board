package hello.board.infrastructure.web.user.validator;

import hello.board.domain.user.UserRepository;
import hello.board.infrastructure.web.user.request.UserForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class UserSignUpValidator implements Validator {

    private final UserRepository userRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserForm form = (UserForm) target;

        if (userRepository.existsByUsername(form.getUsername())) {
            errors.rejectValue("username", "duplicate", "이미 존재하는 아이디입니다.");
        }
        if (userRepository.existsByNickname(form.getNickname())) {
            errors.rejectValue("nickname", "duplicate", "이미 존재하는 닉네임입니다.");
        }
    }
}
