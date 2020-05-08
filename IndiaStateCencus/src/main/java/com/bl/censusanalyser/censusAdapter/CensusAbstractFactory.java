package com.bl.censusanalyser.censusAdapter;

import com.bl.censusanalyser.analyser.CensusAnalyser;
import com.bl.censusanalyser.censusAdapter.CensusLoaderAdapter;
import com.bl.censusanalyser.censusdao.CensusDAO;
import com.bl.censusanalyser.exception.CensusAnalyserException;
import com.bl.censusanalyser.model.IndiaCensusCSV;
import com.bl.censusanalyser.model.IndiaStateCodeCSV;
import com.bl.censusanalyser.model.USCensusCSV;

import java.util.Map;

public class CensusAbstractFactory {

    public static  <T> Map<String, CensusDAO> getCensusData(CensusAnalyser.FileType fileType, String csvFilePath, char seprator) throws CensusAnalyserException {
        if(fileType.equals(CensusAnalyser.FileType.INDIA))
            return new CensusLoaderAdapter().loadCensusData(csvFilePath, IndiaCensusCSV.class,seprator);
        else if(fileType.equals(CensusAnalyser.FileType.US))
            return new CensusLoaderAdapter().loadCensusData(csvFilePath, USCensusCSV.class,seprator);
        else if(fileType.equals(CensusAnalyser.FileType.INDIASTATE))
            return new CensusLoaderAdapter().loadCensusData(csvFilePath, IndiaStateCodeCSV.class,seprator);
        else
            throw new CensusAnalyserException("Incorrect type of file", CensusAnalyserException.ExceptionType.INVALID_FILE_TYPE);
    }
}
