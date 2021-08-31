package com.logigear.crm.admins.response;

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
