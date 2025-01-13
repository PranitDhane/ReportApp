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
                information.setInfoTitle(node.get("infoTitle").asText());
                information.setInfoName(node.get("infoName").asText());
                information.setDesc(node.get("description").asText());
                information.setRating(Integer.parseInt(node.get("rating").asText()));
                information.setCountry(node.get("country").asText());
                information.setState(node.get("state").asText());
                information.setCity(node.get("city").asText());
                informationRepository.save(information);
            }
        }catch (Exception e){
            e.printStackTrace();
            return "Bulk Upload Fail";
        }
        return "Success Bulk Upload";
    }
    public JsonNode getInformationbyReportId(JsonNode request, ObjectNode response){
        Long reportId = request.get(Constants.reportId).asLong();
        reportService.getSheetDataUsingReportId(reportId,response);
        if(response.get(Constants.Status_CODE).asInt()!=200){
            return response;
        }
        String fileLocation = response.get(Constants.storageLocation).asText();
        String reportName = response.get(Constants.infoTitle).asText();
        List<Information> getInfoFromTitle = informationRepository.getInfoFromTitle(reportName);
        String emailId = request.get("emailId")!=null?request.get("emailId").asText():null;
        if(emailId!=null)generateCsvUtils.createMainReportToCsv(getInfoFromTitle,fileLocation,emailId);
        response.put(Constants.Status_CODE , Constants.ok);
        response.put("data",getInfoFromTitle.toString());
        return response;
    }

}
