package msTransacciones.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class ImageService {
    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Value("${aws.s3.folderNameImages}")
    private String folderNameImages;

    @Autowired
    private AmazonS3 amazonS3Client;

    public String getImage(String imgUUID){
        GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucketName, folderNameImages + imgUUID);
        urlRequest.addRequestParameter("response-content-disposition", "inline");
        return amazonS3Client.generatePresignedUrl(urlRequest).toString();
    }
}
