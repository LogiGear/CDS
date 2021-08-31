package com.logigear.crm.employees.response;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
