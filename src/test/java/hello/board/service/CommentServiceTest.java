package hello.board.service;

import hello.board.domain.entity.comment.Comment;
import hello.board.service.comment.dto.CommentForm;
import hello.board.domain.entity.post.Post;
import hello.board.domain.entity.user.User;
import hello.board.domain.repository.CommentRepository;
import hello.board.service.comment.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


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

}