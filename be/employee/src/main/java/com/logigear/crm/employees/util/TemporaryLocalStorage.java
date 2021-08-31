package com.logigear.crm.employees.util;

import java.util.HashMap;
import java.util.Map;

/**
 * For storing local storage as key-value short-term memory
 **/
public class TemporaryLocalStorage {
    public static final Map<String, String> TEMP_IMAGES_STORAGE = new HashMap<>();

    /**
     * Map and get the encoded image string to the associated image name and put back to the local storage.
     *
     * @param imageName     The name of the image.
     * @param encodedString The encoded base64 string of the image content.
     * @return The encoded base64 image string
     * @author bang.ngo
     **/
    public static String mapAndGetEncodedStringToAssociatedImageName(String imageName, String encodedString) {
        TEMP_IMAGES_STORAGE.put(imageName, encodedString);
        return encodedString;
    }
}
