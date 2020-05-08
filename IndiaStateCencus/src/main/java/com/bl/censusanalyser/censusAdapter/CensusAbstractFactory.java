package com.bl.censusanalyser.censusAdapter;

import com.bl.censusanalyser.analyser.CensusAnalyser;
import com.bl.censusanalyser.censusdao.CensusDAO;
import com.bl.censusanalyser.exception.CensusAnalyserException;
import com.bl.censusanalyser.model.IndiaCensusCSV;
import com.bl.censusanalyser.model.IndiaStateCodeCSV;
import com.bl.censusanalyser.model.USCensusCSV;

import java.util.Map;

public class CensusAbstractFactory {

    public static  <T> Map<String, CensusDAO> getCensusData(CensusAnalyser.CountryAndState countryAndState, String csvFilePath, char seprator) throws CensusAnalyserException {
        if(countryAndState.equals(CensusAnalyser.CountryAndState.INDIA))
            return new CensusLoaderAdapter().loadCensusData(csvFilePath, IndiaCensusCSV.class,seprator);
        else if(countryAndState.equals(CensusAnalyser.CountryAndState.US))
            return new CensusLoaderAdapter().loadCensusData(csvFilePath, USCensusCSV.class,seprator);
        else if(countryAndState.equals(CensusAnalyser.CountryAndState.INDIASTATE))
            return new CensusLoaderAdapter().loadCensusData(csvFilePath, IndiaStateCodeCSV.class,seprator);
        else
            throw new CensusAnalyserException("Incorrect type of file", CensusAnalyserException.ExceptionType.INVALID_FILE_TYPE);
    }
}
