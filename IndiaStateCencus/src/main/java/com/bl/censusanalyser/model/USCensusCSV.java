package com.bl.censusanalyser.model;

import com.opencsv.bean.CsvBindByName;

public class USCensusCSV {
    @CsvBindByName(column = "StateId", required = true)
    public String stateId;

    @CsvBindByName(column = "State", required = true)
    public String state;
    @CsvBindByName(column = "Population", required = true)
    public int population;

    @CsvBindByName(column = "HousingUnits")
    public Double HousingUnits;

    @CsvBindByName(column = "TotalArea", required = true)
    public double totalArea;

    @CsvBindByName(column = "WaterArea")
    public Double WaterArea;
    @CsvBindByName(column = "LandArea")
    public Double LandArea;


    @CsvBindByName(column = "PopulationDensity", required = true)
    public double populationDensity;
    @CsvBindByName(column = "HousingDensity")
    public double HousingDensity;

    @Override
    public String toString() {
        return "USCensusCSV{" +
                "State='" + state + '\'' +
                "State ID='" + stateId + '\'' +
                ", Population='" + population + '\'' +
                ", TotalArea='" + totalArea + '\'' +
                ", PopulationDensity='" + populationDensity + '\'' +
                '}';
    }
}
