package com.bl.censusanalyser;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;


import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.stream.StreamSupport;

public class CensusAnalyser {
   public int loadIndiaCensusData(String csvFilePath, Class classType, char seprator) throws CensusAnalyserException {
        int namOfEateries = 0;
        try {
            Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
            Iterable<IndiaCensusCSV> csvIterable = () -> this.getCSVFileIterator(reader,IndiaCensusCSV.class);
            namOfEnteries = this.getCount(csvIterable);
            //return namOfEateries;
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
       
        return namOfEateries;

    }
    public int loadIndianStateCode(String csvFilePath,Class classType,char seprator) throws CensusAnalyserException {
        try {
	    Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
            Iterable<IndiaStateCodeCSV> csvIterable = () -> this.getCSVFileIterator(reader,IndiaStateCode.class);
            namOfEnteries = this.getCount(csvIterable);
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
	return namOfEateries;
    }

    public <T> int getCount(Iterator<T> iterator){
        Iterable<T> csvIterable = () -> iterator;
        int namOfEnteries = (int) StreamSupport.stream(csvIterable.spliterator(), false).count();
        return namOfEnteries;
    }

    public <T> Iterator<T> getCSVIterator(Readable reader,Class<T> classType, char separator) throws CensusAnalyserException {
        try{
            if(separator!=',')
            { throw new InputMismatchException(); }
            CsvToBeanBuilder<T> csvToBeanBuilder = new CsvToBeanBuilder<>(reader);
            csvToBeanBuilder.withType(classType);
            csvToBeanBuilder.withIgnoreLeadingWhiteSpace(true);
            csvToBeanBuilder.withSeparator(separator);
            CsvToBean<T> csvToBean = csvToBeanBuilder.build();
            return csvToBean.iterator();
        }catch (IllegalStateException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE);
        }
        catch (InputMismatchException e){
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.WRONG_DELIMETER);
        }
        catch (RuntimeException e){
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.NO_SUCH_CLASS_TYPE);
        }
    }

    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {
        return loadIndiaCensusData(csvFilePath, IndiaCensusCSV.class, ',');
    }

    public int loadIndianStateCode(String csvFilePath) throws CensusAnalyserException {
        return loadIndianStateCode(csvFilePath, IndiaStateCodeCSV.class,',');
    }

}
