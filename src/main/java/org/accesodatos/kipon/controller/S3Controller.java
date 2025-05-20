package org.accesodatos.kipon.controller;

import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.accesodatos.kipon.service.S3Service;
import org.accesodatos.kipon.service.impl.S3ServiceImpl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/kipon/images")
public class S3Controller {

    private final S3Client s3Client;
    private final S3Service s3Service;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // Validar que sea JPG
            if (!"image/jpeg".equalsIgnoreCase(file.getContentType())) {
                return ResponseEntity.badRequest().body("Solo se permiten imágenes JPG");
            }

            // Redimensionar imagen con Thumbnailator
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (InputStream is = file.getInputStream()) {
                long MAX_HEIGHT = 800;
                long MAX_WIDTH = 800;
                Thumbnails.of(is)
                        .size((int) MAX_WIDTH, (int) MAX_HEIGHT)
                        .outputFormat("jpg")
                        .toOutputStream(baos);
            }

            byte[] resizedImageBytes = baos.toByteArray();

            String key = "profiles/" + System.currentTimeMillis() + "_" + file.getOriginalFilename();

            // Subir imagen redimensionada a S3
            try (InputStream isResized = new ByteArrayInputStream(resizedImageBytes)) {
                s3Service.uploadFile(key, isResized, resizedImageBytes.length);
            }

            // Generar URL para guardar en BBDD o devolver
            String url = s3Service.getUrlForKey(key);

            return ResponseEntity.ok(url);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al subir imagen");
        }
    }
}
