package com.logview.logview.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "logview")
public class StorageProperties {

    private Upload upload = new Upload();
    private Storage storage = new Storage();

    public Upload getUpload() {
        return upload;
    }

    public void setUpload(Upload upload) {
        this.upload = upload;
    }

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public static class Upload {
        private String allowedExtension = ".log";
        private long maxSize = 10_485_760L;

        public String getAllowedExtension() {
            return allowedExtension;
        }

        public void setAllowedExtension(String allowedExtension) {
            this.allowedExtension = allowedExtension;
        }

        public long getMaxSize() {
            return maxSize;
        }

        public void setMaxSize(long maxSize) {
            this.maxSize = maxSize;
        }
    }

    public static class Storage {
        private String tempDir = System.getProperty("java.io.tmpdir") + "/logview/uploads";

        public String getTempDir() {
            return tempDir;
        }

        public void setTempDir(String tempDir) {
            this.tempDir = tempDir;
        }
    }
}
