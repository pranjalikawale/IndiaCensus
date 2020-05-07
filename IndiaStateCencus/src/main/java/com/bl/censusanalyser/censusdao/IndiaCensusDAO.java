package com.bl.censusanalyser.censusdao;

import com.bl.censusanalyser.model.IndiaStateCodeCSV;
import com.bl.censusanalyser.model.IndiaCensusCSV;
import com.bl.censusanalyser.model.USCensusCSV;

public class IndiaCensusDAO {
    public int stateId;
    public String stateCode;
    public int population;
    public double densityPerSqKm;
    public double areaInSqKm;
    public String state;

    public IndiaCensusDAO(IndiaCensusCSV indiaCensusCSV) {
        state=indiaCensusCSV.state;
        areaInSqKm=indiaCensusCSV.areaInSqKm;
        densityPerSqKm=indiaCensusCSV.densityPerSqKm;
        population=indiaCensusCSV.population;
    }

    public IndiaCensusDAO(IndiaStateCodeCSV indiaStateCodeCSV) {
        state=indiaStateCodeCSV.state;
        stateCode=indiaStateCodeCSV.stateCode;
    }

    public IndiaCensusDAO(USCensusCSV usCensusCSV) {
        stateId=usCensusCSV.stateId;
        state=usCensusCSV.state;
        areaInSqKm=usCensusCSV.totalArea;
        densityPerSqKm=usCensusCSV.populationDensity;
        population=usCensusCSV.population;
    }
}
