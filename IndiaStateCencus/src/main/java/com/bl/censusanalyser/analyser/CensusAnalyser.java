package com.bl.censusanalyser.analyser;

import com.bl.censusanalyser.censusAdapter.CensusAbstractFactory;
import com.bl.censusanalyser.censusdao.CensusDAO;
import com.bl.censusanalyser.exception.CensusAnalyserException;
import com.bl.censusanalyser.model.IndiaStateCodeCSV;
import com.bl.censusanalyser.model.IndiaCensusCSV;
import com.bl.censusanalyser.model.USCensusCSV;
import com.google.gson.Gson;
import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class CensusAnalyser {
    //Declared field
    Map<String,CensusDAO> censusCSVMap=null;
    public String ColName=null;
    public enum CountryAndState {INDIA,US,INDIASTATE;}
    private CountryAndState countryAndState;

    //Declared Constructor
    public CensusAnalyser(CountryAndState countryAndState){
        this.countryAndState = countryAndState;
    }
    //Cencus Data method used to load data
    public <T> int CensusData(CountryAndState countryAndState, String csvFilePath, Class<T> classType, char seprator)
                              throws CensusAnalyserException {
        SeparatorCheck(seprator);
        checkCsvHeader(csvFilePath); // Check the column
        checkCSVType(classType);
        censusCSVMap= CensusAbstractFactory.getCensusData(countryAndState,csvFilePath,seprator);
        return censusCSVMap.size();
    }
    public int CensusData(CountryAndState countryAndState, String csvFilePath) throws CensusAnalyserException {
        if(countryAndState.equals(CountryAndState.INDIA))
            return CensusData(countryAndState,csvFilePath, IndiaCensusCSV.class,',');
        else if(countryAndState.equals(CountryAndState.US))
            return CensusData(countryAndState,csvFilePath, USCensusCSV.class,',');
        else if(countryAndState.equals(CountryAndState.INDIASTATE))
            return CensusData(countryAndState,csvFilePath, IndiaStateCodeCSV.class,',');
        throw new CensusAnalyserException("Incorrect type of file",
                                            CensusAnalyserException.ExceptionType.INVALID_FILE_TYPE);
    }
    //Check for delimiter
    private void SeparatorCheck(char separator) throws CensusAnalyserException {
        if(separator!=',')
        { throw new CensusAnalyserException(CensusAnalyserException.ExceptionType.WRONG_DELIMETER); }
    }
    //Check for Class Type
    private void checkCSVType(Class CSVClassType) throws CensusAnalyserException {
        if(!((CSVClassType.getName().equals("com.bl.censusanalyser.model.IndiaCensusCSV"))
                ||(CSVClassType.getName().equals("com.bl.censusanalyser.model.IndiaStateCodeCSV"))
                ||(CSVClassType.getName().equals("com.bl.censusanalyser.model.USCensusCSV")))){
            throw new CensusAnalyserException(CensusAnalyserException.ExceptionType.NO_SUCH_CLASS_TYPE);
        }
    }
    //Set column name
    public void setColName(String ColName){
        this.ColName=ColName;
    }
    //Check for header
    private void checkCsvHeader(String csvFilePath) throws CensusAnalyserException {
        if(ColName!=null)
        {
            try {
                CSVReader reader1 = null;
                reader1 = new CSVReader(new FileReader(csvFilePath));
                String headerNames[] = new String[0]; //Reading only first line
                headerNames = reader1.readNext();
                Boolean flag = false;
                for (String header : headerNames) {
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
    //Get Sorted Census Data
    public  String getSortedCensusData(String parameter,String sortingOrder) throws CensusAnalyserException {
        checkForListEmpty(censusCSVMap);
        return arraylistInSortedOrder(sortingParameter(parameter,sortingOrder));
    }
    //Check for Empty List
    private void checkForListEmpty(Map censusCSVMap) throws CensusAnalyserException {
        if (censusCSVMap==null || censusCSVMap.size()==0){
            throw new CensusAnalyserException("No census data",CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
    }
    //Check for Sorting Parameter and order
    private Comparator sortingParameter(String parameter,String sortingOrder)
    {
        Comparator<CensusDAO> censusComparator=null;
        switch(parameter)
        {
            case "state":
                censusComparator=Comparator.comparing(CensusCSV->CensusCSV.state);
                break;
            case "stateCode":
                censusComparator=Comparator.comparing(CensusCSV->CensusCSV.stateCode);
                break;
            case "population":
                censusComparator=Comparator.comparing(CensusCSV->CensusCSV.population);
                break;
            case "populationDensity":
                censusComparator=Comparator.comparing(CensusCSV->CensusCSV.populationDensity);
                break;
            case "totalArea":
                censusComparator=Comparator.comparing(CensusCSV->CensusCSV.totalArea);
                break;
        }

        if(sortingOrder.equals("descending"))
            censusComparator=censusComparator.reversed();

        return censusComparator;
    }

    //Return list in sorted order
    private String arraylistInSortedOrder(Comparator <CensusDAO> censusComparator){
        ArrayList censusDTO=censusCSVMap.values().stream()
                .sorted(censusComparator)
                .map(censusDAO->censusDAO.getCensusDTO(countryAndState))
                .collect(Collectors.toCollection(ArrayList::new));
        return getJson(censusDTO);
    }
    //Return JSON
    private String getJson(List list) {
        String sortedJson=new Gson().toJson(list);
        return sortedJson;
    }

    }