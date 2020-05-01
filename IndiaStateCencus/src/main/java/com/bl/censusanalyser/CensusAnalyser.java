package com.bl.censusanalyser;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.StreamSupport;

public class CensusAnalyser {
    public int loadIndiaCensusData(String csvFilePath, Class classType, char seprator) throws CensusAnalyserException {
        int namOfEnteries = 0;

        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));){
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<IndiaCensusCSV> csvIterable = csvBuilder.getCSVIterator(reader,classType,seprator);
            namOfEnteries = this.getCount(csvIterable);
            //return namOfEateries;
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }

        return namOfEnteries;

    }
    public int loadIndianStateCode(String csvFilePath,Class classType,char seprator) throws CensusAnalyserException {
        int namOfEnteries=0;
        try(Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<IndiaStateCodeCSV> csvIterable = csvBuilder.getCSVIterator(reader,classType,seprator);
            namOfEnteries = this.getCount(csvIterable);
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
        return namOfEnteries;
    }
    public <T> int getCount(Iterator<T> iterator){
        Iterable<T> csvIterable = () -> iterator;
        return (int) StreamSupport.stream(csvIterable.spliterator(), false).count();
    }


    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {
        return loadIndiaCensusData(csvFilePath, IndiaCensusCSV.class, ',');
    }


    public int loadIndianStateCode(String csvFilePath) throws CensusAnalyserException {
        return loadIndianStateCode(csvFilePath, IndiaStateCodeCSV.class,',');
    }




}
