package com.logview.logview.domain.model;

import com.logview.logview.domain.enums.LogFileStatus;
import com.logview.logview.domain.enums.StatusProcessamento;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "log_files")
@CompoundIndex(name = "idx_tenant_created", def = "{'tenantCnpj': 1, 'createdAt': -1}")
public class LogFile {

    @Id
    private String id;

    @Indexed
    private String tenantCnpj;

    private String empresaId;
    private String sistemaId;
    private String originalFileName;
    private String storagePath;
    private long fileSizeBytes;
    private LogFileStatus status;
    private StatusProcessamento statusProcessamento;
    private int totalOccurrences;
    private int criticalCount;
    private int warningCount;

    @Indexed
    private Instant createdAt;

    private Instant processedAt;
    private String mensagemFalha;

    public LogFile() {
    }

    private LogFile(Builder builder) {
        this.id = builder.id;
        this.tenantCnpj = builder.tenantCnpj;
        this.empresaId = builder.empresaId;
        this.sistemaId = builder.sistemaId;
        this.originalFileName = builder.originalFileName;
        this.storagePath = builder.storagePath;
        this.fileSizeBytes = builder.fileSizeBytes;
        this.status = builder.status;
        this.statusProcessamento = builder.statusProcessamento;
        this.totalOccurrences = builder.totalOccurrences;
        this.criticalCount = builder.criticalCount;
        this.warningCount = builder.warningCount;
        this.createdAt = builder.createdAt;
        this.processedAt = builder.processedAt;
        this.mensagemFalha = builder.mensagemFalha;
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

    public String getTenantCnpj() {
        return tenantCnpj;
    }

    public void setTenantCnpj(String tenantCnpj) {
        this.tenantCnpj = tenantCnpj;
    }

    public String getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(String empresaId) {
        this.empresaId = empresaId;
    }

    public String getSistemaId() {
        return sistemaId;
    }

    public void setSistemaId(String sistemaId) {
        this.sistemaId = sistemaId;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    public long getFileSizeBytes() {
        return fileSizeBytes;
    }

    public void setFileSizeBytes(long fileSizeBytes) {
        this.fileSizeBytes = fileSizeBytes;
    }

    public LogFileStatus getStatus() {
        return status;
    }

    public void setStatus(LogFileStatus status) {
        this.status = status;
    }

    public StatusProcessamento getStatusProcessamento() {
        return statusProcessamento;
    }

    public void setStatusProcessamento(StatusProcessamento statusProcessamento) {
        this.statusProcessamento = statusProcessamento;
    }

    public int getTotalOccurrences() {
        return totalOccurrences;
    }

    public void setTotalOccurrences(int totalOccurrences) {
        this.totalOccurrences = totalOccurrences;
    }

    public int getCriticalCount() {
        return criticalCount;
    }

    public void setCriticalCount(int criticalCount) {
        this.criticalCount = criticalCount;
    }

    public int getWarningCount() {
        return warningCount;
    }

    public void setWarningCount(int warningCount) {
        this.warningCount = warningCount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(Instant processedAt) {
        this.processedAt = processedAt;
    }

    public String getMensagemFalha() {
        return mensagemFalha;
    }

    public void setMensagemFalha(String mensagemFalha) {
        this.mensagemFalha = mensagemFalha;
    }

    public static final class Builder {
        private String id;
        private String tenantCnpj;
        private String empresaId;
        private String sistemaId;
        private String originalFileName;
        private String storagePath;
        private long fileSizeBytes;
        private LogFileStatus status;
        private StatusProcessamento statusProcessamento;
        private int totalOccurrences;
        private int criticalCount;
        private int warningCount;
        private Instant createdAt;
        private Instant processedAt;
        private String mensagemFalha;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder tenantCnpj(String tenantCnpj) {
            this.tenantCnpj = tenantCnpj;
            return this;
        }

        public Builder empresaId(String empresaId) {
            this.empresaId = empresaId;
            return this;
        }

        public Builder sistemaId(String sistemaId) {
            this.sistemaId = sistemaId;
            return this;
        }

        public Builder originalFileName(String originalFileName) {
            this.originalFileName = originalFileName;
            return this;
        }

        public Builder storagePath(String storagePath) {
            this.storagePath = storagePath;
            return this;
        }

        public Builder fileSizeBytes(long fileSizeBytes) {
            this.fileSizeBytes = fileSizeBytes;
            return this;
        }

        public Builder status(LogFileStatus status) {
            this.status = status;
            return this;
        }

        public Builder statusProcessamento(StatusProcessamento statusProcessamento) {
            this.statusProcessamento = statusProcessamento;
            return this;
        }

        public Builder totalOccurrences(int totalOccurrences) {
            this.totalOccurrences = totalOccurrences;
            return this;
        }

        public Builder criticalCount(int criticalCount) {
            this.criticalCount = criticalCount;
            return this;
        }

        public Builder warningCount(int warningCount) {
            this.warningCount = warningCount;
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder processedAt(Instant processedAt) {
            this.processedAt = processedAt;
            return this;
        }

        public Builder mensagemFalha(String mensagemFalha) {
            this.mensagemFalha = mensagemFalha;
            return this;
        }

        public LogFile build() {
            return new LogFile(this);
        }
    }
}
