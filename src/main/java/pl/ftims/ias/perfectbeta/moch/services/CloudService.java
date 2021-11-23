package pl.ftims.ias.perfectbeta.moch.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.ftims.ias.perfectbeta.exceptions.UploadFileException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;


@Service
public class CloudService implements CloudServiceLocal {

    private String cloudName;
    private String apiKey;
    private String apiSecret;

    @Value("${cloudinary.cloudName}")
    public void setCloudName(String secret) {
        this.cloudName = secret;
    }

    @Value("${cloudinary.apiKey}")
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Value("${cloudinary.apiSecret}")
    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }

    public String uploadFileToCloudinary(MultipartFile file) throws UploadFileException, IOException {
        File fileObj = convertMultiPartFileToFile(file);
        Map config = ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret);
        Cloudinary cloudinary = new Cloudinary(config);
        Map result = cloudinary.uploader().upload(convertMultiPartFileToFile(file), ObjectUtils.emptyMap());
        fileObj.delete();
        return result.get("secure_url").toString();
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
