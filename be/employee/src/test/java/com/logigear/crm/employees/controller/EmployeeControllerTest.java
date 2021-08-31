 package com.logigear.crm.employees.controller;


 import com.logigear.crm.employees.model.Department;
 import com.logigear.crm.employees.model.EmployeeDetails;
 import com.logigear.crm.employees.response.DepartmentResponse;
 import com.logigear.crm.employees.response.DepartmentStructureResponse;
 import com.logigear.crm.employees.response.EmployeeResponse;
 import com.logigear.crm.employees.response.ProjectResponse;
 import com.logigear.crm.employees.security.JwtProvider;
 import com.logigear.crm.employees.service.DepartmentService;
 import com.logigear.crm.employees.service.EmployeeService;
 import com.logigear.crm.employees.service.ProjectService;
 import org.junit.jupiter.api.Test;
 import org.junit.jupiter.api.extension.ExtendWith;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
 import org.springframework.boot.test.context.SpringBootTest;
 import org.springframework.boot.test.mock.mockito.MockBean;
 import org.springframework.core.io.ClassPathResource;
 import org.springframework.core.io.Resource;
 import org.springframework.data.projection.ProjectionFactory;
 import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
 import org.springframework.http.HttpHeaders;
 import org.springframework.http.MediaType;
 import org.springframework.mock.web.MockMultipartFile;
 import org.springframework.security.test.context.support.WithMockUser;
 import org.springframework.test.context.junit.jupiter.SpringExtension;
 import org.springframework.test.web.servlet.MockMvc;
 import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
 import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
 import org.springframework.test.web.servlet.setup.MockMvcBuilders;
