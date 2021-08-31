package com.logigear.crm.employees.controller;

import com.logigear.crm.employees.security.JwtProvider;
import com.logigear.crm.employees.service.FileService;
import com.logigear.crm.employees.response.*;
import com.logigear.crm.employees.payload.UpdateStatus;
import com.logigear.crm.employees.model.File;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
// import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static reactor.core.publisher.Mono.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class FileControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    FileService fileService;

    @MockBean
    JwtProvider tokenProvider;

    @Test
    public void uploadFile_Success() throws Exception {
        Resource resource = new ClassPathResource("test.docx");
        MockMultipartFile file = new MockMultipartFile("file", resource.getFilename(),
                MediaType.MULTIPART_FORM_DATA_VALUE, resource.getInputStream());

        MockMultipartHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .multipart("/api/employees/upload-file");

        mockMvc.perform(requestBuilder.file(file).param("id", "13")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "nimda", roles = { "USER" })
    public void getFileDetails() throws Exception {
        File file = new File();
        file.setId(1L);
        file.setFileName("fileName.docx");
        file.setFileType("application/json");
        file.setStatus("To be reviewed");
        file.setNote("need to be updated");
        given(fileService.getFileDetail((long) 1)).willReturn(
                new FileResponse(file.getId(), file.getFileName(), file.getStatus(), Instant.now(), file.getNote()));
        mockMvc.perform(
                get("/api/employees/file/1").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(username = "nimda", roles = { "USER" })
    public void updateStatus() throws Exception {
        File file = new File();
        UpdateStatus status = new UpdateStatus();
        status.setStatus("To be reviewed");
        file.setId(1L);
        file.setStatus("To be reviewed");
        file.setFileName("fileName.doc");
        file.setUploadAt(Instant.now());
        file.setNote("need to be updated");
        given(fileService.updateStatus((long) 1, status)).willReturn(new FileResponse(file.getId(), file.getFileName(),
                file.getStatus(), file.getUploadAt(), file.getNote()));
        mockMvc.perform(patch("/api/employees/file/1").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    // @Test
    // public void uploadFile_thenTooManyRequests() throws Exception {
    // Bandwidth limit = Bandwidth.classic(1, Refill.greedy(10,
    // Duration.ofSeconds(60)));
    // Bucket bucket = Bucket4j.builder().addLimit(limit).build();
    //
    // Resource resource = new ClassPathResource("test.docx");
    // MockMultipartFile file = new MockMultipartFile(
    // "file", resource.getFilename(), MediaType.MULTIPART_FORM_DATA_VALUE,
    // resource.getInputStream()
    // );
    //
    // MockMultipartHttpServletRequestBuilder requestBuilder =
    // MockMvcRequestBuilders.multipart("/api/employees/upload-file");
    //
    // for(int i = 1; i <= 100; i++) {
    // mockMvc.perform(requestBuilder.file(file)
    // .param("id", "13"))
    // .andExpect(status().isTooManyRequests()).andReturn();
    // }
    // }

    @Test
    public void downloadFile_Success() throws Exception {
        Resource resource = mockResource();
        Mockito.when(fileService.loadFileAsResource(anyString())).thenReturn(resource);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/download-file/test.doc"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\""))
                .andExpect(content().bytes("LogiGear".getBytes()));

        BDDMockito.then(fileService).should().loadFileAsResource("test.doc");
    }

    private Resource mockResource() {
        Resource resource = new Resource() {
            @Override
            public boolean exists() {
                return false;
            }

            @Override
            public URL getURL() throws IOException {
                return null;
            }

            @Override
            public URI getURI() throws IOException {
                return null;
            }

            @Override
            public java.io.File getFile() throws IOException {
                java.io.File file = new java.io.File("test.doc");
                return file;
            }

            @Override
            public long contentLength() throws IOException {
                return 0;
            }

            @Override
            public long lastModified() throws IOException {
                return 0;
            }

            @Override
            public Resource createRelative(String s) throws IOException {
                return null;
            }

            @Override
            public String getFilename() {
                return "test.doc";
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream("LogiGear".getBytes());
            }
        };
        return resource;
    }
}
