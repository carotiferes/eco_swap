package msUsers.controllers;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api")
public class ImageController {
    @Value("${path.filesystem}")
    private String pathFilesystem;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Value("${aws.s3.folderNameImages}")
    private String folderNameImages;

    @Autowired
    private AmazonS3 amazonS3Client;

    @GetMapping(path = "/getImage/{img}")
    public ResponseEntity<Resource> getImage(@PathVariable("img") String img) {
        log.info(">> Se busca en S3 la imagen: {}", img);
        try {
            S3Object s3Object = amazonS3Client.getObject(bucketName, folderNameImages + img);

            // Verificar que el objeto exista en S3
            if (s3Object != null) {
                MediaType mediaType = MediaType.parseMediaType(s3Object.getObjectMetadata().getContentType());
                InputStreamResource inputStreamResource = new InputStreamResource(s3Object.getObjectContent());

                // Log para fines de depuración
                log.info(">> Se retorna la imagen: {}", img);

                return ResponseEntity.ok()
                        .contentType(mediaType)
                        .contentLength(s3Object.getObjectMetadata().getContentLength())
                        .body(inputStreamResource);
            } else {
                // En caso de que la imagen no exista en S3
                log.warn("<< No se encontró la imagen en S3: {}", img);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            // En caso de error al obtener la imagen desde S3
            log.error("<< Error al obtener la imagen desde S3: {}", e.getLocalizedMessage());
            return ResponseEntity.status(500).build();
        }
    }
}
