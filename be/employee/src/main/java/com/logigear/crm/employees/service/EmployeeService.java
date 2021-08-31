package com.logigear.crm.employees.service;

import java.util.*;
import java.util.stream.Collectors;

import com.logigear.crm.employees.exception.ResourceNotFoundException;
import com.logigear.crm.employees.model.*;
import com.logigear.crm.employees.model.query.QEmployeeDetails;
import com.logigear.crm.employees.repository.*;
import com.logigear.crm.employees.response.*;
import com.logigear.crm.employees.util.FileReaderUtil;
import com.logigear.crm.employees.util.FileUploadUtil;
import com.logigear.crm.employees.util.TemporaryLocalStorage;
import com.logigear.crm.employees.model.Department;
import com.logigear.crm.employees.model.Project;
import com.logigear.crm.employees.repository.DepartmentRepository;
import com.logigear.crm.employees.repository.ProjectRepository;
import com.logigear.crm.employees.response.EmployeeImageResponse;
import com.logigear.crm.employees.response.EmployeeUpdateRequest;
import com.logigear.crm.employees.util.*;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import com.logigear.crm.employees.util.FileReaderUtil;
import com.logigear.crm.employees.util.FileUploadUtil;
import com.logigear.crm.employees.util.TemporaryLocalStorage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.logigear.crm.employees.mapper.EmployeeMapper;

