package com.bl.censusanalyser;

import com.google.gson.Gson;
import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.*;
import java.util.stream.StreamSupport;


public class CensusAnalyser {
    List<IndiaCensusCSV> censusCSVList=null;
    List<IndiaStateCodeCSV> stateCodeCSVList =null;

    public int loadIndiaCensusData(String csvFilePath, Class classType, char seprator) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));){
            SeparatorCheck(seprator);
            checkCsvHeader(csvFilePath); // Check the column
            checkCSVType(IndiaCensusCSV.class,classType);
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
           // censusCSVList = csvBuilder.getCSVList(reader,classType,seprator);
            Map<String,Object> censusCSVMapData=new HashMap<>();
            censusCSVMapData=csvBuilder.getCSVMap(reader,classType,seprator);
            censusCSVList= new ArrayList(censusCSVMapData.values());
            return censusCSVMapData.size();
            //return censusCSVList.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
        catch(CSVBuilderException e){
            throw new CensusAnalyserException(e.getMessage(),e.type.name());
        }
    }
    public int loadIndianStateCode(String csvFilePath,Class classType,char seprator) throws CensusAnalyserException {
       // int noOfEnteries=0;
        try(Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            SeparatorCheck(seprator);
            checkCsvHeader(csvFilePath); // Check the column
            checkCSVType(IndiaStateCodeCSV.class,classType);
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            //Iterator<IndiaStateCodeCSV> csvIterable = csvBuilder.getCSVIterator(reader,classType,seprator);
            //noOfEnteries = this.getCount(csvIterable);
            /*Return List
            stateCodeCSVList = csvBuilder.getCSVList(reader,classType,seprator);
            System.out.println(stateCodeCSVList.size());*/
            Map<String,Object> stateCodeCSVMapData=new HashMap<>();
            stateCodeCSVMapData=csvBuilder.getCSVMap(reader,classType,seprator);
            stateCodeCSVList= new ArrayList(stateCodeCSVMapData.values());
            return stateCodeCSVMapData.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.STATECODE_FILE_PROBLEM);
        }
        catch(CSVBuilderException e){
            throw new CensusAnalyserException(e.getMessage(),e.type.name());
        }
        //return noOfEnteries;
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
        if (!(CSVClass.equals(CSVClassType))){
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

    public  String getStateWiseSortedCensusData() throws CensusAnalyserException {
            if (censusCSVList==null || censusCSVList.size()==0){
               throw new CensusAnalyserException("No census data",CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
            }
            Comparator<IndiaCensusCSV> censusComparator=Comparator.comparing(census->census.state);
            this.sort(censusComparator,censusCSVList);
            return getJson(censusCSVList);
    }

    private <T> void sort(Comparator<T> csvComparator, List list){
        for (int i=0;i<list.size()-1;i++){
            for (int j=i;j<list.size()-1;j++){
                 T CSV1=(T) list.get(i);
                 T CSV2=(T) list.get(j+1);
                if (csvComparator.compare(CSV1,CSV2)>0){
                    list.set(i,CSV2);
                    list.set(j+1,CSV1);

                }
            }
        }
    }

    public String getJson(List list)
    {
        String sortedJson=new Gson().toJson(list);
        return sortedJson;
    }

    public  String getStateCodeWiseSortedCensusData() throws CensusAnalyserException {
        if (censusCSVList==null || censusCSVList.size()==0 || stateCodeCSVList==null || stateCodeCSVList.size()==0){
            throw new CensusAnalyserException("No census data",CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<IndiaStateCodeCSV> censusComparator=Comparator.comparing(stateCode->stateCode.stateCode);
        sort(censusComparator,stateCodeCSVList);
        List<IndiaCensusCSV> tempList = new ArrayList<>();
        for(IndiaStateCodeCSV o : stateCodeCSVList) {
            for(IndiaCensusCSV cs :censusCSVList) {

                if (cs.state.equals(o.state)) {
                    tempList.add(cs);
                    break;
                }
            }
        }
        return getJson(tempList);
    }

}
