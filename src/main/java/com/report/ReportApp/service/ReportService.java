package com.report.ReportApp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.report.ReportApp.entity.Information;
import com.report.ReportApp.entity.ReportDetails;
import com.report.ReportApp.entity.Sheet;
import com.report.ReportApp.repository.InformationRepository;
import com.report.ReportApp.repository.ReportDetailsRepository;
import com.report.ReportApp.repository.SheetRepository;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.report.ReportApp.Constants;


import java.util.List;
import java.util.Optional;

@Service
public class ReportService {
    @Autowired
    private ReportDetailsRepository reportDetailsRepository;
    @Autowired
    private  SheetRepository sheetRepository;

/*
{
"reportName":GardenReport,
"sheets":[
{
sheetLocation : "/Users/pranitdhane/Documents/ReportApplication/ReportApp",
queryForSheet : "Select * from information where infoTitle = "Gardens";
}
]
}

*/
    public String insertReportSheetDetails(JsonNode node){
        try{
        String reportName = node.get("reportName").toString();
        ArrayNode sheets =  (ArrayNode) node.get("sheets");
        ReportDetails reportDetails = new ReportDetails();
        reportDetails.setReportName(reportName);

            ReportDetails savedReportDetails = reportDetailsRepository.save(reportDetails);

            for (JsonNode sheet : sheets) {
                String sheetLocation = sheet.get("sheetLocation").toString();
                String queryForSheet = sheet.get("queryForSheet").toString();
                String sheetName = sheet.get("sheetName").toString();
                Sheet sheetInfo = new Sheet();
                sheetInfo.setSheetLocation(sheetLocation);
                sheetInfo.setQueryForSheet(queryForSheet);
                sheetInfo.setSheetName(sheetName);
                sheetInfo.setReportDetails(savedReportDetails);
                sheetRepository.save(sheetInfo);
            }
        }catch (Exception e){
            e.printStackTrace();
            return "Something Went Wrong Please Try Again";
        }

    return "Sheet Report Data added successFully";
    }

    public JsonNode getSheetDataUsingReportId(Long reportId, ObjectNode response){
        ReportDetails reportDetails = reportDetailsRepository.findById(reportId).get();

    if(reportDetails != null) {
        response.put("reportName", reportDetails.getReportName());
        response.put("infoTitle",reportDetails.getReportName());

    }else
        response.put(Constants.Status_CODE,Constants.failer);
    List<Sheet> sheet = sheetRepository.findSheetByReportId(reportId);
    if(sheet!=null  &&  !sheet.isEmpty()) {
        response.put("storageLocation", sheet.get(0).getSheetLocation());

    }else
        response.put(Constants.Status_CODE,Constants.failer);

    response.put(Constants.Status_CODE,Constants.ok);
    return response;
    }


}