import javax.persistence.EntityManager;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper mapper;
    private final FileUploadUtil fileUploadUtil;
    private final FileReaderUtil fileReaderUtil;
    private final DepartmentRepository departmentRepository;
    private final ProjectRepository projectRepository;
    private final RoleService roleService;
    private final UserRepository userRepository;
    private final EntityManager entityManager;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository,
                           EmployeeMapper mapper,
                           FileUploadUtil fileUploadUtil,
                           FileReaderUtil fileReaderUtil,
                           DepartmentRepository departmentRepository,
                           ProjectRepository projectRepository,
                           RoleService roleService,
                           UserRepository userRepository,
                           EntityManager entityManager) {
        this.employeeRepository = employeeRepository;
        this.mapper = mapper;
        this.fileUploadUtil = fileUploadUtil;
        this.fileReaderUtil = fileReaderUtil;
        this.departmentRepository = departmentRepository;
        this.projectRepository = projectRepository;
        this.roleService = roleService;
        this.userRepository = userRepository;
        this.entityManager = entityManager;
    }

    /**
     * Get the employee details associated with the employee id
     *
     * @param id The provided employee id.
     * @return The employee details of the employee with the associated employee id.
     * @throws NoSuchElementException When no employee associated with the provided employee id.
     * @author bang.ngo
     **/
    public EmployeeDetails getEmployeeDetailsById(Long id) {
        return employeeRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }



    /**
     * Get all employees details from the repository
     *
     * @return The list of employees associated with their EmployeeDetails
     **/
    public List<EmployeeDetails> getEmployees() {
        return employeeRepository.findAll();
    }

    /**
     * Update employee details with the provided updated employee
     *
     * @param employee The requested employee to be updated
     * @return The EmployeeDetails of the updated instance of the employee
     * @author bang.ngo
     **/
    public EmployeeDetails updateEmployeeDetails(EmployeeDetailsDTO employee) {
        // Find employee by id, if exists then map the requested employee details to the current employee instance.
        return employeeRepository.findById(employee.getId()).map((emp) -> {
            mapper.updateEmployeeFromDto(employee, emp);
            return employeeRepository.save(emp);
        }).orElseThrow(NoSuchElementException::new);
    }

    /**
     * Get the the encoded base64 image content associated with the provided employee id, if not existed return the
     * default image content (default.png).
     *
     * @param id The provided employee id.
     * @return The encoded base64 image content in string associated with the employee id.
     * @author bang.ngo
     **/
    public String getEncodedBase64ImageByEmployeeId(Long id) {
        return employeeRepository.findById(id).map((emp) -> {
            //Get image name absolute path from Employee instance
            String imageName = emp.getImage();

            //Check if TEMP_IMAGES_STORAGE contains the temp image name, if it exists the immediately return
            // the image encoded in base64.
            if (TemporaryLocalStorage.TEMP_IMAGES_STORAGE.containsKey(imageName)) {
                return TemporaryLocalStorage.TEMP_IMAGES_STORAGE.get(imageName);
            }

            //Read file content from disk with associated image file name
            byte[] fileContent = fileReaderUtil.readFileFromDisk(imageName);

            //Convert image file content to base64 type.
            String encodedString = fileReaderUtil.getConvertedBase64ImageContentFromImageByteContent(fileContent);

            //Map the encoding to the associated image name.
            return TemporaryLocalStorage.mapAndGetEncodedStringToAssociatedImageName(imageName, encodedString);

        }) //If employee is not found based on the given employee id then throw the exception.
                .orElseThrow(NoSuchElementException::new);
    }

    /**
     * Upload the image with the associated employee id to the disk and save the image file name to the database.
     *
     * @param id   The provided employee id.
     * @param file The provided image as multipart-file.
     * @author bang.ngo
     **/
    @Transactional
    public void uploadImageByEmployeeId(MultipartFile file, Long id) {
        //Using .ifPresent to avoid NullPointerException
        employeeRepository.findById(id).map((emp) -> {
            //Given: File name is equal or greater than OS recommendation (255 chars)
            //When: Creating a file with modified name will make the file larger than 255 chars.
            //Then: Bad practices!
            //  String imageName = id + "_" + file.getOriginalFilename();
            String imageAbsoluteName = fileUploadUtil
                    .getPreparedImageFileNameWithAssociatedEmployeeId(String.valueOf(id));
            emp.setImage(imageAbsoluteName);
            fileUploadUtil.writePNGImageFileToDisk(imageAbsoluteName, file);
            return employeeRepository.save(emp);
        }).orElseThrow(NoSuchElementException::new);
    }

    /**
     * Get all the employees profile images from the current repository.
     *
     * @return The list of employee image response which contains employee id and the image content in base64
     * @author bang.ngo
     **/
    public List<EmployeeImageResponse> getAllEmployeesProfileImage() {
        return employeeRepository
                //Get all employee id list
                .findAllEmployeeId()
                //For each employee id in employee id list, set the base64 encoded string to each employee image response
                .map(empIdList -> empIdList.stream().map(empId -> {
                    EmployeeImageResponse eir = new EmployeeImageResponse(empId);
                    eir.setImage(getEncodedBase64ImageByEmployeeId(Long.parseLong(empId)));
                    return eir;
                })
                        //Collect the result as List
                        .collect(Collectors.toList()))
                //If the result is null then throw the exception
                .orElseThrow(NoSuchElementException::new);
    }

    public EmployeeDetails getEmployeeDetailsByEmployeeId(int employeeId) {
        Optional<EmployeeDetails> employeeDetails = employeeRepository.findByEmployeeID(employeeId);

        if (!employeeDetails.isPresent()) {
            return null;
        }

        return employeeDetails.get();
    }

    private Department updateEmployeeDepartment(EmployeeUpdateRequest employeeUpdateRequest) {
        Optional<Department> department = departmentRepository.findById(employeeUpdateRequest.getDepartment());

        if (!department.isPresent()) {
            throw new ResourceNotFoundException("NOT_FOUND");
        }

        if (department.get().isDeleted()) {
            throw new ResourceNotFoundException("DELETED");
        }

        return department.get();
    }

    private Project updateEmployeeProject(EmployeeUpdateRequest employeeUpdateRequest) {
        Optional<Project> project = projectRepository.findById(employeeUpdateRequest.getProject());

        if (!project.isPresent()) {
            throw new ResourceNotFoundException("NOT_FOUND");
        }

        if (project.get().isDeleted()) {
            throw new ResourceNotFoundException("DELETED");
        }

        return project.get();
    }

    private EmployeeDetails updateEmployeeManager(EmployeeUpdateRequest employeeUpdateRequest) {
        Optional<EmployeeDetails> manager = employeeRepository.findById(employeeUpdateRequest.getManager());

        if (!manager.isPresent()) {
            return null;
        }

        if (manager.get().isDeleted()) {
            throw new ResourceNotFoundException("DELETED");
        }

        return manager.get();
    }

    private EmployeeDetails updateEmployeeCdm(EmployeeUpdateRequest employeeUpdateRequest) {
        Optional<EmployeeDetails> cdm = employeeRepository.findById(employeeUpdateRequest.getCdm());

        if (!cdm.isPresent()) {
            return null;
        }

        if (cdm.get().isDeleted()) {
            throw new ResourceNotFoundException("DELETED");
        }

        return cdm.get();
    }

    public EmployeeUpdateRequest updateEmployeeDepartmentAndProject(EmployeeUpdateRequest employeeUpdateRequest) {
        EmployeeDetails employeeDetails = this.getEmployeeDetailsById(employeeUpdateRequest.getId());
        Department department = departmentRepository.findById(employeeUpdateRequest.getDepartment()).orElseThrow(
                () -> new ResourceNotFoundException(
                        MessageUtil.getMessage("MSG_RESOURCE_NOT_FOUND", ""))
        );
        Project project = projectRepository.findById(employeeUpdateRequest.getProject()).orElseThrow(
                () -> new ResourceNotFoundException(
                        MessageUtil.getMessage("MSG_RESOURCE_NOT_FOUND", ""))
        );
        System.out.println("Project: " + project);
        if (employeeDetails == null || project == null || department == null) {
            return null;
        }
        System.out.println(department.toString());
        System.out.println(project.toString());
        employeeDetails.setDepartment(department);
        employeeDetails.getProjects().add(project);
        EmployeeDetails savedEmployeeDetails = employeeRepository.save(employeeDetails);
        System.out.println(savedEmployeeDetails);

        return new EmployeeUpdateRequest(
                employeeUpdateRequest.getId(),
                employeeUpdateRequest.getEmail(),
                employeeUpdateRequest.getEmployeeID(),
                employeeUpdateRequest.getManager(),
                employeeUpdateRequest.getCdm(),
                savedEmployeeDetails.getDepartment().getName(),
                employeeUpdateRequest.getJobTitle(),
                project.getName(),
                employeeUpdateRequest.getStartDate(),
                employeeUpdateRequest.getDepartment(),
                employeeUpdateRequest.getProject(),
                employeeUpdateRequest.getProjects()
        );
    }

    public EmployeeUpdateRequest updateEmployeeWorkingInfo(EmployeeUpdateRequest employeeUpdateRequest) {
        EmployeeDetails employeeDetails = this.getEmployeeDetailsById(employeeUpdateRequest.getId());
        EmployeeDetails manager = this.updateEmployeeManager(employeeUpdateRequest);
        EmployeeDetails cdm = this.updateEmployeeCdm(employeeUpdateRequest);

        if (employeeDetails == null || manager == null || cdm == null) {
            return null;
        }

        employeeDetails.setManager(manager);
        employeeDetails.setCdm(cdm);
        EmployeeDetails savedEmployeeDetails = employeeRepository.save(employeeDetails);

        return new EmployeeUpdateRequest(
                employeeUpdateRequest.getId(),
                employeeUpdateRequest.getEmail(),
                employeeUpdateRequest.getEmployeeID(),
                savedEmployeeDetails.getManager().getId(),
                savedEmployeeDetails.getCdm().getId(),
                employeeUpdateRequest.getDepartmentName(),
                employeeUpdateRequest.getJobTitle(),
                employeeUpdateRequest.getProjectName(),
                employeeUpdateRequest.getStartDate(),
                employeeUpdateRequest.getDepartment(),
                employeeUpdateRequest.getProject(),
                employeeUpdateRequest.getProjects()
        );
    }

