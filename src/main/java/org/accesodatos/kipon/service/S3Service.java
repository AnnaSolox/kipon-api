package org.accesodatos.kipon.service;

import java.io.InputStream;

public interface S3Service {
    void uploadFile(String key, InputStream inputStream, long contentLength);
    InputStream downloadFile(String key);
    String getUrlForKey(String key);
    void deleteFile(String key);
}
