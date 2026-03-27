package com.anudeep.ApiDashboard.dto;

import java.util.Map;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiRequestDTO {

    private String url;
    private String method;
    private Map<String, String> headers;
    private String body;
}