package hello.board.controller.post;

import hello.board.domain.Role;
import hello.board.domain.entity.post.Post;
import hello.board.domain.entity.user.User;
import hello.board.domain.repository.PostRepository;
import hello.board.domain.repository.UserRepository;
import hello.board.service.image.FileStore;
import hello.board.service.post.PostService;
import hello.board.service.post.dto.MainPostDto;
import hello.board.service.post.dto.PostSearch;
import hello.board.service.post.dto.UpdatePostForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @MockBean
    private FileStore fileStore;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .username("wss3325")
                .nickname("keke")
                .password("1234")
                .grade(Role.USER)
                .build();
        userRepository.save(user);
        Post post = Post.builder()
                .user(user)
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(post);
    }

    @DisplayName("게시글의 목록 페이지를 보여준다.")
    @Test
    void postList() throws Exception {
        // given
        // when & then
        mockMvc.perform(get("/")
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("posts", "search", "currentPage", "totalPages", "startPage", "endPage"))
                .andExpect(view().name("posts/postList"))
                .andDo(print());
    }

    @DisplayName("검색한 결과를 포함한 게시글 목록 페이지를 보여준다.")
    @Test
    void postListAndSearch() throws Exception {
        // given
        // when & then
        mockMvc.perform(get("/")
                        .with(csrf())
                        .param("keyword", "제목")
                        .param("searchField", "title")
                )
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("posts", "search"))
                .andExpect(view().name("posts/postList"))
                .andDo(print());
    }

    @DisplayName("게시글 목록 페이지를 페이징 처리한다.")
    @Test
    void postListAndPaging() throws Exception {
        // given
        // when & then
        mockMvc.perform(get("/")
                        .with(csrf())
                        .param("page", "1")
                )
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("posts", "search", "totalPages"))
                .andExpect(view().name("posts/postList"))
                .andDo(print());
    }

    @DisplayName("게시글을 작성한다.")
    @Test
    @WithMockUser(username = "wss3325")
    void write() throws Exception {
        //given
        // when & then
        mockMvc.perform(post("/post/create")
                        .with(csrf())
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .param("title", "제목")
                        .param("content", "내용")
                )
                .andExpect(status().is3xxRedirection())
                .andDo(print());
    }

    @DisplayName("제목없이 게시글을 작성하면 오류가 발생한다.")
    @Test
    @WithMockUser(username = "wss3325")
    void writePostEmptyTitle() throws Exception {
        //given
        // when & then
        mockMvc.perform(post("/post/create")
                        .with(csrf())
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .param("title", "")
                        .param("content", "내용")
                )
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("form", "title"))
                .andExpect(view().name("posts/createPostForm"))
                .andDo(print());
    }

    @DisplayName("존재하지 않는 게시글은 조회할 수 없다.")
    @Test
    @WithMockUser(username = "wss3325")
    void viewPostNotExistence() throws Exception {
        //given
        // expect
        mockMvc.perform(get("/post/9999")
                        .with(csrf())
                )
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @DisplayName("게시글을 조회한다.")
    @Test
    @WithMockUser(username = "wss3325")
    void viewPost() throws Exception {
        //given
        Post savedPost = postRepository.findAll().get(0);

        // when & then
        mockMvc.perform(get("/post/" + savedPost.getId())
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("post"))
                .andExpect(view().name("posts/viewPost"))
                .andDo(print());
    }

    @DisplayName("게시글을 수정한다.")
    @Test
    @WithMockUser(username = "wss3325")
    void updatePost() throws Exception {
        //given
        Post savedPost = postRepository.findAll().get(0);
        UpdatePostForm updatePostForm = UpdatePostForm.builder()
                .title("라멘")
                .content("츠케멘")
                .build();

        //when
        mockMvc.perform(post("/post/edit/" + savedPost.getId())
                        .with(csrf())
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .param("title", updatePostForm.getTitle())
                        .param("content", updatePostForm.getContent())
                )
                .andExpect(status().is3xxRedirection())
                .andDo(print());

        //then
        assertThat(savedPost.getTitle()).isEqualTo("라멘");
        assertThat(savedPost.getContent()).isEqualTo("츠케멘");
    }

    @DisplayName("게시글 수정 권한이 없는 경우 오류가 발생한다.")
    @Test
    @WithMockUser(username = "1232144")
    void updatePostNotAuthority() throws Exception {
        //given
        Post savedPost = postRepository.findAll().get(0);
        UpdatePostForm updatePostForm = UpdatePostForm.builder()
                .title("라멘")
                .content("츠케멘")
                .build();

        // when & then
        mockMvc.perform(post("/post/edit/" + savedPost.getId())
                        .with(csrf())
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .param("title", updatePostForm.getTitle())
                        .param("content", updatePostForm.getContent())
                )
                .andExpect(status().isInternalServerError())
                .andDo(print());
    }

    @DisplayName("게시글 수정 페이지를 보여준다.")
    @Test
    @WithMockUser(username = "wss3325")
    void updatePostForm() throws Exception {
        //given
        Post savedPost = postRepository.findAll().get(0);

        //when & then
        mockMvc.perform(get("/post/edit/" + savedPost.getId())
                        .with(csrf())
                        .contentType(APPLICATION_FORM_URLENCODED)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("posts/updatePostForm"))
                .andDo(print());
    }

    @DisplayName("게시글을 삭제한다.")
    @Test
    @WithMockUser(username = "wss3325")
    void delete() throws Exception {
        //given
        Post savedPost = postRepository.findAll().get(0);

        //when
        mockMvc.perform(post("/post/delete/" + savedPost.getId())
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andDo(print());

        //then
        assertThat(postRepository.findAll()).isEmpty();
    }

    @DisplayName("게시글을 작성하면 DB에 저장된다.")
    @Test
    @WithMockUser(username = "wss3325")
    void writePost() throws Exception {
        //given
        String title = "제목";
        String content = "내용";

        //when
        mockMvc.perform(post("/post/create")
                        .with(csrf())
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .param("title", title)
                        .param("content", content)
                )
                .andExpect(status().is3xxRedirection())
                .andDo(print());

        //then
        PostSearch postSearch = PostSearch.builder()
                .keyword(title)
                .searchField("title")
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<MainPostDto> posts = postService.searchPosts(postSearch, pageable);

        assertThat(posts).isNotEmpty();
        assertThat(posts.getContent().get(0).getTitle()).isEqualTo(title);
    }

    @DisplayName("게시글에 이미지를 업로드 할 수 있다.")
    @Test
    @WithMockUser(username = "wss3325")
    void imageUpload() throws Exception {
        // given
        MockMultipartFile image = new MockMultipartFile(
                "images",
                "test.jpg",
                "image/jpeg",
                "이미지 데이터".getBytes()
        );

        // when & then
        mockMvc.perform(multipart("/post/create")
                        .file(image)
                        .with(csrf())
                        .param("title", "제목")
                        .param("content", "내용")
                )
                .andExpect(status().is3xxRedirection())
                .andDo(print());
    }
}