package com.bl.censusanalyser.censusdao;

import com.bl.censusanalyser.model.IndiaStateCodeCSV;
import com.bl.censusanalyser.model.IndiaCensusCSV;
import com.bl.censusanalyser.model.USCensusCSV;

public class CensusDAO {
    public String stateCode;
    public int population;
    public double densityPerSqKm;
    public double areaInSqKm;
    public String state;

    public CensusDAO(IndiaCensusCSV indiaCensusCSV) {
        state=indiaCensusCSV.state;
        areaInSqKm=indiaCensusCSV.areaInSqKm;
        densityPerSqKm=indiaCensusCSV.densityPerSqKm;
        population=indiaCensusCSV.population;
    }

    public CensusDAO(IndiaStateCodeCSV indiaStateCodeCSV) {
        state=indiaStateCodeCSV.state;
        stateCode=indiaStateCodeCSV.stateCode;
    }

    public CensusDAO(USCensusCSV usCensusCSV) {
        stateCode=usCensusCSV.stateId;
        state=usCensusCSV.state;
        areaInSqKm=usCensusCSV.totalArea;
        densityPerSqKm=usCensusCSV.populationDensity;
        population=usCensusCSV.population;
    }
}
