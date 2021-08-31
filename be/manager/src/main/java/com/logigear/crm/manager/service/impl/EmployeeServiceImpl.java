package com.logigear.crm.manager.service.impl;

import com.logigear.crm.manager.exception.ResourceNotFoundException;
import com.logigear.crm.manager.model.EmployeeDetails;
import com.logigear.crm.manager.model.RoleName;
import com.logigear.crm.manager.model.query.QDepartment;
import com.logigear.crm.manager.model.query.QEmployeeDetails;
import com.logigear.crm.manager.model.query.QUser;
import com.logigear.crm.manager.payload.EmployeePayload;
import com.logigear.crm.manager.repository.DepartmentRepository;
import com.logigear.crm.manager.repository.EmployeeRepository;
import com.logigear.crm.manager.repository.ProjectRepository;
import com.logigear.crm.manager.response.EmployeeDetailsDTO;
import com.logigear.crm.manager.service.EmployeeService;
import com.logigear.crm.manager.service.MapperService;
import com.logigear.crm.manager.service.RoleService;
import com.logigear.crm.manager.util.MessageUtil;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EntityManager entityManager;
    private final RoleService roleService;
    private final MapperService mapperService;
    private final ProjectRepository projectRepository;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EntityManager entityManager,
            RoleService roleService, MapperService mapperService, DepartmentRepository departmentRepository,
            ProjectRepository projectRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.entityManager = entityManager;
        this.roleService = roleService;
        this.mapperService = mapperService;
        this.projectRepository = projectRepository;
    }

    @Override
    public <T> List<T> findAll(Class<T> type) {
        return employeeRepository.findBy(type);
    }

    @Override
    public List<EmployeeDetails> findFilter(String name, String[] major) {
        QEmployeeDetails employees = QEmployeeDetails.employeeDetails;
        BooleanExpression containsInName = employees.fullName.contains(name);
        BooleanExpression containsInMajor = employees.major.in(major);
        JPQLQuery<Void> query = new JPAQuery<Void>(entityManager);
        return query.select(employees).from(employees).where(containsInName.and(containsInMajor)).fetch();
    }

    @Override
    public <T> List<T> findAll(Long id, Class<T> type) {
        return employeeRepository.findByManagerAndCdm(id, type);
    }

    @Override
    public <T> List<T> findAllExcludeAdmin(Long id, Class<T> type) {
        QEmployeeDetails employee = QEmployeeDetails.employeeDetails;
        QUser user = QUser.user;
        QDepartment department = QDepartment.department;
        BooleanExpression isManagerSubordinates = employee.cdm.id.eq(id);
        BooleanExpression isCdmSubordinates = employee.manager.id.eq(id);
        BooleanExpression isNameAdmin = employee.fullName.eq("Admin");
        JPQLQuery<Void> query = new JPAQuery<Void>(entityManager);
        return query.from(employee).leftJoin(employee.department, department)
                .select(Projections.constructor(type, employee.id, employee.fullName, employee.gender,
                        employee.jobTitle, employee.major, employee.user.email, employee.department.id,
                        employee.department.name))
                .from(employee).where(isNameAdmin.not().andAnyOf(isCdmSubordinates, isManagerSubordinates)).fetch();
    }

    @Override
    public <T> List<T> findAllExcludeAdmin(Class<T> type) {
        QEmployeeDetails employee = QEmployeeDetails.employeeDetails;
        QUser user = QUser.user;
        QDepartment department = QDepartment.department;
        BooleanExpression isNameAdmin = employee.fullName.eq("Admin");
        JPQLQuery<Void> query = new JPAQuery<Void>(entityManager);
        return query.from(employee).leftJoin(employee.department, department)
                .select(Projections.constructor(type, employee.id, employee.fullName, employee.gender,
                        employee.jobTitle, employee.major, employee.user.email, employee.department.id,
                        employee.department.name))
                .from(employee).where(isNameAdmin.not()).fetch();
    }

    @Override
    public EmployeeDetails updateEmployeeDetails(EmployeeDetailsDTO employee) {
        return null;
        // return employeeRepository.findById(employee.getId()).map((emp) -> {
        // mapper.updateEmployeeFromDto(employee, emp);
        // return employeeRepository.save(emp);
        // }).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public <T> T updateEmployee(EmployeePayload source, Long id, Class<T> type) {
        EmployeeDetails emp = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageUtil.getMessage("MSG_RESOURCE_NOT_FOUND", "")));
        emp.setDepartment(departmentRepository.findById(source.getDepartment()).orElseThrow(
                () -> new ResourceNotFoundException(MessageUtil.getMessage("MSG_RESOURCE_NOT_FOUND", ""))));
        emp.setProjects(projectRepository.findByIdIn(source.getProjects()));
        return mapperService.mapObject(employeeRepository.save(emp), type);
    }
}