//    public EmployeeUpdateRequest updateEmployeeInfo(EmployeeUpdateRequest employeeUpdateRequest, long id) {
//        System.out.println("Hi in Service");
////        EmployeeDetailsDTO employeeDetailsDTO = new EmployeeDetailsDTO();
//        EmployeeDetails employeeDetails = this.getEmployeeDetailsById(id);
//
//        // Update user's manager and cdm
////        Optional<EmployeeDetails> manager = employeeRepository.findById(employeeUpdateRequest.getManager());
////        System.out.println("Manager: " + manager.get().toString());
////        Optional<EmployeeDetails> cdm = employeeRepository.findById(employeeUpdateRequest.getCdm());
////        System.out.println("Cdm: " + cdm.get().toString());
//
//        EmployeeDetails manager = this.updateEmployeeManager(employeeUpdateRequest);
//        EmployeeDetails cdm = this.updateEmployeeCdm(employeeUpdateRequest);
//
////        if (!manager.isPresent() || !cdm.isPresent()) {
////            throw new ResourceNotFoundException("NOT_FOUND");
////        }
////        EmployeeDetails manager = this.updateEmployeeManager(employeeUpdateRequest);
////        EmployeeDetails cdm = this.updateEmployeeCdm(employeeUpdateRequest);
//
////        roleService.validRoles(RoleName.MANAGER, manager.get().getEmail());
////        System.out.println("Role Manager through");
////        roleService.validRoles(RoleName.CDM, cdm.get().getEmail());
////        System.out.println("Role Cdm through");
//
//        // Update user's deparment and project
//        Department department = this.updateEmployeeDepartment(employeeUpdateRequest);
//        Project project = this.updateEmployeeProject(employeeUpdateRequest);
//
//        employeeDetails.setManager(manager);
//        employeeDetails.setCdm(cdm);
//        employeeDetails.setDepartment(department);
//        employeeDetails.getProjects().add(project);
//        EmployeeDetails savedEmployeeDetails = employeeRepository.save(employeeDetails);
//
//
////        employeeDetailsDTO.setId(id);
////        employeeDetailsDTO.setManager(manager.get());
////        employeeDetailsDTO.setCdm(cdm.get());
////        employeeDetailsDTO.setProject(project);
////        employeeDetailsDTO.setDepartment(department);
//
//        return new EmployeeUpdateRequest(
//                savedEmployeeDetails.getManager().getId(),
//                savedEmployeeDetails.getCdm().getId(),
//                savedEmployeeDetails.getDepartment().getId(),
//                project.getId()
//        );
//    }

