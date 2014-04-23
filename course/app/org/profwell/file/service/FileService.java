package org.profwell.file.service;

import java.io.InputStream;

public interface FileService {

    String FILE_HASH_PLACEHOLDER = "DEAFBEEFDEAFBEEFDEAFBEEFDEAFBEEFDEAFBEEFDEAFBEEFDEAFBEEFDEAFBEEF";

    Long FILE_LENGTH_LIMIT = 200l*1024;

    InputStream retriveFile(String mimeType, String hash);

    void storeFile(String mimeType, InputStream inputStream, FileStorageCallback callback);

}
