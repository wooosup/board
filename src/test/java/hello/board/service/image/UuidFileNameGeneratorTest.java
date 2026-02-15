package hello.board.service.image;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class UuidFileNameGeneratorTest {

    private final UuidFileNameGenerator fileNameGenerator = new UuidFileNameGenerator();

    @Test
    void generateKeepsOriginalExtension() {
        String generatedFileName = fileNameGenerator.generate("board-image.png");

        assertTrue(generatedFileName.endsWith(".png"));
    }

    @Test
    void generateThrowsExceptionWhenFileHasNoExtension() {
        assertThrows(IllegalArgumentException.class,
                () -> fileNameGenerator.generate("board-image"));
    }
}
