package com.bl.censusanalyser.censusAdapter;

import com.bl.censusanalyser.censusdao.CensusDAO;
import com.bl.censusanalyser.exception.CSVBuilderException;
import com.bl.censusanalyser.exception.CensusAnalyserException;
import com.bl.censusanalyser.model.IndiaCensusCSV;
import com.bl.censusanalyser.model.IndiaStateCodeCSV;
import com.bl.censusanalyser.model.USCensusCSV;
import com.bl.censusanalyser.opencsv.CSVBuilderFactory;
import com.bl.censusanalyser.opencsv.ICSVBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.StreamSupport;

public abstract class CensusAdapter {

    public <T> Map<String,CensusDAO> loadedCensusData(String csvFilePath, Class<T> classType, char seprator) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));){
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<T> csvIterator = csvBuilder.getCSVIterator(reader,classType,seprator);
            Iterable<T> csvIterable=()->csvIterator;
            return getMap(classType ,csvIterable);
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
        catch(CSVBuilderException e){
            throw new CensusAnalyserException(e.getMessage(),e.type.name());
        }
    }

    public <T> Map<String,CensusDAO>  getMap(Class<T> classType ,Iterable<T> csvIterable){
        Map<String,CensusDAO> censusCSVMap=new HashMap<>();

        if(classType.getName().equals("com.bl.censusanalyser.model.IndiaCensusCSV")) {
            StreamSupport.stream(csvIterable.spliterator(), false)
                    .map(IndiaCensusCSV.class::cast)
                    .forEach(censusCSV -> censusCSVMap.put(censusCSV.state, new CensusDAO(censusCSV)));
        }else if(classType.getName().equals("com.bl.censusanalyser.model.USCensusCSV")) {
            StreamSupport.stream(csvIterable.spliterator(), false)
                    .map(USCensusCSV.class::cast)
                    .forEach(censusCSV -> censusCSVMap.put(censusCSV.state, new CensusDAO(censusCSV)));
        }else if(classType.getName().equals("com.bl.censusanalyser.model.IndiaStateCodeCSV")){
            StreamSupport.stream(csvIterable.spliterator(),false)
                    .map(IndiaStateCodeCSV.class::cast)
                    .forEach(censusCSV->censusCSVMap.put(censusCSV.state, new CensusDAO(censusCSV)));
        }
        return censusCSVMap;
    }

}
