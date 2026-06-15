package com.logview.logview.domain.exception;

public class TenantNotFoundException extends RuntimeException {

    public TenantNotFoundException() {
        super("Tenant CNPJ header is required");
    }
}
