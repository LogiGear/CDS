package com.logigear.crm.employees.payload;

import com.logigear.crm.employees.response.*;

import lombok.*;


@Getter
@Setter
@ToString
public class UpdateCdmManaRequest {
    Long manager;
    Long cdm;
}
