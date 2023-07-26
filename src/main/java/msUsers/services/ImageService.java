package msUsers.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.UUID;

@Service
@Slf4j
public class ImageService {
    @Value("${path.filesystem}")
    private String pathFilesystem;

    @Value("${default.extension}")
    private String defaultExtension;

    public String saveImage(String imgBase64){

        log.info(">> Se comienza el guardado de imagen");
        String extensionImg = "";
        String[] parts = imgBase64.split(",");
        int len = parts.length;
        if (len > 1) {
            imgBase64 = parts[1];
            extensionImg = parts[0].split("/|;")[1];
        }
        else{
            extensionImg = defaultExtension;
        }

        byte[] imgBytes = Base64.getDecoder().decode(imgBase64);
        String imageName = UUID.randomUUID().toString() + "." + extensionImg;
        String directorioActual = System.getProperty("user.dir");
        String storagePath = directorioActual + pathFilesystem + imageName;

        File directory = new File(directorioActual + pathFilesystem);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                log.info("Directorio de almacenamiento creado correctamente.");
            }
        }

        try (OutputStream outputStream = new FileOutputStream(storagePath)) {
            outputStream.write(imgBytes);
            log.info("<< Imagen {} guardada correctamente", imageName);
        } catch (IOException e) {
            // Manejar el error de escritura de la imagen
            log.error("<< Error al guardar la imagen: ", e.getCause());
        }

        return imageName;
    }

}
