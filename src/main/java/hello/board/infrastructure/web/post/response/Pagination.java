package hello.board.infrastructure.web.post.response;

import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class Pagination {

    private final int startPage;
    private final int endPage;
    private final int currentPage;
    private final int totalPages;
    private static final int BLOCK_SIZE = 5;

    public Pagination(Page<?> page) {
        this.currentPage = page.getNumber();
        this.totalPages = (page.getTotalPages() == 0) ? 1 : page.getTotalPages();

        int currentBlock = currentPage / BLOCK_SIZE;
        this.startPage = currentBlock * BLOCK_SIZE;

        int calculatedEndPage = startPage + BLOCK_SIZE - 1;
        this.endPage = Math.min(calculatedEndPage, totalPages - 1);
    }

}
