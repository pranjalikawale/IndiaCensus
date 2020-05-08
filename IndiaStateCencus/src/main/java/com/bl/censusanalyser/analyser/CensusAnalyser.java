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
    List<CensusDAO> censusCSVList=null;
    Map<String,CensusDAO> censusCSVMap=null;
    public String ColName=null;
    public enum FileType {INDIA,US,INDIASTATE;}
    private  FileType fileType;

    public CensusAnalyser(FileType fileType){
        this.fileType=fileType;
    }

    public <T> int CensusData(FileType fileType,String csvFilePath, Class<T> classType, char seprator) throws CensusAnalyserException {
        SeparatorCheck(seprator);
        checkCsvHeader(csvFilePath); // Check the column
        checkCSVType(classType);
        censusCSVMap= CensusAbstractFactory.getCensusData(fileType,csvFilePath,seprator);
        return censusCSVMap.size();
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

    public  String getStateWiseSortedCensusData() throws CensusAnalyserException {
        checkForListEmpty(censusCSVMap);
        Comparator<CensusDAO> censusComparator=Comparator.comparing(CensusCSV->CensusCSV.state);
        return arraylistInSortedOrder(censusComparator);
    }

    private void checkForListEmpty(Map censusCSVMap) throws CensusAnalyserException {
        if (censusCSVMap==null || censusCSVMap.size()==0){
            throw new CensusAnalyserException("No census data",CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
    }
    private String arraylistInSortedOrder(Comparator <CensusDAO> censusComparator)throws CensusAnalyserException {
        ArrayList censusDTO=censusCSVMap.values().stream()
                .sorted(censusComparator)
                .map(censusDAO->censusDAO.getCensusDTO(fileType))
                .collect(Collectors.toCollection(ArrayList::new));
        return getJson(censusDTO);
    }

    public String getJson(List list) {
        String sortedJson=new Gson().toJson(list);
        return sortedJson;
    }

    public  String getStateCodeWiseSortedCensusData() throws CensusAnalyserException {
        checkForListEmpty(censusCSVMap);
        Comparator<CensusDAO> censusComparator=Comparator.comparing(CensusCSV->CensusCSV.stateCode);
        return arraylistInSortedOrder(censusComparator);
    }


    public  String getPopulationWiseSortedCensusData() throws CensusAnalyserException {
        checkForListEmpty(censusCSVMap);
        Comparator<CensusDAO> censusComparator=Comparator.comparing(CensusCSV->CensusCSV.population);
        return arraylistInSortedOrder(censusComparator.reversed());
    }

    public  String getDensityWiseSortedCensusData() throws CensusAnalyserException {
        checkForListEmpty(censusCSVMap);
        Comparator<CensusDAO> censusComparator=Comparator.comparing(CensusCSV->CensusCSV.populationDensity);
        return arraylistInSortedOrder(censusComparator.reversed());
    }

    public String getAreaWiseSortedCensusData() throws CensusAnalyserException {
        checkForListEmpty(censusCSVMap);
        Comparator<CensusDAO> censusComparator=Comparator.comparing(CensusCSV->CensusCSV.totalArea);
        return arraylistInSortedOrder(censusComparator.reversed());

    }
     /*  public <T> int getCount(Iterator<T> iterator){
        Iterable<T> csvIterable = () -> iterator;
        return (int) StreamSupport.stream(csvIterable.spliterator(), false).count();
    }
    */
}
