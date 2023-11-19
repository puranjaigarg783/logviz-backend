package com.logViz.dataservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogAggregateKey {
    private LocalDate date;
    private Integer hour;
    private String logLevel;
}
