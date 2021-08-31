package com.logigear.crm.admins.controller;


import com.logigear.crm.admins.model.EmployeeDetails;
import com.logigear.crm.admins.security.JwtProvider;
import com.logigear.crm.admins.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private JwtProvider tokenProvider;

    @MockBean
    private EmployeeService employeeService;

    @Test
    @WithMockUser(username="huy",roles={"USER"})
    public void getEmployeeByIdSuccessWithUser() throws Exception {

        EmployeeDetails employeeDetails = new EmployeeDetails();
        employeeDetails.setId((long)1);
        employeeDetails.setFullName("Tuan Huy");

        given(employeeService.getEmployeeDetailsByEmployeeId((long)1)).willReturn(employeeDetails);

        mvc.perform(get("/api/employees/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())     // status 200
                .andExpect(jsonPath("$.id",is(1)));

    }
    @Test
    public void getEmployeeByIdReturnUnAuthorization() throws Exception {

        mvc.perform(get("/api/employees/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());


    }
    @Test
    @WithMockUser(username="huy",roles={"USER"})
    public void whenFileUploaded_thenVerifyStatus()throws Exception {
       // String endpoint = BASE_URL + "/upload/photo";

        //FileInputStream fis = new FileInputStream("/home/me/Desktop/someDir/image.jpg");
//        Resource resource = new ClassPathResource("image/1.png");
//        MockMultipartFile multipartFile = new MockMultipartFile("imageFile", resource.getFilename(),MediaType.MULTIPART_FORM_DATA_VALUE,resource.getInputStream());
//
//        HashMap<String, String> contentTypeParams = new HashMap<String, String>();
//        contentTypeParams.put("boundary", "265001916915724");
//        MediaType mediaType = new MediaType("multipart", "form-data", contentTypeParams);
//
//        mvc.perform(
//                post("/api/employees/image/upload")
//
//                        .contentType(mediaType)
//
//                        .param("id", "1")
//                        .content(multipartFile.getBytes()))
//                .andExpect(status().isOk());


//        mvc.perform(multipart("/upload").file(file))
//                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(username="huy",roles={"USER"})
    public void getAllEmployeeDetail_UserNotHavePermission()throws Exception {

        mvc.perform(
                get("/api/employees/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }
    @Test
    @WithMockUser(username="huy",roles={"ADMIN"})
    public void getAllEmployeeDetail_Success()throws Exception {

        mvc.perform(
                get("/api/employees/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }

}
