package org.profwell.file.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.Play;

import com.google.common.hash.Hashing;

public class FileServiceImpl implements FileService {

    private static final int HASH_FOLDER_NAME_LENGTH = 3;
    /*
     * TODO : improve it and make async and garranted
     */
    private File fileStorage;

    public FileServiceImpl() {

        String fileStoragePath = Play.application().configuration()
                .getString("application.file.storage");

        if (StringUtils.isBlank(fileStoragePath)) {
            throw new IllegalStateException("Illegal filestorage path.");
        }

        this.fileStorage = new File(fileStoragePath);

        try {
            FileUtils.forceMkdir(this.fileStorage);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot create File Storage.", e);
        }
    }

    @Override
    public InputStream retriveFile(String mimeType, String hash) {

        if (StringUtils.isBlank(mimeType)) {
            throw new IllegalArgumentException("MimeType is wrong");
        }

        if (StringUtils.isBlank(hash) && hash.length() < HASH_FOLDER_NAME_LENGTH) {
            throw new IllegalArgumentException("Hash is wrong");
        }

        String mimeFolderName = clearMimeType(mimeType);
        File mimeFolder = new File(this.fileStorage, mimeFolderName);
        if (!mimeFolder.exists() || !mimeFolder.isDirectory()) {
            Logger.error("Mime folder [" + mimeFolderName + "] is not found.");
            return null;
        }

        String hashFolderName = hash.substring(0, HASH_FOLDER_NAME_LENGTH);
        File hashFolder = new File(mimeFolder, hashFolderName);
        if (!hashFolder.exists() || !hashFolder.isDirectory()) {
            Logger.error("Hash folder [" + hashFolderName + "] inside [" + mimeFolderName + "] is not found.");
            return null;
        }

        File file = new File(hashFolder, hash);
        if (!file.exists() || !file.isFile()) {
            Logger.error("File [" + hash + "] inside mime [" + mimeFolderName + "] is not found.");
            return null;
        }

        ByteArrayOutputStream out = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            out = new ByteArrayOutputStream();
            IOUtils.copy(fis, out);
        } catch (Exception e) {
            Logger.error("Error on reading file [" + hash + "] inside [" + mimeFolderName + "].");
            return null;
        } finally {
            IOUtils.closeQuietly(fis);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    @Override
    public synchronized void storeFile(String mimeType, InputStream inputStream,
            FileStorageCallback callback) {

        if (StringUtils.isBlank(mimeType)) {
            callback.onError("MimeType is wrong");
            return;
        }

        if (inputStream == null) {
            callback.onError("Data is null");
            return;
        }

        String mimeFolderName = clearMimeType(mimeType);
        File mimeFolder = new File(this.fileStorage, mimeFolderName);
        if (!mimeFolder.exists()) {
            try {
                FileUtils.forceMkdir(mimeFolder);
            } catch (IOException e) {
                callback.onError("Error on creation mime folder [" + mimeFolderName + "].");
                return;
            }
        } else if (!mimeFolder.isDirectory()) {
            callback.onError("File with name [" + mimeFolderName + "] blocks to create folder.");
            return;
        }

        byte[] bytes = null;
        try {
            bytes = IOUtils.toByteArray(inputStream);
        } catch (IOException e1) {
            callback.onError("Error on reading data.");
            return;
        }

        String hash = Hashing.sha256().hashBytes(bytes).toString();

        String hashFolderName = hash.substring(0, HASH_FOLDER_NAME_LENGTH);
        File hashFolder = new File(mimeFolder, hashFolderName);
        if (!hashFolder.exists()) {
            try {
                FileUtils.forceMkdir(hashFolder);
            } catch (IOException e) {
                callback.onError("Error on creation hash folder [" + hashFolderName + "] inside [" + mimeFolderName + "].");
                return;
            }
        } else if (!hashFolder.isDirectory()) {
            callback.onError("File with name [" + hashFolderName + "] blocks to create folder.");
            return;
        }

        File file = new File(hashFolder, hash);
        if (!file.exists()) {
            try {
                FileUtils.copyInputStreamToFile(new ByteArrayInputStream(bytes), file);
                callback.onSuccess(hash);
                return;
            } catch (IOException e) {
                callback.onError("Error on writing data to file [" + hash + "] inside [" + mimeFolderName + "].");
                return;
            }
        } else if (file.isFile()) {
            callback.onSuccess(hash);
            return;
        } else {
            callback.onError("Directory with name [" + hash + "] blocks to create file.");
            return;
        }

    }

    String clearMimeType(String mimeType) {
        String clearedMimeType = mimeType
                .replaceAll("\\-", "")
                .replaceAll("\\/", "")
                .replaceAll("\\+", "")
                .replaceAll("\\.", "");

        if (clearedMimeType.length() > 32) {
            return clearedMimeType.toLowerCase().substring(0, 32);
        } else {
            return clearedMimeType.toLowerCase();
        }
    }
}
