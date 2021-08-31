package com.logigear.crm.employees.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.logigear.crm.employees.util.FileReaderUtil;
import com.logigear.crm.employees.util.TemporaryLocalStorage;

import lombok.Data;

@Data
public class EmployeeResponse {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("employeeId")
    private int employeeID;
    @JsonProperty("fullName")
    private String fullName;
    @JsonProperty("gender")
    private String gender;
    @JsonProperty("degree")
    private String degree;
    @JsonProperty("major")
    private String major;
    @JsonProperty("image")
    @JsonIgnore
    private String image;
    private String encodedImage;
    private String departmentName;

    private String ConvertTo64(String imageName){
        FileReaderUtil fileReaderUtil = new FileReaderUtil();
        if (TemporaryLocalStorage.TEMP_IMAGES_STORAGE.containsKey(imageName)) {
            return TemporaryLocalStorage.TEMP_IMAGES_STORAGE.get(imageName);
        }
        byte[] fileContent = fileReaderUtil.readFileFromDisk(imageName);
        String encodedString = fileReaderUtil.getConvertedBase64ImageContentFromImageByteContent(fileContent);
        return TemporaryLocalStorage.mapAndGetEncodedStringToAssociatedImageName(imageName, encodedString);
    }

    public EmployeeResponse(Long id,int employeeID,String fullName, String gender, String degree, String major, String image,
                            String departmentName) {
        this.id = id;
        this.employeeID = employeeID;
        this.fullName = fullName;
        this.gender = gender;
        this.degree = degree;
        this.major = major;
        this.departmentName = departmentName;
        try{
            this.encodedImage = ConvertTo64(image);
            this.image = image;
        }catch(Exception e){
            this.encodedImage = ConvertTo64("default");
            this.image = image;
        }
    }
}


