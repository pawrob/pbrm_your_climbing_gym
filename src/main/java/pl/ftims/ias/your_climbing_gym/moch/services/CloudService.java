package pl.ftims.ias.your_climbing_gym.moch.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.ftims.ias.your_climbing_gym.exceptions.UploadFileException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


@Service
public class CloudService {

    @Value("${cloud.aws.bucket.name}")
    private String bucketName;
    private final AmazonS3 client;


    @Autowired
    public CloudService(AmazonS3 client) {
        this.client = client;
    }

    public String uploadFile(MultipartFile file) throws UploadFileException {
        File fileObj = convertMultiPartFileToFile(file);
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        client.putObject(new PutObjectRequest(bucketName, "photos/" + fileName, fileObj));
        fileObj.delete();
        return client.getUrl(bucketName, "photos/" + fileName).toString();
    }


    private File convertMultiPartFileToFile(MultipartFile file) throws UploadFileException {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            throw UploadFileException.createUploadFileException(file.toString());
        }
        return convertedFile;
    }
}
