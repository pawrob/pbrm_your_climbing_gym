package pl.ftims.ias.your_climbing_gym.moch.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    public String uploadFile(MultipartFile file) {
        File fileObj = convertMultiPartFileToFile(file);
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        client.putObject(new PutObjectRequest(bucketName, "photos/" + fileName, fileObj));
        fileObj.delete();
        return client.getUrl(bucketName, "photos/"+ fileName).toString();
    }

    public byte[] downloadFile(String fileName) {
        //todo own exception
        S3Object s3Object = client.getObject(bucketName, fileName);

        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        } catch (IOException e) {

            e.printStackTrace();
        }
        return null;
    }

    public String deleteFile(String fileName) {
        client.deleteObject(bucketName, fileName);
        //todo own exception
        return fileName + " removed ...";
    }


    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            //todo own exception
            e.printStackTrace();
        }
        return convertedFile;
    }
}
