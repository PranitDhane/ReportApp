package com.report.ReportApp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.report.ReportApp.entity.Sheet;
import com.report.ReportApp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.report.ReportApp.Constants;


import java.util.*;
import java.util.ArrayList;

@Service
public class UserService {
    private InformationService informationService;

    @Autowired
    public UserService(InformationService informationService) {
        this.informationService = informationService;
    }

    public JsonNode getReport(JsonNode request, ObjectNode response){
        Long reportId =  request.get("reportId").asLong();
        informationService.getInformationbyReportId(reportId,response);
        return response;
    }

}
