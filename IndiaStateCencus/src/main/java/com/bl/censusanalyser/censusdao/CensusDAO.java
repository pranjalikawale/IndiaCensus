package com.bl.censusanalyser.censusdao;

import com.bl.censusanalyser.analyser.CensusAnalyser;
import com.bl.censusanalyser.model.IndiaStateCodeCSV;
import com.bl.censusanalyser.model.IndiaCensusCSV;
import com.bl.censusanalyser.model.USCensusCSV;

public class CensusDAO {
    public String stateCode;
    public int population;
    public double populationDensity;
    public double totalArea;
    public String state;

    public CensusDAO(IndiaCensusCSV indiaCensusCSV) {
        state=indiaCensusCSV.state;
        totalArea=indiaCensusCSV.areaInSqKm;
        populationDensity=indiaCensusCSV.densityPerSqKm;
        population=indiaCensusCSV.population;
    }

    public CensusDAO(IndiaStateCodeCSV indiaStateCodeCSV) {
        state=indiaStateCodeCSV.state;
        stateCode=indiaStateCodeCSV.stateCode;
    }

    public CensusDAO(USCensusCSV usCensusCSV) {
        stateCode=usCensusCSV.stateId;
        state=usCensusCSV.state;
        totalArea=usCensusCSV.totalArea;
        populationDensity=usCensusCSV.populationDensity;
        population=usCensusCSV.population;
    }

    public Object getCensusDTO(CensusAnalyser.FileType fileType)
    {
        if(fileType.equals(CensusAnalyser.FileType.US))
            return new USCensusCSV(state,stateCode,population,populationDensity,totalArea);
        else if(fileType.equals(CensusAnalyser.FileType.INDIA))
            return new IndiaCensusCSV(state,population,(int)populationDensity,(int)totalArea);
        return new IndiaStateCodeCSV(state,stateCode);
    }
}
