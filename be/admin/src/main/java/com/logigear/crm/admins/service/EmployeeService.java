package com.logigear.crm.admins.service;

import com.logigear.crm.admins.mapper.EmployeeMapper;
import com.logigear.crm.admins.model.EmployeeDetails;
import com.logigear.crm.admins.model.RoleName;
import com.logigear.crm.admins.model.User;
import com.logigear.crm.admins.repository.DepartmentRepository;
import com.logigear.crm.admins.repository.EmployeeRepository;
import com.logigear.crm.admins.repository.ProjectRepository;
import com.logigear.crm.admins.repository.RoleRepository;
import com.logigear.crm.admins.repository.UserRepository;
import com.logigear.crm.admins.response.EmployeeDetailsDTO;
import com.logigear.crm.admins.response.EmployeeImageResponse;
import com.logigear.crm.admins.util.FileReaderUtil;
import com.logigear.crm.admins.util.FileUploadUtil;
import com.logigear.crm.admins.util.TemporaryLocalStorage;
import com.logigear.crm.admins.exception.ResourceNotFoundException;
import com.logigear.crm.admins.payload.EmployeePayload;
import com.logigear.crm.admins.util.MessageUtil;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
	private final static String USER_NOT_FOUND_MSG = "User with email %s not found";
	
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final ProjectRepository projectRepository;
    private final EmployeeMapper mapper;
    private final FileUploadUtil fileUploadUtil;
    private final FileReaderUtil fileReaderUtil;
	private final RoleRepository roleRepository;
    
    public EmployeeService(EmployeeRepository employeeRepository,
    					   UserRepository userRepository,
                           EmployeeMapper mapper,
                           FileUploadUtil fileUploadUtil,
                           FileReaderUtil fileReaderUtil,
                           ProjectRepository projectRepository,
                           DepartmentRepository departmentRepository,
                           RoleRepository roleRepository) {
        this.employeeRepository = employeeRepository;
        this.mapper = mapper;
        this.fileUploadUtil = fileUploadUtil;
        this.fileReaderUtil = fileReaderUtil;
        this.projectRepository = projectRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    /**
     * Get the employee details associated with the employee id
     *
     * @param id The provided employee id.
     * @return The employee details of the employee with the associated employee id.
     * @throws NoSuchElementException When no employee associated with the provided employee id.
     * @author bang.ngo
     **/
    public EmployeeDetails getEmployeeDetailsByEmployeeId(Long id) {
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
            if(employee.getProjects() != null || employee.getProjects().size() > 0 ) {
            	emp.setProjects(employee.getProjects());
            }
            
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
    /*
    public EmployeeDetails updateManager(User u, Long id) {
        EmployeeDetailsDTO res = new EmployeeDetailsDTO();
        res.setId(id);
        res.setManager(u.getId());
        if(u.getId().equals(id))
        	throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"This user is not valid, please assign someone else.");
        return updateEmployeeDetails(res);
	}
	
	public EmployeeDetails updateCdm(User u,Long id) {
		EmployeeDetailsDTO res = new EmployeeDetailsDTO();
        res.setId(id);
        res.setCdm(u.getId());
        if(u.getId().equals(id))
        	throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"This user is not valid, please assign someone else.");
        return updateEmployeeDetails(res);
	}*/
	
	public Long findIdByEmail(String email) {
		User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException(
                        MessageUtil.getMessage("MSG_RESOURCE_NOT_FOUND", ""))); 
		return user.getId();
	}
	
	public void validRoles(RoleName role, Long id) {
		if(id != 0) {
			User user = userRepository.findById(id).orElseThrow(
					() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG,id)));	
			if(!user.getRoles().contains(roleRepository.findByName(role))) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"user does not have the valid role");
			}
		}
	}
	
	public EmployeeDetails updateEmployee(EmployeePayload source, Long id){
        EmployeeDetailsDTO res = new EmployeeDetailsDTO();
        res.setId(id);
        
        if(source.getManagerId()!=null) {
	        EmployeeDetails manager = employeeRepository.findById(source.getManagerId())
	        		.orElseThrow(
	                () -> new ResourceNotFoundException(
	                        MessageUtil.getMessage("MSG_RESOURCE_NOT_FOUND", ""))
	        );
	        validRoles(RoleName.MANAGER,source.getManagerId());
	        res.setManager(manager);
        }
        if(source.getCdmId()!= null) {
	        EmployeeDetails cdm = employeeRepository.findById(source.getCdmId())
	        		.orElseThrow(
	                () -> new ResourceNotFoundException(
	                        MessageUtil.getMessage("MSG_RESOURCE_NOT_FOUND", ""))
	        );
	        validRoles(RoleName.CDM,source.getCdmId());
	        res.setCdm(cdm);
	        
        }
        if(source.getDepartment()!=null) {
	        res.setDepartment(departmentRepository.findById(source.getDepartment()).orElseThrow(
	                () -> new ResourceNotFoundException(
	                        MessageUtil.getMessage("MSG_RESOURCE_NOT_FOUND", ""))
	        ));
        }       
        if(source.getProjects()!= null)
        	res.setProjects(projectRepository.findByIdIn(source.getProjects()));
        
        return updateEmployeeDetails(res);
    }
	
}
