package com.report.ReportApp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@Table(name = "report_details")
@Getter
@Setter
@ToString
public class ReportDetails {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "report_Id")
    private Long reportId;

   @Column(name = "report_name",nullable = false)
   private String reportName;

   @OneToMany(mappedBy = "reportDetails",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sheet>sheets;

}
