package com.bl.censusanalyser.analyser;

import com.bl.censusanalyser.censusdao.CensusDAO;
import com.bl.censusanalyser.exception.CSVBuilderException;
import com.bl.censusanalyser.exception.CensusAnalyserException;
import com.bl.censusanalyser.model.IndiaStateCodeCSV;
import com.bl.censusanalyser.model.IndiaCensusCSV;
import com.bl.censusanalyser.model.USCensusCSV;
import com.google.gson.Gson;
import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CensusAnalyser {
    List<CensusDAO> censusCSVList=null;
    Map<String,CensusDAO> censusCSVMap=null;
    public String ColName=null;
    public enum FileType {INDIA,US,INDIASTATE;}

    public CensusAnalyser(){ }

    public <T> int CensusData(FileType fileType,String csvFilePath, Class<T> classType, char seprator) throws CensusAnalyserException {
        SeparatorCheck(seprator);
        checkCsvHeader(csvFilePath); // Check the column
        checkCSVType(classType);
        censusCSVMap=new CensusLoader().loadCensusData(fileType,csvFilePath,seprator);
        return getCount();
    }
    public int loadIndiaCensusData(FileType fileType,String csvFilePath) throws CensusAnalyserException {
        return CensusData(fileType,csvFilePath, IndiaCensusCSV.class, ',');
    }

    public int loadUSCensusData(FileType fileType,String csvFilePath) throws CensusAnalyserException {
        return CensusData(fileType,csvFilePath, USCensusCSV.class, ',');
    }

    public int loadIndianStateCode(FileType fileType,String csvFilePath) throws CensusAnalyserException {
        return CensusData(fileType,csvFilePath, IndiaStateCodeCSV.class,',');
    }
    public int getCount(){
        censusCSVList= new ArrayList(censusCSVMap.values());
        return this.censusCSVList.size();
    }

    public void SeparatorCheck(char separator) throws CensusAnalyserException {
        if(separator!=',')
        { throw new CensusAnalyserException(CensusAnalyserException.ExceptionType.WRONG_DELIMETER); }
    }

    public void checkCSVType(Class CSVClassType) throws CensusAnalyserException {
        if(!((CSVClassType.getName().equals("com.bl.censusanalyser.model.IndiaCensusCSV"))||(CSVClassType.getName().equals("com.bl.censusanalyser.model.IndiaStateCodeCSV"))||(CSVClassType.getName().equals("com.bl.censusanalyser.model.USCensusCSV")))){
            throw new CensusAnalyserException(CensusAnalyserException.ExceptionType.NO_SUCH_CLASS_TYPE);
        }
    }

    public void setColName(String ColName){
        this.ColName=ColName;
    }

    public void checkCsvHeader(String csvFilePath) throws CensusAnalyserException {
        if(ColName!=null)
        {
            try {
                CSVReader reader1 = null;
                reader1 = new CSVReader(new FileReader(csvFilePath));
                String headerNames[] = new String[0]; //Reading only first line
                headerNames = reader1.readNext();
                //System.out.println("header length : " + headerNames.length);
                Boolean flag = false;
                for (String header : headerNames) {
                    //System.out.println("header : " + headerNames.length);
                    if (header.equals(ColName)) {
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

    public  String getStateWiseSortedCensusData(String sortingOrder) throws CensusAnalyserException {
        checkForListEmpty(censusCSVList);
        Comparator<CensusDAO> censusComparator=Comparator.comparing(indiaCensusCSV->indiaCensusCSV.state);
        this.sort(censusComparator,censusCSVList,sortingOrder);
        return getJson(censusCSVList);
    }

    private void checkForListEmpty(List list) throws CensusAnalyserException {
        if (list==null || list.size()==0){
            throw new CensusAnalyserException("No census data",CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
    }

    private <T> void sort(Comparator<T> csvComparator, List list,String sorting){
        for (int i=0;i<list.size()-1;i++){
            for (int j=i;j<list.size()-1;j++){
                    T CSV1=(T) list.get(i);
                    T CSV2=(T) list.get(j+1);
                if(sorting.equals("ascending")) {
                    if (csvComparator.compare(CSV1,CSV2)>0){
                        list.set(i,CSV2);
                        list.set(j+1,CSV1);
                    }
                }
                else {
                    if (csvComparator.compare(CSV1,CSV2)<0){
                        list.set(i,CSV2);
                        list.set(j+1,CSV1);

                    }
                }
            }
        }
    }

    public String getJson(List list) {
        String sortedJson=new Gson().toJson(list);
        return sortedJson;
    }

    public  String getStateCodeWiseSortedCensusData(String sortingOrder) throws CensusAnalyserException {
        checkForListEmpty(censusCSVList);
        Comparator<CensusDAO> censusComparator=Comparator.comparing(ndiaStateCodeCSV->ndiaStateCodeCSV.stateCode);
        this.sort(censusComparator,censusCSVList,sortingOrder);
        return getJson(censusCSVList);
    }


    public  String getPopulationWiseSortedCensusData(String sortingOrder) throws CensusAnalyserException {
        checkForListEmpty(censusCSVList);
        Comparator<CensusDAO> censusComparator=Comparator.comparing(indiaCensusCSV->indiaCensusCSV.population);
        this.sort(censusComparator,censusCSVList,sortingOrder);
        return getJson(censusCSVList);
    }

    public  String getDensityWiseSortedCensusData(String sortingOrder) throws CensusAnalyserException {
        checkForListEmpty(censusCSVList);
        Comparator<CensusDAO> censusComparator=Comparator.comparing(indiaCensusCSV->indiaCensusCSV.densityPerSqKm);
        this.sort(censusComparator,censusCSVList,sortingOrder);
        return getJson(censusCSVList);
    }

    public String getAreaWiseSortedCensusData(String sortingOrder) throws CensusAnalyserException {
        checkForListEmpty(censusCSVList);
        Comparator<CensusDAO> censusComparator=Comparator.comparing(indiaCensusCSV->indiaCensusCSV.areaInSqKm);
        this.sort(censusComparator,censusCSVList,sortingOrder);
        return getJson(censusCSVList);
    }
     /*  public <T> int getCount(Iterator<T> iterator){
        Iterable<T> csvIterable = () -> iterator;
        return (int) StreamSupport.stream(csvIterable.spliterator(), false).count();
    }
    */
}
