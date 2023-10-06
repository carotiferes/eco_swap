package msUsers.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ImageService {
    @Value("${path.filesystem}")
    private String pathFilesystem;

    @Value("${default.extension}")
    private String defaultExtension;

    @Value("${aws.s3.bucketName}")
    private String bucketName;
    @Value("${aws.s3.folderNameImages}")
    private String folderNameImages;


    @Autowired
    private AmazonS3 amazonS3Client;

    private final List<String> imagesToSave = new ArrayList<>();

    @Transactional
    public void queueImageForSaving(String imgBase64) {
        log.info(">> Se encola la imagen para guardar.");
        imagesToSave.add(imgBase64);
    }

    public void saveQueuedImages() {
        log.info(">> Se inicia el guardado de imágenes en cola.");
        for (String imgBase64 : imagesToSave) {
            saveImage(imgBase64);
        }
        imagesToSave.clear();
    }
    @Transactional
    public String saveImage(String imgBase64) {

        String extensionImg = "";
        String[] parts = imgBase64.split(",");
        int len = parts.length;
        if (len > 1) {
            imgBase64 = parts[1];
            extensionImg = parts[0].split("/|;")[1];
        } else {
            extensionImg = defaultExtension;
        }

        byte[] imgBytes = Base64.getDecoder().decode(imgBase64);
        String imageName = UUID.randomUUID().toString() + "." + extensionImg;

        try
        {
            InputStream inputStream = new ByteArrayInputStream(imgBytes);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(imgBytes.length);
            metadata.setContentType("image/jpeg"); // Cambia esto según el tipo de imagen

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, folderNameImages + imageName, inputStream, metadata);

            // Solo se ejecuta cuando se confirma correctamente la transacción.
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    amazonS3Client.putObject(putObjectRequest);
                    log.info("<< Imagen {} guardada correctamente en Amazon S3", imageName);
                }
            });

            return imageName;
        }
        catch (Exception e){
            log.error("<< Error al guardar la imagen en Amazon S3: {}", e.getMessage());
            return null;
        }
    }
}
