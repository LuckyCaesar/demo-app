package com.demo.app.entity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiongcaesar on 2018/5/4.
 *
 */
public class DataAnalysisResult {

    private String columnName;

    private BigDecimal averageValue;

    private BigDecimal standardDeviation;

    private Integer outlierNum;

    private Map<String, Integer> factorInfo;

    public static DataAnalysisResult create() {
        return new DataAnalysisResult();
    }

    public DataAnalysisResult columName(String columnName) {
        this.columnName = columnName;
        return this;
    }

    public DataAnalysisResult averageValue(BigDecimal averageValue) {
        this.averageValue = averageValue;
        return this;
    }

    public DataAnalysisResult standardDeviation(BigDecimal standardDeviation) {
        this.standardDeviation = standardDeviation;
        return this;
    }

    public DataAnalysisResult outlierNum(Integer outlierNum) {
        this.outlierNum = outlierNum;
        return this;
    }

    public DataAnalysisResult factorNum(String factorName, Integer factorNum) {
        if (this.factorInfo == null) {
            this.factorInfo = new HashMap<>();
        }
        this.factorInfo.put(factorName, factorNum);
        return this;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public BigDecimal getAverageValue() {
        return averageValue;
    }

    public void setAverageValue(BigDecimal averageValue) {
        this.averageValue = averageValue;
    }

    public BigDecimal getStandardDeviation() {
        return standardDeviation;
    }

    public void setStandardDeviation(BigDecimal standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    public Integer getOutlierNum() {
        return outlierNum;
    }

    public void setOutlierNum(Integer outlierNum) {
        this.outlierNum = outlierNum;
    }

    public Map<String, Integer> getFactorInfo() {
        return factorInfo;
    }

    public void setFactorInfo(Map<String, Integer> factorInfo) {
        this.factorInfo = factorInfo;
    }
}
