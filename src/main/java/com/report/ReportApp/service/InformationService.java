package com.report.ReportApp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.report.ReportApp.Utils.GenerateCsvUtils;
import com.report.ReportApp.entity.Information;
import com.report.ReportApp.repository.InformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.context.GenericReactiveWebApplicationContext;
import org.springframework.stereotype.Service;
import com.report.ReportApp.Constants;

import java.util.List;

@Service
public class InformationService {
    private InformationRepository informationRepository;
    private ReportService reportService;
    private GenerateCsvUtils generateCsvUtils;
    @Autowired
    public InformationService(InformationRepository informationRepository, ReportService reportService, GenerateCsvUtils generateCsvUtils) {
        this.informationRepository = informationRepository;
        this.reportService = reportService;
        this.generateCsvUtils=generateCsvUtils;
    }

    public String insertBulkInformationData(ArrayNode inputNode){
        try{
            for (JsonNode node : inputNode) {
                Information information = new Information();
                information.setInfoTitle(node.get("infoTitle").toString());
                information.setInfoName(node.get("infoName").toString());
                information.setDesc(node.get("description").toString());
                information.setRating(Integer.parseInt(node.get("rating").toString()));
                information.setCountry(node.get("country").toString());
                information.setState(node.get("state").toString());
                information.setCity(node.get("city").toString());
                informationRepository.save(information);
            }
        }catch (Exception e){
            e.printStackTrace();
            return "Bulk Upload Fail";
        }
        return "Success Bulk Upload";
    }
    public JsonNode getInformationbyReportId(Long reportId, ObjectNode response){
        reportService.getSheetDataUsingReportId(reportId,response);
        if(response.get(Constants.Status_CODE).asInt()!=200){
            return response;
        }
        String fileLocation = response.get(Constants.storageLocation).asText();
        String reportName = response.get(Constants.infoTitle).toString();
        List<Information> getInfoFromTitle = informationRepository.getInfoFromTitle(reportName);
        generateCsvUtils.createMainReportToCsv(getInfoFromTitle,fileLocation);
        response.put(Constants.Status_CODE , Constants.ok);
        response.put("data",getInfoFromTitle.toString());
        return response;
    }

}
