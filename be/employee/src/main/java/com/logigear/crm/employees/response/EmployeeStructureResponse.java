package com.logigear.crm.employees.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.logigear.crm.employees.util.FileReaderUtil;
import com.logigear.crm.employees.util.TemporaryLocalStorage;
import org.springframework.beans.factory.annotation.Value;

public interface EmployeeStructureResponse {

    @JsonProperty("id")
    Long getId();
    @JsonProperty("employeeId")
    int getEmployeeID();
    @JsonProperty("fullName")
    String getFullName();
    @JsonProperty("gender")
    String getGender();
    @JsonProperty("degree")
    String getDegree();
    @JsonProperty("major")
    String getMajor();
    @JsonProperty("image")
    @JsonIgnore
    String getImage();

    void setId(Long id);
    void setEmployeeID(int employeeID);
    void setFullName(String fullName);
    void setGender(String gender);
    void setDegree(String degree);
    void setMajor(String major);

    public default String getImageEncode(){
        try{
            FileReaderUtil fileReaderUtil = new FileReaderUtil();
            if (TemporaryLocalStorage.TEMP_IMAGES_STORAGE.containsKey(getImage())) {
                return TemporaryLocalStorage.TEMP_IMAGES_STORAGE.get(getImage());
            }
            byte[] fileContent = fileReaderUtil.readFileFromDisk(getImage());
            String encodedString = fileReaderUtil.getConvertedBase64ImageContentFromImageByteContent(fileContent);
            return TemporaryLocalStorage.mapAndGetEncodedStringToAssociatedImageName(getImage(), encodedString);
        }catch(Exception e){
            FileReaderUtil fileReaderUtil = new FileReaderUtil();
            if (TemporaryLocalStorage.TEMP_IMAGES_STORAGE.containsKey("default")) {
                return TemporaryLocalStorage.TEMP_IMAGES_STORAGE.get("default");
            }
            byte[] fileContent = fileReaderUtil.readFileFromDisk("default");
            String encodedString = fileReaderUtil.getConvertedBase64ImageContentFromImageByteContent(fileContent);
            return TemporaryLocalStorage.mapAndGetEncodedStringToAssociatedImageName("default", encodedString);
        }
    }
}
