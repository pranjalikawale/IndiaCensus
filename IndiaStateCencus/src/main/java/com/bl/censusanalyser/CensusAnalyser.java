package com.bl.censusanalyser;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;


import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.StreamSupport;

public class CensusAnalyser {
    public int loadIndiaCensusData(String csvFilePath, Class classType) throws CensusAnalyserException {
        int namOfEateries = 0;
        try {
            Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
            CsvToBeanBuilder<IndiaCensusCSV> csvToBeanBuilder = new CsvToBeanBuilder<>(reader);
            csvToBeanBuilder.withType(classType);
            csvToBeanBuilder.withIgnoreLeadingWhiteSpace(true);
            CsvToBean<IndiaCensusCSV> csvToBean = csvToBeanBuilder.build();
            Iterator<IndiaCensusCSV> censusCSVIterator = csvToBean.iterator();
            Iterable<IndiaCensusCSV> csvIterable = () -> censusCSVIterator;
            namOfEateries = (int) StreamSupport.stream(csvIterable.spliterator(), false).count();
            //return namOfEateries;
        } catch (IllegalStateException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE);
        }
        catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (RuntimeException e){
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.NO_SUCH_CLASS_TYPE);
        }
        return namOfEateries;

    }

    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {
        return loadIndiaCensusData(csvFilePath, IndiaCensusCSV.class);
    }

    public int loadIndianStateCode(String csvFilePath,Class classType) throws CensusAnalyserException {
        try {
            Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
            CsvToBeanBuilder<IndiaStateCodeCSV> csvToBeanBuilder = new CsvToBeanBuilder<>(reader);
            csvToBeanBuilder.withType(classType);
            csvToBeanBuilder.withIgnoreLeadingWhiteSpace(true);
            CsvToBean<IndiaStateCodeCSV> csvToBean = csvToBeanBuilder.build();
            Iterator<IndiaStateCodeCSV> IndiaStateCodeIterator = csvToBean.iterator();
            Iterable<IndiaStateCodeCSV> csvIterable = () -> IndiaStateCodeIterator;
            int namOfEateries = (int) StreamSupport.stream(csvIterable.spliterator(), false).count();
            return namOfEateries;
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (IllegalStateException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE);
        }catch (RuntimeException e){
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.NO_SUCH_CLASS_TYPE);
        }
    }
    public int loadIndianStateCode(String csvFilePath) throws CensusAnalyserException {
        return loadIndiaCensusData(csvFilePath, IndiaCensusCSV.class);
    }

}
