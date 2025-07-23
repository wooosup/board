package hello.board.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {
    USER("사용자"),
    ADMIN("관리자");

    private final String description;
}
