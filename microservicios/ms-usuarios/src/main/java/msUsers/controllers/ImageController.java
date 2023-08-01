package msUsers.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
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
    @GetMapping(path = "/getImage/{img}")
    public ResponseEntity<Resource> getImage(@PathVariable("img") String img) {
        String dir = System.getProperty("user.dir") + pathFilesystem;
        Resource imagenResource = new FileSystemResource(dir + img);

        if (imagenResource.exists()) {
            String extension = obtenerExtension(img);
            MediaType mediaType = MediaType.parseMediaType(MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE);

            // Establecer el tipo de contenido según la extensión de archivo
            if (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg")) {
                mediaType = MediaType.IMAGE_JPEG;
            } else if (extension.equalsIgnoreCase("png")) {
                mediaType = MediaType.IMAGE_PNG;
            } else if (extension.equalsIgnoreCase("bmp")) {
                mediaType = MediaType.parseMediaType("image/bmp");
            };
            //log.info(">> Se retorna la imagen: {}", img); -- Descomentar para debug
            return ResponseEntity.ok().contentType(mediaType).body(imagenResource);

        } else {
            // En caso de que la imagen no exista
            log.warn(">> No se encontro la imagen: {}", img);
            return ResponseEntity.notFound().build();
        }
    }

    private String obtenerExtension(String nombreArchivo) {
        int indicePunto = nombreArchivo.lastIndexOf(".");
        if (indicePunto != -1) {
            return nombreArchivo.substring(indicePunto + 1);
        }
        return "";
    }
}
