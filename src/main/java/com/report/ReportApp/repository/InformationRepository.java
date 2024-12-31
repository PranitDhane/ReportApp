package com.report.ReportApp.repository;

import com.report.ReportApp.entity.Information;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InformationRepository extends JpaRepository<Information,Long> {

    @Query("Select i from Information i where i.infoTitle = :infoTitle")
    List<Information>getInfoFromTitle(@Param("infoTitle")String infoTitle);
}
