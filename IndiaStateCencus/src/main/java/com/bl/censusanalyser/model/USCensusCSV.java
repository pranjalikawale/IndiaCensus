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
    public double HousingUnits;

    @CsvBindByName(column = "TotalArea", required = true)
    public double totalArea;

    @CsvBindByName(column = "WaterArea")
    public double WaterArea;

    @CsvBindByName(column = "LandArea")
    public double LandArea;

    @CsvBindByName(column = "PopulationDensity", required = true)
    public double populationDensity;

    @CsvBindByName(column = "HousingDensity")
    public double HousingDensity;

    public USCensusCSV(String state, String stateCode, int population, double populationDensity, double totalArea) {
        this.state=state;
        this.stateId=stateCode;
        this.population=population;
        this.populationDensity=populationDensity;
        this.totalArea=totalArea;
    }

    public USCensusCSV() {
    }

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
