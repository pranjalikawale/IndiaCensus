package com.bl.censusanalyser.analyser;

import com.bl.censusanalyser.censusdao.CensusDAO;
import com.bl.censusanalyser.exception.CSVBuilderException;
import com.bl.censusanalyser.exception.CensusAnalyserException;
import com.bl.censusanalyser.model.IndiaCensusCSV;
import com.bl.censusanalyser.model.IndiaStateCodeCSV;
import com.bl.censusanalyser.model.USCensusCSV;
import com.bl.censusanalyser.opencsv.CSVBuilderFactory;
import com.bl.censusanalyser.opencsv.ICSVBuilder;
import com.sun.tools.jdeprscan.CSV;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.StreamSupport;

public class CensusLoader {
         public <T> Map<String,CensusDAO> loadCensusData(CensusAnalyser.FileType fileType, String csvFilePath,char seprator) throws CensusAnalyserException {
            if(fileType.equals(CensusAnalyser.FileType.INDIA))
                return this.loadCensusData(csvFilePath,IndiaCensusCSV.class,seprator);
            else if(fileType.equals(CensusAnalyser.FileType.US))
                return this.loadCensusData(csvFilePath,USCensusCSV.class,seprator);
            else if(fileType.equals(CensusAnalyser.FileType.INDIASTATE))
                return this.loadCensusData(csvFilePath,IndiaStateCodeCSV.class,seprator);
            else
                throw new CensusAnalyserException("Incorrect type of file", CensusAnalyserException.ExceptionType.INVALID_FILE_TYPE);
         }

        public <T> Map<String,CensusDAO> loadCensusData(String csvFilePath, Class<T> classType, char seprator) throws CensusAnalyserException {
        Map<String,CensusDAO> censusCSVMap=new HashMap<>();
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));){
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<T> csvIterator = csvBuilder.getCSVIterator(reader,classType,seprator);
            Iterable<T> csvIterable=()->csvIterator;
            if(classType.getName().equals("com.bl.censusanalyser.model.IndiaCensusCSV")) {
                StreamSupport.stream(csvIterable.spliterator(), false)
                        .map(IndiaCensusCSV.class::cast)
                        .forEach(censusCSV -> censusCSVMap.put(censusCSV.state, new CensusDAO(censusCSV)));
            }else if(classType.getName().equals("com.bl.censusanalyser.model.USCensusCSV")) {
                StreamSupport.stream(csvIterable.spliterator(), false)
                        .map(USCensusCSV.class::cast)
                        .forEach(censusCSV -> censusCSVMap.put(censusCSV.state, new CensusDAO(censusCSV)));
            }else if(classType.getName()=="com.bl.censusanalyser.model.IndiaStateCodeCSV"){
                StreamSupport.stream(csvIterable.spliterator(),false)
                        .map(IndiaStateCodeCSV.class::cast)
                        .forEach(censusCSV->censusCSVMap.put(censusCSV.state, new CensusDAO(censusCSV)));
            }
            return censusCSVMap;
            /*while (csvIterator.hasNext()) {
                this.stateCodeCSVList.add(new IndiaCensusDAO(csvIterator.next()));}
            noOfEnteries = this.getCount(csvIterable);
            Map<String,Object> stateCodeCSVMapData=new HashMap<>();
            stateCodeCSVMapData=csvBuilder.getCSVMap(reader,classType,seprator);
            */

        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
        catch(CSVBuilderException e){
            throw new CensusAnalyserException(e.getMessage(),e.type.name());
        }
    }
}
