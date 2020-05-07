package com.bl.censusanalyser.analyser;

import com.bl.censusanalyser.censusdao.CensusDAO;
import com.bl.censusanalyser.exception.CSVBuilderException;
import com.bl.censusanalyser.exception.CensusAnalyserException;
import com.bl.censusanalyser.model.IndiaStateCodeCSV;
import com.bl.censusanalyser.model.IndiaCensusCSV;
import com.bl.censusanalyser.model.USCensusCSV;
import com.bl.censusanalyser.opencsv.CSVBuilderFactory;
import com.bl.censusanalyser.opencsv.ICSVBuilder;
import com.google.gson.Gson;
import com.opencsv.CSVReader;
import com.sun.tools.jdeprscan.CSV;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.StreamSupport;


public class CensusAnalyser {
    List<CensusDAO> censusCSVList=null;
    List<CensusDAO> stateCodeCSVList =null;
    Map<String,CensusDAO> censusCSVMap=null;
    public String ColName=null;

    public CensusAnalyser(){
        this.censusCSVList=new ArrayList<>();
        this.stateCodeCSVList=new ArrayList<>();
        this.censusCSVMap=new HashMap<>();
    }

    public <T> int loadCensusData(String csvFilePath, Class<T> classType, char seprator) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));){
            SeparatorCheck(seprator);
            checkCsvHeader(csvFilePath); // Check the column
            checkCSVType(classType);
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
            censusCSVList= new ArrayList(censusCSVMap.values());
            return this.censusCSVList.size();
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

    /*  public <T> int getCount(Iterator<T> iterator){
        Iterable<T> csvIterable = () -> iterator;
        return (int) StreamSupport.stream(csvIterable.spliterator(), false).count();
    }
    */
    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {
        return loadCensusData(csvFilePath, IndiaCensusCSV.class, ',');
    }

    public int loadUSCensusData(String csvFilePath) throws CensusAnalyserException {
        return loadCensusData(csvFilePath, USCensusCSV.class, ',');
    }

    public int loadIndianStateCode(String csvFilePath) throws CensusAnalyserException {
        return loadCensusData(csvFilePath, IndiaStateCodeCSV.class,',');
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
        checkForListEmpty(stateCodeCSVList);
        Comparator<CensusDAO> censusComparator=Comparator.comparing(ndiaStateCodeCSV->ndiaStateCodeCSV.stateCode);
        this.sort(censusComparator,stateCodeCSVList,sortingOrder);
        return getJson(stateCodeCSVList);
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
}
