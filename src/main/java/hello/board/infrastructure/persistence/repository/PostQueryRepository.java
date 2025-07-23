package hello.board.infrastructure.persistence.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.board.domain.post.QPost;
import hello.board.domain.user.QUser;
import hello.board.infrastructure.web.post.response.MainPostDto;
import hello.board.infrastructure.web.post.response.PostSearch;
import hello.board.infrastructure.web.post.response.QMainPostDto;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public PostQueryRepository(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    public Page<MainPostDto> searchPosts(PostSearch search, Pageable pageable) {
        BooleanBuilder builder = createPredicate(search);

        List<MainPostDto> content = fetchContent(pageable, builder);
        long total = fetchTotalCount(builder);

        final long MAX_ITEMS = 1000;
        if (total > MAX_ITEMS) {
            total = MAX_ITEMS;
        }

        return new PageImpl<>(content, pageable, total);
    }

    private List<MainPostDto> fetchContent(Pageable pageable, BooleanBuilder predicate) {
        QPost post = QPost.post;
        QUser user = QUser.user;

        return jpaQueryFactory
                .select(new QMainPostDto(
                        post.id,
                        user.username,
                        user.nickname,
                        post.title,
                        post.content,
                        post.createDateTime
                ))
                .from(post)
                .leftJoin(post.user, user)
                .where(predicate)
                .orderBy(post.createDateTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }


    private long fetchTotalCount(BooleanBuilder builder) {
        QPost post = QPost.post;

        Long total = jpaQueryFactory
                .select(post.id.count())
                .from(post)
                .where(builder)
                .limit(1000)
                .fetchOne();

        return total != null ? total : 0;
    }


    private BooleanBuilder createPredicate(PostSearch search) {
        QPost post = QPost.post;
        BooleanBuilder builder = new BooleanBuilder();

        if ("title".equals(search.getSearchField())) {
            builder.and(post.title.contains(search.getKeyword()));
        } else if ("content".equals(search.getSearchField())) {
            builder.and(post.content.contains(search.getKeyword()));
        } else if ("nickname".equals(search.getSearchField())) {
            builder.and(post.user.nickname.contains(search.getKeyword()));
        }

        return builder;
    }
}
