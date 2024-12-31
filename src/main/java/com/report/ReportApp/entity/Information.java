package com.report.ReportApp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "information")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Information {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "info_id")
    private long infoId;
    @Column(name = "infoTitle")
    private String infoTitle;
    @Column(name = "infoName")
    private String infoName;
    @Column(name = "description")
    private String desc;
    @Column(name = "rating")
    private int rating;
    @Column(name = "country")
    private String country;
    @Column(name = "state")
    private String state;
    @Column (name = "city")
    private String city;




}
