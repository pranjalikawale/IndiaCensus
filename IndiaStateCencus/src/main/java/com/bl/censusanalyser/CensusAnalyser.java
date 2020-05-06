package com.bl.censusanalyser;

import com.google.gson.Gson;
import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.StreamSupport;


public class CensusAnalyser {
    List<IndiaCensusDAO> censusCSVList=null;
    List<IndiaCensusDAO> stateCodeCSVList =null;
    Map<String,IndiaCensusDAO> censusCSVMap=null;
    public String ColName=null;

    public CensusAnalyser(){
        this.censusCSVList=new ArrayList<>();
        this.stateCodeCSVList=new ArrayList<>();
        this.censusCSVMap=new HashMap<>();
    }

    public int loadIndiaCensusData(String csvFilePath, Class classType, char seprator) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));){
            SeparatorCheck(seprator);
            checkCsvHeader(csvFilePath); // Check the column
            checkCSVType(IndiaCensusCSV.class,classType);
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<IndiaCensusCSV> csvIterator = csvBuilder.getCSVIterator(reader,classType,seprator);
            while (csvIterator.hasNext()) {
                this.censusCSVList.add(new IndiaCensusDAO(csvIterator.next()));
            }
            return this.censusCSVList.size();
            /*Iterable<IndiaCensusCSV> csvIterable=()->csvIterator;
            StreamSupport.stream(csvIterable.spliterator(),false)
                   .forEach(csvState->censusCSVMap.put(csvState.state, new IndiaCensusDAO(csvState)));
            censusCSVList= new ArrayList(censusCSVMap.values());
            censusCSVList = csvBuilder.getCSVList(reader,classType,seprator);
            Map<String,Object> censusCSVMapData=new HashMap<>();
            censusCSVMapData=csvBuilder.getCSVMap(reader,classType,seprator);
            censusCSVList= new ArrayList(censusCSVMapData.values());
            return censusCSVMapData.size();
            */
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
            Iterator<IndiaStateCodeCSV> csvIterator = csvBuilder.getCSVIterator(reader,classType,seprator);
            while (csvIterator.hasNext()) {
                this.stateCodeCSVList.add(new IndiaCensusDAO(csvIterator.next()));
            }
            return stateCodeCSVList.size();
            /*Iterable<IndiaStateCodeCSV> csvIterable=()->csvIterator;
            StreamSupport.stream(csvIterable.spliterator(),false)
                         .forEach(csvState->censusCSVMap.put(csvState.state, new IndiaCensusDAO(csvState)));
            noOfEnteries = this.getCount(csvIterable);
            Return List
            Map<String,Object> stateCodeCSVMapData=new HashMap<>();
            stateCodeCSVMapData=csvBuilder.getCSVMap(reader,classType,seprator);
            stateCodeCSVList= new ArrayList(censusCSVMap.values());
            */
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.STATECODE_FILE_PROBLEM);
        }
        catch(CSVBuilderException e){
            throw new CensusAnalyserException(e.getMessage(),e.type.name());
        }
        //return noOfEnteries;
    }
  /*  public <T> int getCount(Iterator<T> iterator){
        Iterable<T> csvIterable = () -> iterator;
        return (int) StreamSupport.stream(csvIterable.spliterator(), false).count();
    }
    */
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

    public  String getStateWiseSortedCensusData(String sorting) throws CensusAnalyserException {
        checkForListEmpty(censusCSVList);
        Comparator<IndiaCensusDAO> censusComparator=Comparator.comparing(indiaCensusCSV->indiaCensusCSV.state);
        this.sort(censusComparator,censusCSVList,sorting);
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

    public String getJson(List list)
    {
        String sortedJson=new Gson().toJson(list);
        return sortedJson;
    }

    public String getStateCodeWiseSortedCensusData(String sorting) throws CensusAnalyserException {
        checkForListEmpty(censusCSVList);
        checkForListEmpty(stateCodeCSVList);
        Comparator<IndiaCensusDAO> censusComparator=Comparator.comparing(indiaStateCodeCSV->indiaStateCodeCSV.stateCode);
        sort(censusComparator,stateCodeCSVList,sorting);
        List<IndiaCensusDAO> tempList = new ArrayList<>();
        for(IndiaCensusDAO o : stateCodeCSVList) {
            for(IndiaCensusDAO cs :censusCSVList) {
                if (cs.state.equals(o.state)) {
                    tempList.add(cs);
                    break;
                }
            }
        }
        return getJson(tempList);
    }

}
