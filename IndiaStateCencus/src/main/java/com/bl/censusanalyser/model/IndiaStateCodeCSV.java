package com.bl.censusanalyser.model;

import com.opencsv.bean.CsvBindByName;

public class IndiaStateCodeCSV {

    @CsvBindByName(column = "State Name", required = true)
    public String state;

    @CsvBindByName(column = "StateCode", required = true)
    public String stateCode;

    @Override
    public String toString() {
        return "IndiaStateCodeCSV{" +
                "State Name='" + state + '\'' +
                ", StateCode='" + stateCode + '\'' +
                '}';
    }
}


