package com.report.ReportApp.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.report.ReportApp.Constants;
import com.report.ReportApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<JsonNode> getReport(@RequestBody JsonNode request){
        ObjectNode response = mapper.createObjectNode();

        userService.getReport(request,response);
        if(response.get(Constants.Status_CODE).asInt()==Constants.failer){
            return ResponseEntity.internalServerError().body(response);
        }
        return ResponseEntity.ok(response);
    }
}
