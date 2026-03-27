package com.anudeep.ApiDashboard.dto;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryEntryDTO {

    private String id;
    private String url;
    private String method;
    private Map<String, String> headers;
    private String body;
    private int statusCode;
    private long timestamp;
}