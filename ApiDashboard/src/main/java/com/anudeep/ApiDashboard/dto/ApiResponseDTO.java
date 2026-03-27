package com.anudeep.ApiDashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponseDTO {

    private int status;
    private String body;
    private long timeTaken;
    private String error;
}