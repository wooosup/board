package hello.board.service;

import hello.board.domain.entity.comment.Comment;
import hello.board.domain.entity.post.Post;
import hello.board.domain.entity.user.User;
import hello.board.domain.repository.CommentRepository;
import hello.board.service.comment.CommentService;
import hello.board.service.comment.dto.CommentDto;
import hello.board.service.comment.dto.CommentForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private EntityFinder entityFinder;

    @InjectMocks
    private CommentService commentService;

    private User mockUser;
    private Post mockPost;
    private Comment parentComment;
    private Comment childComment;

    @BeforeEach
    void setUp() {
        mockUser = User.builder()
                .username("wss3325")
                .nickname("seop")
                .build();
        ReflectionTestUtils.setField(mockUser, "id", 1L);

        mockPost = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        ReflectionTestUtils.setField(mockPost, "id", 1L);

        parentComment = Comment.builder()
                .content("부모 댓글")
                .post(mockPost)
                .user(mockUser)
                .parent(null)
                .build();
        ReflectionTestUtils.setField(parentComment, "id", 1L);

        childComment = Comment.builder()
                .content("자식 댓글")
                .post(mockPost)
                .user(mockUser)
                .parent(parentComment)
                .build();
        ReflectionTestUtils.setField(childComment, "id", 2L);

        parentComment.getChildren().add(childComment);
    }

    @DisplayName("대댓글 등록 성공")
    @Test
    void savedComment() throws Exception {
        //given
        CommentForm form = new CommentForm();
        form.setContent("자식 댓글");
        form.setPostId(mockPost.getId());
        form.setParentId(parentComment.getId());

        given(entityFinder.getLoginUser("wss3325")).willReturn(mockUser);
        given(entityFinder.getPost(mockPost.getId())).willReturn(mockPost);
        given(entityFinder.getComment(parentComment.getId())).willReturn(parentComment);

        //when
        commentService.saveComment(form, "wss3325");

        //then
        ArgumentCaptor<Comment> captor = ArgumentCaptor.forClass(Comment.class);
        verify(commentRepository, times(1)).save(captor.capture());

        Comment savedReply = captor.getValue();
        assertThat(savedReply.getParent()).isEqualTo(parentComment);
        assertThat(savedReply.getContent()).isEqualTo("자식 댓글");
        assertThat(parentComment.getChildren()).hasSize(1);
        assertThat(parentComment.getChildren().get(0)).isEqualTo(savedReply);
    }

    @Test
    @DisplayName("삭제할 댓글이 자식이 있으면, 삭제 표시만 한다.")
    void deleteShouldMarkDeletedIfHasChildren() throws Exception {
        //given
        given(entityFinder.getComment(parentComment.getId())).willReturn(parentComment);
        given(entityFinder.getLoginUser(mockUser.getUsername())).willReturn(mockUser);

        //when
        commentService.deleteComment(parentComment.getId(), mockUser.getUsername());

        //then
        assertThat(parentComment.isDeleted()).isTrue();
        verify(commentRepository, never()).delete(parentComment);
    }

    @Test
    @DisplayName("삭제할 댓글에 자식이 없으면 삭제한다.")
    void deleteComment_noChildren() throws Exception {
        // given
        Long commentId = 2L;
        childComment.getChildren().clear();
        given(entityFinder.getComment(commentId)).willReturn(childComment);
        given(entityFinder.getLoginUser("mockUser")).willReturn(mockUser);

        // when
        commentService.deleteComment(commentId, "mockUser");

        // then
        verify(commentRepository).delete(childComment);
    }

    @Test
    void findAll() throws Exception {
        //given
        given(entityFinder.getPost(mockPost.getId())).willReturn(mockPost);
        mockPost.getComments().add(parentComment);

        //when
        List<CommentDto> result = commentService.findAll(mockPost.getId());

        //then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(parentComment.getId());
    }

    @DisplayName("댓글을 수정한다.")
    @Test
    void updateComment() throws Exception {
        //given
        String content = "수정된 댓글";

        given(entityFinder.getComment(parentComment.getId())).willReturn(parentComment);
        given(entityFinder.getLoginUser(mockUser.getUsername())).willReturn(mockUser);

        //when
        commentService.updateComment(content, parentComment.getId(), mockUser.getUsername());

        //then
        assertThat(parentComment.getContent()).isEqualTo(content);
    }
}