//    private EmployeeDetails updateEmployeeManager(EmployeeUpdateRequest employeeUpdateRequest) {
//        Optional<EmployeeDetails> manager = employeeRepository.findById(employeeUpdateRequest.getManager());
//
//        if (!manager.isPresent()) {
//            return null;
//        }
//
//        if (manager.get().isDeleted()) {
//            throw new ResourceNotFoundException("DELETED");
//        }
//
//        return manager.get();
//    }
//
//    private EmployeeDetails updateEmployeeCdm(EmployeeUpdateRequest employeeUpdateRequest) {
//        Optional<EmployeeDetails> cdm = employeeRepository.findById(employeeUpdateRequest.getCdm());
//
//        if (!cdm.isPresent()) {
//            return null;
//        }
//
//        if (cdm.get().isDeleted()) {
//            throw new ResourceNotFoundException("DELETED");
//        }
//
//        return cdm.get();
//    }

    public <T> List<T> getEmployeeByDepartment(Long id,boolean deleted,Class<T> type){
        QEmployeeDetails employee = QEmployeeDetails.employeeDetails;
        BooleanExpression isSameDep = employee.department.id.eq(id);
        JPQLQuery<Void> query = new JPAQuery<Void>(entityManager);
        return query.from(employee)
                .select(Projections.constructor(type,
                        employee.id,
                        employee.employeeID,
                        employee.fullName,
                        employee.gender,
                        employee.jobTitle,
                        employee.major,
                        employee.image,
                        employee.department.name))
                .from(employee)
                .where(isSameDep)
                .fetch();
    }

}
