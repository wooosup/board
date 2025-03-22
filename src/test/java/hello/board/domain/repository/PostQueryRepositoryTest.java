package hello.board.domain.repository;

import hello.board.domain.Role;
import hello.board.domain.entity.post.Post;
import hello.board.domain.entity.user.User;
import hello.board.service.post.dto.MainPostDto;
import hello.board.service.post.dto.PostSearch;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PostQueryRepositoryTest {

    @Autowired
    private PostQueryRepository postQueryRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void paging() throws Exception {
        //given
        User user = User.builder()
                .username("wss3325")
                .password("123456789")
                .nickname("sup")
                .grade(Role.USER)
                .build();
        userRepository.save(user);

        for (int i = 0; i < 20; i++) {
            Post post = Post.builder()
                    .user(user)
                    .title("제목" + i)
                    .content("내용" + i)
                    .build();
            postRepository.save(post);
        }

        //when
        PostSearch search = PostSearch.builder().build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<MainPostDto> result = postQueryRepository.searchPosts(search, pageable);

        //then
        assertThat(result.getTotalElements()).isEqualTo(21); // DB에 들어가있는 게시글 + 1
        assertThat(result.getTotalPages()).isEqualTo(3);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("제목19");
    }

    @Test
    void searchTitlePaging() throws Exception {
        //given
        User user = User.builder()
                .username("wss3325")
                .password("123456789")
                .nickname("sup")
                .grade(Role.USER)
                .build();
        userRepository.save(user);

        for (int i = 0; i < 20; i++) {
            Post post = Post.builder()
                    .user(user)
                    .title("제목" + i)
                    .content("내용" + i)
                    .build();
            postRepository.save(post);
        }

        //when
        PostSearch search = PostSearch.builder()
                .searchField("title")
                .keyword("5")
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<MainPostDto> result = postQueryRepository.searchPosts(search, pageable);

        //then
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("제목15");
    }

    @Test
    void searchContentPaging() throws Exception {
        //given
        User user = User.builder()
                .username("wss3325")
                .password("123456789")
                .nickname("sup")
                .grade(Role.USER)
                .build();
        userRepository.save(user);

        for (int i = 0; i < 20; i++) {
            Post post = Post.builder()
                    .user(user)
                    .title("제목" + i)
                    .content("내용" + i)
                    .build();
            postRepository.save(post);
        }

        //when
        PostSearch search = PostSearch.builder()
                .searchField("content")
                .keyword("5")
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<MainPostDto> result = postQueryRepository.searchPosts(search, pageable);

        //then
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.getContent().get(0).getContent()).isEqualTo("내용15");
    }

    @Test
    void searchNickNamePaging() throws Exception {
        //given
        User user = User.builder()
                .username("wss3325")
                .password("123456789")
                .nickname("sup")
                .grade(Role.USER)
                .build();
        userRepository.save(user);
        Post post = Post.builder()
                .user(user)
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(post);

        //when
        PostSearch search = PostSearch.builder()
                .searchField("nickname")
                .keyword("sup")
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<MainPostDto> result = postQueryRepository.searchPosts(search, pageable);

        //then
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("제목");
        assertThat(result.getContent().get(0).getContent()).isEqualTo("내용");
    }
}