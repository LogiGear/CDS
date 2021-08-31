package com.logigear.crm.init.config;

import com.github.javafaker.Faker;
import com.logigear.crm.init.model.*;
import com.logigear.crm.init.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


@Configuration
public class StartupConfig {


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;
    private final FileRepository fileRepository;
    private final Faker faker;

    @Autowired
    public StartupConfig(UserRepository userRepository, RoleRepository roleRepository,
                         DepartmentRepository departmentRepository,ProjectRepository projectRepository,
                         EmployeeRepository employeeRepository,FileRepository fileRepository,
                         Faker faker
                         ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.departmentRepository = departmentRepository;
        this.projectRepository = projectRepository;
        this.employeeRepository = employeeRepository;
        this.fileRepository = fileRepository;
        this.faker = faker;
    }

    private int parentDepartmentNum = 3;
    private int childDepartmentNum = 2;
    private int childDepartmentOfChildDepartmentNum = 2;
    private int projectNum = 20;
    private int employeeNum = 12000;
    private String defaultPassword = "$2a$10$3u3M0GIcPQ05xLCmi/lX7e8TUdEqWUVircpELjkqUrbVnlxpU9OVm"; //12345678

    @EventListener
    public void afterApplicationReady(ApplicationReadyEvent event) {
        List<Department> allDepartment = new ArrayList<>();
        List<Department> parentDepartment = new ArrayList<>();
        List<Department> childDepartment = new ArrayList<>();
        List<Department> childDepartmentOfChildDepartment = new ArrayList<>();
        List<Project> allProject = new ArrayList<>();

        for(int i =0;i<parentDepartmentNum;i++){
            Department department = new Department();
            department.setName(faker.company().name());
            department = departmentRepository.save(department);
            parentDepartment.add(department);
            allDepartment.add(department);
        }
        for(int i =0;i<childDepartmentNum*parentDepartmentNum;i++){
            Department department = new Department();
            department.setName(faker.job().field());
            department.setParentDepartment(parentDepartment.get(new Random().nextInt(parentDepartment.size())));
            department = departmentRepository.save(department);
            childDepartment.add(department);
            allDepartment.add(department);
        }

        for(int i =0;i<childDepartmentOfChildDepartmentNum*childDepartmentNum*
                parentDepartmentNum;i++){
            Department department = new Department();
            department.setName(faker.programmingLanguage().name());
            department.setParentDepartment(childDepartment.get(new Random().nextInt(childDepartment.size())));
            department = departmentRepository.save(department);
            childDepartmentOfChildDepartment.add(department);
            allDepartment.add(department);
        }

        for(int i =0;i<projectNum;i++){
            Project project = new Project();
            project.setName(faker.app().name()+" "+faker.space().star()+" "+faker.app().version());
            project = projectRepository.save(project);
            allProject.add(project);
        }

        Role adminRole = new Role();
        Role managerRole = new Role();
        Role cdmRole = new Role();
        Role employeeRole = new Role();

        adminRole.setName(RoleName.ADMIN);
        managerRole.setName(RoleName.MANAGER);
        cdmRole.setName(RoleName.CDM);
        employeeRole.setName(RoleName.EMPLOYEE);

        if (!roleRepository.existsByName(RoleName.ADMIN)){
            adminRole = roleRepository.save(adminRole);
        }else{
            adminRole = roleRepository.findByName(RoleName.ADMIN);
        }
        if (!roleRepository.existsByName(RoleName.MANAGER)){
            managerRole = roleRepository.save(managerRole);
        }else{
            managerRole = roleRepository.findByName(RoleName.MANAGER);
        }
        if (!roleRepository.existsByName(RoleName.CDM)){
            cdmRole = roleRepository.save(cdmRole);
        }else{
            cdmRole = roleRepository.findByName(RoleName.CDM);
        }
        if (!roleRepository.existsByName(RoleName.EMPLOYEE)){
            employeeRole = roleRepository.save(employeeRole);
        }else{
            employeeRole = roleRepository.findByName(RoleName.EMPLOYEE);
        }

        List<User> ldapUsers = new ArrayList<>();

        User admin = new User();
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);
        admin.setName("Admin");
        admin.setStatus(UserStatus.ACTIVATED);
        admin.setEmail("admin@gmail.com");
        admin.setPassword(defaultPassword);
        admin.setRoles(adminRoles);

