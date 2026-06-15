package com.logview.logview.domain.model;

import com.logview.logview.domain.enums.Severity;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "log_occurrences")
@CompoundIndex(name = "idx_file_line", def = "{'fileId': 1, 'lineNumber': 1}")
@CompoundIndex(name = "idx_tenant_severity", def = "{'tenantCnpj': 1, 'severity': 1, 'detectedAt': -1}")
public class LogOccurrence {

    @Id
    private String id;

    @Indexed
    private String fileId;

    @Indexed
    private String tenantCnpj;

    private int lineNumber;
    private String message;

    @Indexed
    private Severity severity;

    @Indexed
    private Instant detectedAt;

    public LogOccurrence() {
    }

    private LogOccurrence(Builder builder) {
        this.id = builder.id;
        this.fileId = builder.fileId;
        this.tenantCnpj = builder.tenantCnpj;
        this.lineNumber = builder.lineNumber;
        this.message = builder.message;
        this.severity = builder.severity;
        this.detectedAt = builder.detectedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getTenantCnpj() {
        return tenantCnpj;
    }

    public void setTenantCnpj(String tenantCnpj) {
        this.tenantCnpj = tenantCnpj;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public Instant getDetectedAt() {
        return detectedAt;
    }

    public void setDetectedAt(Instant detectedAt) {
        this.detectedAt = detectedAt;
    }

    public static final class Builder {
        private String id;
        private String fileId;
        private String tenantCnpj;
        private int lineNumber;
        private String message;
        private Severity severity;
        private Instant detectedAt;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder fileId(String fileId) {
            this.fileId = fileId;
            return this;
        }

        public Builder tenantCnpj(String tenantCnpj) {
            this.tenantCnpj = tenantCnpj;
            return this;
        }

        public Builder lineNumber(int lineNumber) {
            this.lineNumber = lineNumber;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder severity(Severity severity) {
            this.severity = severity;
            return this;
        }

        public Builder detectedAt(Instant detectedAt) {
            this.detectedAt = detectedAt;
            return this;
        }

        public LogOccurrence build() {
            return new LogOccurrence(this);
        }
    }
}
