package com.logview.logview.web.controller;

import com.logview.logview.application.dto.LogFileSummaryResponse;
import com.logview.logview.application.dto.ProcessLogFileResponse;
import com.logview.logview.application.dto.UploadLogFileResponse;
import com.logview.logview.application.usecase.ListLogFilesUseCase;
import com.logview.logview.application.usecase.ProcessLogFileUseCase;
import com.logview.logview.application.usecase.UploadLogFileUseCase;
import com.logview.logview.web.resolver.TenantContextResolver;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/log-files")
public class LogFileController {

    private final UploadLogFileUseCase uploadLogFileUseCase;
    private final ProcessLogFileUseCase processLogFileUseCase;
    private final ListLogFilesUseCase listLogFilesUseCase;
    private final TenantContextResolver tenantContextResolver;

    public LogFileController(UploadLogFileUseCase uploadLogFileUseCase,
                               ProcessLogFileUseCase processLogFileUseCase,
                               ListLogFilesUseCase listLogFilesUseCase,
                               TenantContextResolver tenantContextResolver) {
        this.uploadLogFileUseCase = uploadLogFileUseCase;
        this.processLogFileUseCase = processLogFileUseCase;
        this.listLogFilesUseCase = listLogFilesUseCase;
        this.tenantContextResolver = tenantContextResolver;
    }

    @GetMapping
    public List<LogFileSummaryResponse> list(HttpServletRequest request) {
        String tenantCnpj = tenantContextResolver.resolve(request);
        return listLogFilesUseCase.execute(tenantCnpj);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public UploadLogFileResponse upload(
            @RequestPart("file") MultipartFile file,
            HttpServletRequest request) throws IOException {
        String tenantCnpj = tenantContextResolver.resolve(request);
        return uploadLogFileUseCase.execute(tenantCnpj, file);
    }

    @PostMapping("/{id}/process")
    public ProcessLogFileResponse process(
            @PathVariable String id,
            HttpServletRequest request) throws IOException {
        String tenantCnpj = tenantContextResolver.resolve(request);
        return processLogFileUseCase.execute(tenantCnpj, id);
    }
}
