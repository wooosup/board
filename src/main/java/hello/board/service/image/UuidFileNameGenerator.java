package hello.board.service.image;

import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class UuidFileNameGenerator implements FileNameGenerator {

    @Override
    public String generate(String originalFileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
    }

    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("잘못된 형식의 파일(" + fileName + ") 입니다.", e);
        }
    }
}
