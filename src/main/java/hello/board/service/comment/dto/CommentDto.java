package hello.board.service.comment.dto;

import hello.board.domain.TimeUtil;
import hello.board.domain.entity.comment.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CommentDto {
    private final Long id;
    private final Long postId;
    private final String content;
    private final LocalDateTime createdDateTime;
    private final String nickname;
    private final String username;
    private final String createTime;
    private final boolean deleted;
    private final Long parentId;
    private final List<CommentDto> children = new ArrayList<>();

    @Builder
    private CommentDto(Long id, Long postId, String content, LocalDateTime createdDate, String nickname, String username, boolean deleted, Long parentId) {
        this.id = id;
        this.postId = postId;
        this.content = content;
        this.createdDateTime = createdDate;
        this.deleted = deleted;
        this.parentId = parentId;
        this.createTime = TimeUtil.getTime(createdDateTime);
        this.nickname = nickname;
        this.username = username;
    }

    public static CommentDto of(Comment comment) {
        CommentDto dto = CommentDto.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .content(comment.getContent())
                .createdDate(comment.getCreateDateTime())
                .nickname(comment.getUser().getNickname())
                .username(comment.getUser().getUsername())
                .deleted(comment.isDeleted())
                .parentId(comment.getParent() == null ? null : comment.getParent().getId())
                .build();

        if (comment.getChildren() != null) {
            for (Comment child : comment.getChildren()) {
                dto.getChildren().add(of(child));
            }
        }
        return dto;
    }
}