// import lombok.var;

 import javax.persistence.EntityManager;
 import java.io.FileInputStream;
 import java.util.*;

 import static org.hamcrest.CoreMatchers.containsString;
 import static org.hamcrest.CoreMatchers.is;
 import static org.hamcrest.Matchers.hasSize;
 import static org.mockito.ArgumentMatchers.any;
 import static org.mockito.BDDMockito.given;
 import static org.mockito.Mockito.when;
 import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
 import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

     @MockBean
     private DepartmentService departmentService;

     @MockBean
     private ProjectService projectService;

     private ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

     @Test
     @WithMockUser(username="huy",roles={"USER"})
     public void getEmployeeById_successWithUser() throws Exception {

         EmployeeDetails employeeDetails = new EmployeeDetails();
         employeeDetails.setId(1L);
         employeeDetails.setFullName("Tuan Huy");

         given(employeeService.getEmployeeDetailsById((long)1)).willReturn(employeeDetails);

         mvc.perform(get("/api/employees/1")
                 .accept(MediaType.APPLICATION_JSON)
                 .contentType(MediaType.APPLICATION_JSON))
                 .andExpect(status().isOk())     // status 200
                 .andExpect(jsonPath("$.id").value(1));

     }
     @Test
     public void getEmployeeById_returnUnAuthorization() throws Exception {

         mvc.perform(get("/api/employees/1")
                 .accept(MediaType.APPLICATION_JSON)
                 .contentType(MediaType.APPLICATION_JSON))
                 .andExpect(status().isUnauthorized());


     }

     @Test
     @WithMockUser(username="huy",roles={"USER"})
     public void whenFileUploaded_thenVerifyStatus()throws Exception {
         Resource resource = new ClassPathResource("image/1.png");
         String fileName = "sampleFile.png";
         MockMultipartFile multipartFile = new MockMultipartFile(
                 "imageFile",
                 fileName,
                 MediaType.MULTIPART_FORM_DATA_VALUE,
                 resource.getInputStream()
         );


         MockMultipartHttpServletRequestBuilder multipartRequest =
                 MockMvcRequestBuilders.multipart("/api/image");



         mvc.perform(multipartRequest.file(multipartFile).param("id", "1"))
                 .andExpect(status().isOk());

 //        Path docRootPath = Path.of(documentRoot, fileName);
 //        filesToBeDeleted.add(docRootPath);
 //        assertThat(Files.exists(docRootPath)).isTrue();
     }
     @Test
     @WithMockUser(username="huy",roles={"USER"})
     public void getAllEmployeeDetail_userNotHavePermission()throws Exception {

         mvc.perform(
                 get("/api/employees")
                         .accept(MediaType.APPLICATION_JSON)
                         .contentType(MediaType.APPLICATION_JSON))
                 .andExpect(status().isUnauthorized());

     }
     @Test
     @WithMockUser(username="huy",roles={"ADMIN"})
     public void getAllEmployeeDetail_Success()throws Exception {
         EmployeeDetails employeeDetails1 = new EmployeeDetails();
         employeeDetails1.setId((long)1);
         employeeDetails1.setFullName("Tuan Huy");
         EmployeeDetails employeeDetails2 = new EmployeeDetails();
         employeeDetails2.setId((long)2);
         employeeDetails2.setFullName("Tuan Huy 2");

         List<EmployeeDetails> records = new ArrayList<>(Arrays.asList(employeeDetails1, employeeDetails2));

         when(employeeService.getEmployees()).thenReturn(records);
         mvc.perform(
                 get("/api/employees")
                         .accept(MediaType.APPLICATION_JSON)
                         .contentType(MediaType.APPLICATION_JSON))
                // .andExpect(status().isUnauthorized())
         .andExpect(status().isOk())
                 .andExpect(jsonPath("$", hasSize(2)))
                 .andExpect(jsonPath("$[1].fullName", is("Tuan Huy 2")));

     }

     @Test
     @WithMockUser(username="huy",roles={"CDM"})
     public void getAllDepartment_Success()throws Exception {
         var department1 = factory.createProjection(DepartmentResponse.class);
         var department2 = factory.createProjection(DepartmentResponse.class);
         department1.setId(1L);
         department1.setName("MoWeDe");
         department2.setId(2L);
         department2.setName("HR");


         List<DepartmentResponse> records = new ArrayList<>();
         records.add(department1);
         records.add(department2);

         given(departmentService.findAllByIsDeleted(DepartmentResponse.class,false)).willReturn(records);

         mvc.perform(get("/api/employees/department")
                         .accept(MediaType.APPLICATION_JSON)
                         .contentType(MediaType.APPLICATION_JSON))
                         .andExpect(status().isOk())
                 .andExpect(jsonPath("$", hasSize(2)))
                 .andExpect(jsonPath("$.[0].id").value(1))
                 .andExpect(jsonPath("$[1].name").value("HR"));

     }
     @Test
     @WithMockUser(username="huy",roles={"CDM"})
     public void getAllProject_Success()throws Exception {
         var project1 = factory.createProjection(ProjectResponse.class);
         var project2 = factory.createProjection(ProjectResponse.class);
         project1.setId(1L);
         project1.setName("CDS");
         project2.setId(2L);
         project2.setName("CDM");


         List<ProjectResponse> records = new ArrayList<>();
         records.add(project1);
         records.add(project2);

         given(projectService.findAllByIsDeleted(ProjectResponse.class,false)).willReturn(records);

         mvc.perform(get("/api/employees/project")
                         .accept(MediaType.APPLICATION_JSON)
                         .contentType(MediaType.APPLICATION_JSON))
                         .andExpect(status().isOk())
                 .andExpect(jsonPath("$", hasSize(2)))
                 .andExpect(jsonPath("$.[0].id").value(1))
                 .andExpect(jsonPath("$[1].name").value("CDM"));

     }

     @Test
     @WithMockUser(username="Thanh",roles={"CDM"})
     public void getDepartmentById_Success()throws Exception {
         var department1 = factory.createProjection(DepartmentStructureResponse.class);
         var department2 = factory.createProjection(DepartmentStructureResponse.class);
         var department3 = factory.createProjection(DepartmentStructureResponse.class);
         department1.setId(1L);
         department1.setName("CDS");
         department2.setId(2L);
         department2.setName("CDM");
         department3.setId(3L);
         department3.setName("CAM");
         Set<DepartmentStructureResponse> childDepartments = new LinkedHashSet<>();
         childDepartments.add(department2);
         childDepartments.add(department3);
         department1.setChildDepartment(childDepartments);

         given(departmentService.findDepartment(DepartmentStructureResponse.class,false,1L)).willReturn(department1);

         mvc.perform(get("/api/employees/department-structure/1")
                 .accept(MediaType.APPLICATION_JSON)
                 .contentType(MediaType.APPLICATION_JSON))
                 .andExpect(status().isOk())
                 .andExpect(jsonPath("$.children", hasSize(2)))
                 .andExpect(jsonPath("$.id").value(1))
                 .andExpect(jsonPath("$.name").value("CDS"))
                 .andExpect(jsonPath("$.children[0].name").value("CDM"));
     }

     @Test
     @WithMockUser(username="Thanh",roles={"CDM"})
     public void getEmployeeByDepId_Success()throws Exception {
         EmployeeResponse employee1 = new EmployeeResponse(
                 1L,0,"Bao Thanh","Male","IT","IT",
                 "default","MoWeDe"
         );
         EmployeeResponse employee2 = new EmployeeResponse(
                 2L,0,"Thanh Bao","Male","TI","TI",
                 "default","DeWeMo"
         );
         List<EmployeeResponse> result = new ArrayList<>();
         result.add(employee1);
         result.add(employee2);

         given(employeeService.getEmployeeByDepartment(1L,false,EmployeeResponse.class)).willReturn(result);

         mvc.perform(get("/api/employees/employee-in-department/1")
                 .accept(MediaType.APPLICATION_JSON)
                 .contentType(MediaType.APPLICATION_JSON))
                 .andExpect(status().isOk())
                 .andExpect(jsonPath("$", hasSize(2)))
                 .andExpect(jsonPath("$.[0].id").value(1))
                 .andExpect(jsonPath("$.[0].fullName").value("Bao Thanh"))
                 .andExpect(jsonPath("$.[1].fullName").value("Thanh Bao"));
     }

 }
