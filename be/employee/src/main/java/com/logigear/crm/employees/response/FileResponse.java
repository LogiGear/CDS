package com.logigear.crm.employees.response;

import lombok.*;

import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FileResponse {
    private Long id;
    private String fileName;
    private String status;
    private Instant uploadAt;
    private String note;
}
