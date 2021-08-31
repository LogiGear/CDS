package com.logigear.crm.admins.util;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

/**
 * This class intended for image file reading from disk then return as base64 content.
 **/
@Component
public class FileReaderUtil {
    private static final String IMAGE_SAVE_FOLDER = "image/";
    private static final String DEFAULT_IMAGE_ABSOLUTE_PATH = IMAGE_SAVE_FOLDER + "default.png";
    private static final ClassPathResource DEFAULT_IMAGE_CLASS_PATH_RESOURCE = new ClassPathResource(DEFAULT_IMAGE_ABSOLUTE_PATH);

    private static final String IMAGE_BASE64_TYPE = "data:image/png;base64,";

    /**
     * Get the image content in base64 with the associated image absolute path in PNG extension.
     *
     * @return The base64 image content in byte array
     * @throws IOException The exception is returned when the image is not read correctly from the disk.
     * @author bang.ngo
     **/
    @SneakyThrows(IOException.class)
    public byte[] readFileFromDisk(String imageAbsolutePath) {
        byte[] fileContent;
        if (Objects.isNull(imageAbsolutePath)) {
            fileContent = FileUtils.readFileToByteArray(DEFAULT_IMAGE_CLASS_PATH_RESOURCE.getFile());
        } else {
            try {
                File imageSavingFile = new File(IMAGE_SAVE_FOLDER + imageAbsolutePath);
                fileContent = FileUtils.readFileToByteArray(imageSavingFile);
            } catch (FileNotFoundException e) {
                fileContent = FileUtils.readFileToByteArray(DEFAULT_IMAGE_CLASS_PATH_RESOURCE.getFile());
            }
        }
        return fileContent;
    }

    /**
     * Get the converted base64 image content from the file content in byte array. (Data Conversion)
     *
     * @return The image base64 type name with the associated base64 image data.
     * @author bang.ngo
     **/
    public String getConvertedBase64ImageContentFromImageByteContent(byte[] fileContent) {
        return IMAGE_BASE64_TYPE + Base64.getEncoder().encodeToString(fileContent);
    }
}
