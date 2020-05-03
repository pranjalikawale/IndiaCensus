package com.bl.censusanalyser;

import com.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.stream.StreamSupport;

public class CensusAnalyser {
    public int loadIndiaCensusData(String csvFilePath, Class classType, char seprator) throws CensusAnalyserException {
        int namOfEnteries = 0;

        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));){
            SeparatorCheck(seprator);
            checkCsvHeader(csvFilePath); // Check the column
            checkCSVType(IndiaCensusCSV.class,classType);
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<IndiaCensusCSV> csvIterable = csvBuilder.getCSVIterator(reader,classType,seprator);
            namOfEnteries = this.getCount(csvIterable);
            //return namOfEateries;
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
        catch(CSVBuilderException e){
            throw new CensusAnalyserException(e.getMessage(),e.type.name());
        }
        return namOfEnteries;
    }
    public int loadIndianStateCode(String csvFilePath,Class classType,char seprator) throws CensusAnalyserException {

        int namOfEnteries=0;
        try(Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            SeparatorCheck(seprator);
            checkCsvHeader(csvFilePath); // Check the column
            checkCSVType(IndiaStateCodeCSV.class,classType);
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<IndiaStateCodeCSV> csvIterable = csvBuilder.getCSVIterator(reader,classType,seprator);
            namOfEnteries = this.getCount(csvIterable);
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
        catch(CSVBuilderException e){
            throw new CensusAnalyserException(e.getMessage(),e.type.name());
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

    public void SeparatorCheck(char separator) throws CensusAnalyserException {
        if(separator!=',')
        { throw new CensusAnalyserException(CensusAnalyserException.ExceptionType.WRONG_DELIMETER); }
    }

    public void checkCSVType(Class CSVClass,Class CSVClassType) throws CensusAnalyserException {
        if (CSVClass.equals(CSVClass)){
            throw new CensusAnalyserException(CensusAnalyserException.ExceptionType.NO_SUCH_CLASS_TYPE);
        }
    }

    public String ColName=null;
    public void setColName(String ColName){
        this.ColName=ColName;
    }

    public void checkCsvHeader(String csvFilePath) throws CensusAnalyserException {
        if(ColName!=null) {
            try {
                CSVReader reader1 = null;
                reader1 = new CSVReader(new FileReader(csvFilePath));
                String headerNames[] = new String[0]; //Reading only first line
                headerNames = reader1.readNext();
                //System.out.println("header length : " + headerNames.length);
                Boolean flag = false;
                for (String header : headerNames) {
                    if (header == ColName) {
                        flag = true;
                        break;
                    }
                }

                if (flag == false) {
                    throw new CensusAnalyserException(CensusAnalyserException.ExceptionType.WRONG_HEADER);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
