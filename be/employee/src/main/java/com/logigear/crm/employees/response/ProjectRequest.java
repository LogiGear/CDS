package com.logigear.crm.employees.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProjectRequest {
    private String manager;
    private String projectName;
}
