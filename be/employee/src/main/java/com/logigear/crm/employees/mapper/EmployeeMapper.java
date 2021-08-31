package com.logigear.crm.employees.mapper;

import com.logigear.crm.employees.model.EmployeeDetails;
import com.logigear.crm.employees.response.EmployeeDetailsDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEmployeeFromDto(EmployeeDetailsDTO dto, @MappingTarget EmployeeDetails entity);
}
