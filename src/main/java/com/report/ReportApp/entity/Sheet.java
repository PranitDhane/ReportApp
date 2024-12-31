package com.report.ReportApp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "sheet")
@Setter
@Getter
@ToString
public class Sheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sheet_id")
    private Long sheetId;

    @Column(name = "sheet_location",nullable = false)
    private String sheetLocation;

    @Column(name = "query_for_sheet", nullable = false)
    private String queryForSheet;

    @ManyToOne
    @JoinColumn(name = "report_id", nullable = false)
    private ReportDetails reportDetails;

}
