package com.logigear.crm.employees.payload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class UpdateCdmManaRequest {
    Long manager;
    Long cdm;
}
