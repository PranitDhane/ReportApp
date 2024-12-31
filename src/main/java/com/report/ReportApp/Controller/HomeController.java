package com.report.ReportApp.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.report.ReportApp.service.InformationService;
import com.report.ReportApp.service.ReportService;
import com.report.ReportApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/home")
public class HomeController {
    private UserService userService;
    private ReportService reportService;
    private InformationService informationService;



    @Autowired
    public HomeController(UserService userService, ReportService adminService  , InformationService informationService) {
        this.informationService=informationService;
        this.userService = userService;
        this.reportService = adminService;
    }


    @GetMapping("/current-user")
    public String getLoggedInUser(Principal principal){
        return principal.getName();
    }

    @PostMapping("/addReportSheetData")
    public String addReportSheetData(@RequestBody JsonNode inputNode){

    return reportService.insertReportSheetDetails(inputNode);
    }

    @PostMapping("/bulkUploadInformationData")
    public String bulkUploadInformationData(@RequestBody ArrayNode inputNode){
       return informationService.insertBulkInformationData( inputNode);
    }

}
