package com.bl.censusanalyser;

import com.google.gson.Gson;
import com.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;

import static java.lang.Comparable.*;

public class CensusAnalyser {
    public int loadIndiaCensusData(String csvFilePath, Class classType, char seprator) throws CensusAnalyserException {
        int namOfEnteries = 0;
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));){
            SeparatorCheck(seprator);
            checkCsvHeader(csvFilePath); // Check the column
            checkCSVType(IndiaCensusCSV.class,classType);
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            List<IndiaCensusCSV> csvList = csvBuilder.getCSVList(reader,classType,seprator);
            return csvList.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
        catch(CSVBuilderException e){
            throw new CensusAnalyserException(e.getMessage(),e.type.name());
        }
    }
    public int loadIndianStateCode(String csvFilePath,Class classType,char seprator) throws CensusAnalyserException {

        int noOfEnteries=0;
        try(Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            SeparatorCheck(seprator);
            checkCsvHeader(csvFilePath); // Check the column
            checkCSVType(IndiaStateCodeCSV.class,classType);
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<IndiaStateCodeCSV> csvIterable = csvBuilder.getCSVIterator(reader,classType,seprator);
            noOfEnteries = this.getCount(csvIterable);
            //List<IndiaStateCodeCSV> csvList = csvBuilder.getCSVList(reader,classType,seprator);
            //return csvList.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
        catch(CSVBuilderException e){
            throw new CensusAnalyserException(e.getMessage(),e.type.name());
        }
        return noOfEnteries;
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


    public String getStateWiseSortedCensusData(String csvFilePath) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));){
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            System.out.println("getSortedState");
            List<IndiaCensusCSV> csvList = csvBuilder.getCSVList(reader,IndiaCensusCSV.class,',');
            System.out.println("List before"+csvList.size());
            Comparator<IndiaCensusCSV> censusComparator=Comparator.comparing(census->census.state);
            this.sort(csvList,censusComparator);
            System.out.println("List after"+csvList.size());

            String sortedJson=new Gson().toJson(csvList);
            System.out.println("Json"+sortedJson);
            return sortedJson;
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
        catch(CSVBuilderException e){
            throw new CensusAnalyserException(e.getMessage(),e.type.name());
        }
    }

    private void sort(List<IndiaCensusCSV>csvList,Comparator<IndiaCensusCSV> censusComparator){
        System.out.println("List Sorted"+csvList.size());
        for (int i=0;i<csvList.size()-1;i++){
            for (int j=0;j<csvList.size()-i-1;j++){
                IndiaCensusCSV censusCSV1=csvList.get(i);
                IndiaCensusCSV censusCSV2=csvList.get(j+1);
                if (censusComparator.compare(censusCSV1,censusCSV2)>0){
                    csvList.set(i,censusCSV2);
                    csvList.set(j+1,censusCSV1);
                }
            }
        }
        System.out.println("List Sortedafter"+csvList.size());

    }
}
