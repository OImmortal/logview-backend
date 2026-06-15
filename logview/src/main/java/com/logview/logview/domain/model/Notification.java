package com.logview.logview.domain.model;

import com.logview.logview.domain.enums.CanalNotificacao;
import com.logview.logview.domain.enums.StatusNotificacao;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "notifications")
@CompoundIndex(name = "idx_empresa_created", def = "{'tenantCnpj': 1, 'createdAt': -1}")
public class Notification {

    @Id
    private String id;

    @Indexed
    private String tenantCnpj;

    private String fileId;
    private String sistemaId;
    private CanalNotificacao canal;
    private StatusNotificacao status;
    private String titulo;
    private String mensagem;
    private int quantidadeErros;
    private int quantidadeAlertas;

    @Indexed
    private Instant createdAt;

    private Instant enviadaEm;

    public Notification() {
    }

    private Notification(Builder builder) {
        this.id = builder.id;
        this.tenantCnpj = builder.tenantCnpj;
        this.fileId = builder.fileId;
        this.sistemaId = builder.sistemaId;
        this.canal = builder.canal;
        this.status = builder.status;
        this.titulo = builder.titulo;
        this.mensagem = builder.mensagem;
        this.quantidadeErros = builder.quantidadeErros;
        this.quantidadeAlertas = builder.quantidadeAlertas;
        this.createdAt = builder.createdAt;
        this.enviadaEm = builder.enviadaEm;
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

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getSistemaId() {
        return sistemaId;
    }

    public void setSistemaId(String sistemaId) {
        this.sistemaId = sistemaId;
    }

    public CanalNotificacao getCanal() {
        return canal;
    }

    public void setCanal(CanalNotificacao canal) {
        this.canal = canal;
    }

    public StatusNotificacao getStatus() {
        return status;
    }

    public void setStatus(StatusNotificacao status) {
        this.status = status;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public int getQuantidadeErros() {
        return quantidadeErros;
    }

    public void setQuantidadeErros(int quantidadeErros) {
        this.quantidadeErros = quantidadeErros;
    }

    public int getQuantidadeAlertas() {
        return quantidadeAlertas;
    }

    public void setQuantidadeAlertas(int quantidadeAlertas) {
        this.quantidadeAlertas = quantidadeAlertas;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getEnviadaEm() {
        return enviadaEm;
    }

    public void setEnviadaEm(Instant enviadaEm) {
        this.enviadaEm = enviadaEm;
    }

    public static final class Builder {
        private String id;
        private String tenantCnpj;
        private String fileId;
        private String sistemaId;
        private CanalNotificacao canal;
        private StatusNotificacao status;
        private String titulo;
        private String mensagem;
        private int quantidadeErros;
        private int quantidadeAlertas;
        private Instant createdAt;
        private Instant enviadaEm;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder tenantCnpj(String tenantCnpj) {
            this.tenantCnpj = tenantCnpj;
            return this;
        }

        public Builder fileId(String fileId) {
            this.fileId = fileId;
            return this;
        }

        public Builder sistemaId(String sistemaId) {
            this.sistemaId = sistemaId;
            return this;
        }

        public Builder canal(CanalNotificacao canal) {
            this.canal = canal;
            return this;
        }

        public Builder status(StatusNotificacao status) {
            this.status = status;
            return this;
        }

        public Builder titulo(String titulo) {
            this.titulo = titulo;
            return this;
        }

        public Builder mensagem(String mensagem) {
            this.mensagem = mensagem;
            return this;
        }

        public Builder quantidadeErros(int quantidadeErros) {
            this.quantidadeErros = quantidadeErros;
            return this;
        }

        public Builder quantidadeAlertas(int quantidadeAlertas) {
            this.quantidadeAlertas = quantidadeAlertas;
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder enviadaEm(Instant enviadaEm) {
            this.enviadaEm = enviadaEm;
            return this;
        }

        public Notification build() {
            return new Notification(this);
        }
    }
}
