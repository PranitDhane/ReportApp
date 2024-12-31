package com.report.ReportApp.Utils;

import com.opencsv.CSVWriter;
import com.report.ReportApp.entity.Information;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
@Component
public class GenerateCsvUtils {

    public CompletableFuture<Void>createMainReportToCsv(List<Information> getInfoFromTitle, String storageLocation){
        return CompletableFuture.runAsync(()->{
           try {
               String finalLocation = storageLocation.replace("{ReportName}.csv","testReport.csv");
               System.out.println("FinalFilePath :::: "+finalLocation);

               File directory = new File(finalLocation.trim());
               if (!directory.exists()) {
                   // Create the file and any necessary parent directories
                   directory.getParentFile().mkdirs(); // Create parent directories if they don't exist
                   directory.createNewFile();         // Create the file
                   System.out.println("File created: " + directory.getAbsolutePath());
               }
               System.out.println(directory.exists() +"<----- Is Directory exist");

                   FileWriter outputfiles = new FileWriter(directory);
                   CSVWriter writer = new CSVWriter(outputfiles);
                    String [] header = {"CITY","COUNTRY","DESCRIPTION","INFONAME","RATING","STATE"};
                    writer.writeNext(header);
                   for (Information info : getInfoFromTitle) {
                      String [] content = {info.getCity(),info.getCountry(),info.getDesc(),info.getInfoName(),info.getRating()+"",info.getState()};
                        writer.writeNext(content);
                   }
                writer.close();


           }catch (IOException e){
               e.printStackTrace();

           }

        });
    }

}
