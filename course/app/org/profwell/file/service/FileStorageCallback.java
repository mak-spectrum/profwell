package org.profwell.file.service;

public interface FileStorageCallback {

    void onSuccess(String hash);

    void onError(String message);

}
