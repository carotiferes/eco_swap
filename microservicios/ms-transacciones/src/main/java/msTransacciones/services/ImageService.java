package msTransacciones.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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

    private List<String> imagesToSave = new ArrayList<>();

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
    public String saveImage(String imgBase64){

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

        // Solo se ejecuta cuando commitee correctamente.
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                File directory = new File(directorioActual + pathFilesystem);
                if (!directory.exists()) {
                    if (directory.mkdirs()) {
                        log.info(">> Directorio de almacenamiento creado correctamente.");
                    }
                }

                try (OutputStream outputStream = new FileOutputStream(storagePath)) {
                    outputStream.write(imgBytes);
                    log.info("<< Imagen {} guardada correctamente", imageName);
                } catch (IOException e) {
                    log.error("<< Error al guardar la imagen: {}", e.getMessage());
                }
            }
        });

        return imageName;
    }

}
