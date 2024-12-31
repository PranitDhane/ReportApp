package com.report.ReportApp.repository;

import com.report.ReportApp.entity.Sheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SheetRepository extends JpaRepository<Sheet,Long> {

    @Query("Select s from Sheet s where s.reportDetails.reportId = :reportId")
    List<Sheet> findSheetByReportId(@Param("reportId")Long reportId);
}