        User manager = new User();
        Set<Role> managerRoles = new HashSet<>();
        managerRoles.add(managerRole);
        manager.setName("Manager");
        manager.setStatus(UserStatus.ACTIVATED);
        manager.setEmail("manager@gmail.com");
        manager.setPassword(defaultPassword);
        manager.setRoles(managerRoles);

        User cdm = new User();
        Set<Role> cdmRoles = new HashSet<>();
        cdmRoles.add(cdmRole);
        cdm.setName("CDM");
        cdm.setStatus(UserStatus.ACTIVATED);
        cdm.setEmail("cdm@gmail.com");
        cdm.setPassword(defaultPassword);
        cdm.setRoles(cdmRoles);

        ldapUsers.add(admin);
        ldapUsers.add(manager);
        ldapUsers.add(cdm);

        for(int i =0;i<ldapUsers.size();i++){
            Instant lowerBound = Instant.now().minus(70*365,ChronoUnit.DAYS);
            Instant upperBound = Instant.now().minus(20*365,ChronoUnit.DAYS);
            Instant birthday = Instant.ofEpochSecond(ThreadLocalRandom.current().nextLong(lowerBound.getEpochSecond(),upperBound.getEpochSecond()));
            Instant contracted = birthday.plus(ThreadLocalRandom.current().nextInt(18*365,30*365),ChronoUnit.DAYS);
            Instant start = contracted.plus(ThreadLocalRandom.current().nextInt(1*7,2*7),ChronoUnit.DAYS);
            Instant issue = start.plus(ThreadLocalRandom.current().nextInt(1*7,2*7),ChronoUnit.DAYS);

            File ldapFile = new File();
            User ldapUser = ldapUsers.get(i);
            EmployeeDetails ldapEmp = new EmployeeDetails();
            ldapEmp.setFullName(faker.name().fullName());
            ldapEmp.setAddress(faker.address().fullAddress());
            ldapEmp.setBankAccount(faker.number().randomNumber(9,false));
            ldapEmp.setInsuranceBookNo(faker.number().randomNumber(13,false));
            ldapEmp.setTaxID(faker.number().randomNumber(9,false));
            ldapEmp.setIdNumber(faker.number().randomNumber(11,false));
            ldapEmp.setBirthDay(birthday.truncatedTo(ChronoUnit.DAYS));
            ldapEmp.setContractedDate(contracted.truncatedTo(ChronoUnit.DAYS));
            ldapEmp.setStartDate(start.truncatedTo(ChronoUnit.DAYS));
            ldapEmp.setIssueDate(issue.truncatedTo(ChronoUnit.DAYS));
            ldapEmp.setBankName(faker.finance().bic());
            ldapEmp.setCellPhone(faker.phoneNumber().cellPhone());
            ldapEmp.setDegree(faker.job().keySkills());
            ldapEmp.setMarriedStatus(faker.relationships().any());
            if(faker.bool().bool()){
                ldapEmp.setGender("Male");
            }else{
                ldapEmp.setGender("Female");
            }
            ldapEmp.setEmployeeID(faker.number().numberBetween(0,999999));
            ldapEmp.setJobTitle(faker.job().title());
            ldapEmp.setPlaceOfBirth(faker.country().name());
            ldapEmp.setRace(faker.harryPotter().house());
            ldapEmp.setReligion(faker.ancient().god());
            ldapEmp.setMajor(faker.educator().university());
            ldapEmp.setUser(ldapUser);
            ldapUser.setEmployee(ldapEmp);
            ldapEmp.setFile(ldapFile);
            if(!userRepository.existsByNameAndEmail(ldapUser.getName(),ldapUser.getEmail())) {
                ldapUser= userRepository.save(ldapUser);
            }else{
                ldapUser = userRepository.findByNameAndEmail(ldapUser.getName(),ldapUser.getEmail());
            }
        }

