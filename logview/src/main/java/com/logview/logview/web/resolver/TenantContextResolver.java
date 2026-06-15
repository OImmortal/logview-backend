package com.logview.logview.web.resolver;

import com.logview.logview.domain.exception.TenantNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class TenantContextResolver {

    public static final String TENANT_HEADER = "X-Tenant-Cnpj";

    public String resolve(HttpServletRequest request) {
        String tenantCnpj = request.getHeader(TENANT_HEADER);
        if (!StringUtils.hasText(tenantCnpj)) {
            throw new TenantNotFoundException();
        }
        return tenantCnpj.trim();
    }
}
