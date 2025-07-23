package hello.board.service.image;

import hello.board.global.exception.FileStorageException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileStore fileStore;

    public String uploadFile(String originalFileName, byte[] fileData) throws IOException {
        MultipartFile multipartFile = new InMemoryMultipartFile(originalFileName, fileData);
        try {
            return fileStore.uploadFile(multipartFile);
        } catch (IOException e) {
            throw new FileStorageException("파일 업로드에 실패했습니다.", e);
        }
    }

    public void deleteFile(String fileName) {
        try {
            fileStore.deleteFile(fileName);
        } catch (IOException e) {
            throw new FileStorageException("파일 삭제에 실패했습니다.", e);
        }
    }

    public String generateFileUrl(String fileName) {
        return fileStore.generateFileUrl(fileName);
    }
}