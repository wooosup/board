package hello.board.service.image.prod;

import hello.board.global.exception.FileStorageException;
import hello.board.service.image.FileNameGenerator;
import hello.board.service.image.FileStore;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.InputStream;
import java.net.URL;

@Service
@RequiredArgsConstructor
@Profile("prod")
public class S3Service implements FileStore {

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    private final S3Client s3Client;
    private final FileNameGenerator fileNameGenerator;

    @Override
    public String uploadFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String fileName = fileNameGenerator.generate(originalFilename);

        try (InputStream inputStream = file.getInputStream()) {
            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(fileName)
                            .acl(ObjectCannedACL.PUBLIC_READ)
                            .contentType(file.getContentType())
                            .build(),
                    RequestBody.fromInputStream(inputStream, file.getSize()));
        } catch (Throwable e) {
            System.err.println("실패 원인: " + e.getClass().getName());

            throw new FileStorageException("S3에 파일 업로드가 실패했습니다.", e);
        }
        return fileName;
    }

    @Override
    public void deleteFile(String fileName) {
        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build());
        } catch (S3Exception e) {
            throw new FileStorageException("S3에서 파일 삭제가 실패했습니다.", e);
        }
    }

    @Override
    public String generateFileUrl(String fileName) {
        try {
            URL url = s3Client.utilities().getUrl(b -> b.bucket(bucketName).key(fileName));
            return url.toString();
        } catch (S3Exception e) {
            throw new FileStorageException("S3에서 파일 URL 생성이 실패했습니다.", e);
        }
    }
}
