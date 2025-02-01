package hello.board.controller;

import hello.board.service.post.dto.MainPostDto;
import hello.board.service.post.dto.PostSearch;
import hello.board.service.comment.CommentService;
import hello.board.service.post.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @MockBean
    private CommentService commentService;

    @Test
    void postListPaging() throws Exception {
        //given
        Pageable pageable = PageRequest.of(2, 10);
        List<MainPostDto> content = new ArrayList<>();

        Page<MainPostDto> page = new PageImpl<>(content, pageable, 200);

        when(postService.searchPosts(any(PostSearch.class), any(Pageable.class)))
                .thenReturn(page);

        //when & then
        mockMvc.perform(get("/")
                        .param("page", "2")
                        .param("keyword", "제목")
                        .param("searchField", "title")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("posts/postList"))
                .andExpect(model().attributeExists("posts"))
                .andExpect(model().attribute("currentPage", 2))
                .andExpect(model().attribute("totalPages", 20))
                .andExpect(model().attribute("startPage", 0))
                .andExpect(model().attribute("endPage", 4))
                .andExpect(model().attribute("search",
                        allOf(
                                hasProperty("keyword", is("제목")),
                                hasProperty("searchField", is("title"))
                        )
                ))
                .andDo(print());
    }
}