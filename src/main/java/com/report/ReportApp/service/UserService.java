package com.report.ReportApp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.report.ReportApp.entity.ReportDetails;
import com.report.ReportApp.entity.Sheet;
import com.report.ReportApp.model.User;
import com.report.ReportApp.repository.InformationRepository;
import com.report.ReportApp.repository.ReportDetailsRepository;
import com.report.ReportApp.repository.SheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.report.ReportApp.Constants;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class UserService {
    private InformationService informationService;

    @Autowired
    private SheetRepository sheetRepository;

    @Autowired
    private ReportDetailsRepository reportDetailsRepository;

    @Value("${spring.datasource.username}")
    private String userName;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.url}")
    private String url;

    private final String tempDir = "temp-reports";

    @Autowired
    public UserService(InformationService informationService) {
        this.informationService = informationService;
    }

    public JsonNode getReport(JsonNode request, ObjectNode response){
        Long reportId =  request.get("reportId").asLong();
        informationService.getInformationbyReportId(reportId,response);
        return response;
    }

    public File generateReportZip(Long reportId) throws Exception{
        // Get the report Details by report id
        Optional<ReportDetails> reportDetails = reportDetailsRepository.findById(reportId);
        String reportName = reportDetails.map(ReportDetails::getReportName).orElse(null);

        // Get the sheet's data from the sheet table
        List<Sheet> sheetsData = sheetRepository.findSheetByReportId(reportId);
        Map<String, String> sheetQueries = sheetsData.stream()
                                            .collect(Collectors.toMap(Sheet::getSheetName, Sheet::getQueryForSheet));

        Files.createDirectories(Paths.get(tempDir));
        List<CompletableFuture<File>> futures = new ArrayList<>();

        for (Map.Entry<String, String> entry : sheetQueries.entrySet()) {
            String sheetName = entry.getKey();
            String query = entry.getValue();
            String fileName = tempDir + "/" + sheetName + ".csv";

            // thread will have task to run the sql query and write output in csv file
            futures.add(CompletableFuture.supplyAsync(() -> {
                try {
                    return executeQueryAndWriteToCsv(query, fileName);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }));
        }
        // Wait for all CSV files to be generated
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        // Collect all generated files
        List<File> csvFiles = new ArrayList<>();
        for (CompletableFuture<File> future : futures) {
            csvFiles.add(future.get());
        }

        // Create ZIP file
        String zipFileName = tempDir + "/" + reportName + ".zip";
        createZipFile(csvFiles, zipFileName);

        // Clean up temporary CSV files
        for (File file : csvFiles) {
            file.delete();
        }

        return new File(zipFileName);
    }

    private File executeQueryAndWriteToCsv(String query, String fileName) throws Exception {

        try (Connection connection = DriverManager.getConnection(url, userName, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);
             com.opencsv.CSVWriter writer = new com.opencsv.CSVWriter(new FileWriter(fileName))) {

            // Write result set to CSV
            writer.writeAll(resultSet, true);

            return new File(fileName);
        }
    }

    private void createZipFile(List<File> files, String zipFileName) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(zipFileName);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            for (File file : files) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    ZipEntry zipEntry = new ZipEntry(file.getName());
                    zos.putNextEntry(zipEntry);

                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, length);
                    }
                    zos.closeEntry();
                }
            }
        }
    }

    public void deleteZipFilesFromTemp() {
        // Define the temporary directory (modify if your temp dir is different)
        File tempDirectory = new File(tempDir);

        // Check if the directory exists and is a directory
        if (tempDirectory.exists() && tempDirectory.isDirectory()) {
            // List all files with .zip extension
            File[] zipFiles = tempDirectory.listFiles((dir, name) -> name.endsWith(".zip"));

            // Delete each .zip file
            if (zipFiles != null) {
                for (File zipFile : zipFiles) {
                    if (zipFile.delete()) {
                        System.out.println("Deleted: " + zipFile.getName());
                    } else {
                        System.err.println("Failed to delete: " + zipFile.getName());
                    }
                }
            }
        } else {
            System.err.println("Temporary directory not found or is not a directory.");
        }
    }
}
