package hello.board.service.image;

import java.util.UUID;

public class FileUtils {

    public static String createFileName(String originalFileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
    }

    public static String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("잘못된 형식의 파일(" + fileName + ") 입니다.", e);
        }
    }
}