        for(int i =0;i<employeeNum;i++){
            Random rand = new Random();
            User user = new User();
            Set<Role> empRoles = new HashSet<>();
            Set<Project> empProject = new HashSet<>();
            for(int j=0;j< rand.nextInt(projectNum/(rand.nextInt(3)+1));j++){
                empProject.add(allProject.get(new Random().nextInt(allProject.size())));
            }
            empRoles.add(employeeRole);
            EmployeeDetails emp = new EmployeeDetails();
            String username = faker.name().username();
            user.setName(username);
            user.setStatus(UserStatus.ACTIVATED);
            user.setEmail(username+"@"+faker.internet().domainWord()+"."+faker.internet().domainSuffix());
            user.setPassword(defaultPassword);
            user.setRoles(empRoles);

            Instant lowerBound = Instant.now().minus(70*365,ChronoUnit.DAYS);
            Instant upperBound = Instant.now().minus(20*365,ChronoUnit.DAYS);
            Instant birthday = Instant.ofEpochSecond(ThreadLocalRandom.current().nextLong(lowerBound.getEpochSecond(),upperBound.getEpochSecond()));
            Instant contracted = birthday.plus(ThreadLocalRandom.current().nextInt(18*365,30*365),ChronoUnit.DAYS);
            Instant start = contracted.plus(ThreadLocalRandom.current().nextInt(1*7,2*7),ChronoUnit.DAYS);
            Instant issue = start.plus(ThreadLocalRandom.current().nextInt(1*7,2*7),ChronoUnit.DAYS);

            File empFile = new File();
            emp.setFullName(faker.name().fullName());
            emp.setAddress(faker.address().fullAddress());
            emp.setBankAccount(faker.number().randomNumber(9,false));
            emp.setInsuranceBookNo(faker.number().randomNumber(13,false));
            emp.setTaxID(faker.number().randomNumber(9,false));
            emp.setIdNumber(faker.number().randomNumber(12,true));
            emp.setBirthDay(birthday.truncatedTo(ChronoUnit.DAYS));
            emp.setContractedDate(contracted.truncatedTo(ChronoUnit.DAYS));
            emp.setStartDate(start.truncatedTo(ChronoUnit.DAYS));
            emp.setIssueDate(issue.truncatedTo(ChronoUnit.DAYS));
            emp.setBankName(faker.finance().bic());
            emp.setCellPhone(faker.phoneNumber().cellPhone());
            emp.setDegree(faker.job().keySkills());
            emp.setMarriedStatus(faker.relationships().any());
            if(faker.bool().bool()){
                emp.setGender("Male");
            }else{
                emp.setGender("Female");
            }
            emp.setEmployeeID(faker.number().numberBetween(0,999999));
            emp.setJobTitle(faker.job().title());
            emp.setPlaceOfBirth(faker.country().name());
            emp.setRace(faker.harryPotter().house());
            emp.setReligion(faker.ancient().god());
            emp.setMajor(faker.educator().university());
            emp.setUser(user);
            emp.setDepartment(allDepartment.get(new Random().nextInt(allDepartment.size())));
            emp.setProjects(empProject);
            emp.setFile(empFile);
            user.setEmployee(emp);
            user = userRepository.save(user);

            System.out.println(i+1+" rows of mock data generated");
        }

        System.out.println("Assigning department's manager...");

        List<EmployeeDetails> allEmp = employeeRepository.findAll();
        allDepartment = departmentRepository.findAll();
        for(Department dep : allDepartment){
            dep.setManager(allEmp.get(new Random().nextInt(allEmp.size())));
            departmentRepository.save(dep);
        }

        System.out.println("All mock data generated !");
    }
}