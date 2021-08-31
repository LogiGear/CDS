package com.logigear.crm.employees.payload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
public class UpdateStatus {
    public String status;
    public String note;
}
