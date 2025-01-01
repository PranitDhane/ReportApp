package com.report.ReportApp.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.report.ReportApp.Constants;
import com.report.ReportApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;

@RestController
@RequestMapping("/user")
public class UserController {
   final ObjectMapper mapper = new ObjectMapper();
   private UserService userService;

   @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/getReport")
    public ResponseEntity<Object> getReport(@RequestBody JsonNode request) throws Exception{
        ObjectNode response = mapper.createObjectNode();
        String mode = request.get("mode").toString();

        if("1".equals(mode)){
            File zipFile = userService.generateReportZip(request.get("reportId").asLong());
            InputStreamResource resource = new InputStreamResource(new FileInputStream(zipFile));
            userService.deleteZipFilesFromTemp();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + zipFile.getName())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        }else{
            System.out.println("Inside the else mode : "+mode);
            userService.getReport(request,response);
            if(response.get(Constants.Status_CODE).asInt()==Constants.failer){
                return ResponseEntity.internalServerError().body(response);
            }
            return ResponseEntity.ok(response);
        }


    }
}
