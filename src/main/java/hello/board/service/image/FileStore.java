package hello.board.service.image;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStore {

    String uploadFile(MultipartFile file) throws IOException;

    void deleteFile(String fileName) throws IOException;

    String generateFileUrl(String fileName);
}
