package com.bl.censusanalyser;

public class IndiaCensusDAO {
    public String stateCode;
    public int population;
    public int densityPerSqKm;
    public int areaInSqKm;
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
}
