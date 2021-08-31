package com.logigear.crm.employees.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class EmployeeImageResponse {
    @NonNull
    private String id;
    private String image;
}
