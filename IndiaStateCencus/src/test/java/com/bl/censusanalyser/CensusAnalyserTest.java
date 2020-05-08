package com.bl.censusanalyser;

import com.bl.censusanalyser.analyser.CensusAnalyser;
import com.bl.censusanalyser.exception.CensusAnalyserException;
import com.bl.censusanalyser.model.IndiaStateCodeCSV;
import com.bl.censusanalyser.model.IndiaCensusCSV;
import com.bl.censusanalyser.model.USCensusCSV;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CensusAnalyserTest {

    private static final String INDIA_CENSUS_CSV_FILE_PATH = "./src/test/resources/IndiaStateCensusData.csv";
    private static final String WRONG_CSV_FILE_PATH = "./src/main/resources/IndiaStateCensusData.csv";
    private static final String INDIAN_STATE_CODE_CSV_FILE_PATH = "./src/test/resources/IndiaStateCode.csv";
    private static final String US_CSV_FILE_PATH = "./src/test/resources/USCensusData.csv";
    @Test
    public void givenIndianCensusCSVFile_ReturnsCorrectRecords() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.CountryAndState.INDIA);
            int numOfRecords = censusAnalyser.loadIndiaCensusData(CensusAnalyser.CountryAndState.INDIA,INDIA_CENSUS_CSV_FILE_PATH);
            Assert.assertEquals(29,numOfRecords);
        } catch (CensusAnalyserException e) { }
    }
    @Test
    public void givenIndiaCensusData_WithWrongFile_ShouldThrowException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.CountryAndState.INDIA);
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CensusAnalyserException.class);
            censusAnalyser.loadIndiaCensusData(CensusAnalyser.CountryAndState.INDIA,WRONG_CSV_FILE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM,e.type);
        }
    }
    @Test
    public void givenIndianCensusCSVFile_WithWrongType_ShouldThrowException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.CountryAndState.INDIA);
            censusAnalyser.CensusData(CensusAnalyser.CountryAndState.INDIA,INDIA_CENSUS_CSV_FILE_PATH,Integer.class,',');
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.NO_SUCH_CLASS_TYPE,e.type);
        }
    }
    @Test
    public void givenIndianCensusCSVFile_WithWrongDelimiter_ShouldThrowException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.CountryAndState.INDIA);
            censusAnalyser.CensusData(CensusAnalyser.CountryAndState.INDIA,INDIA_CENSUS_CSV_FILE_PATH, IndiaCensusCSV.class,';');
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.WRONG_DELIMETER,e.type);
        }
    }
    @Test
    public void givenIndianCensusCSVFile_WithWrongHeader_ShouldThrowException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.CountryAndState.INDIA);
            censusAnalyser.setColName("State");
            censusAnalyser.CensusData(CensusAnalyser.CountryAndState.INDIA,INDIA_CENSUS_CSV_FILE_PATH,IndiaCensusCSV.class,',');
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.WRONG_HEADER,e.type);
        }
    }
    @Test
    public void givenIndiaStateCodeCSV_ShouldReturnExactCount() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.CountryAndState.INDIASTATE);
            int numOfRecords = censusAnalyser.loadIndianStateCode(CensusAnalyser.CountryAndState.INDIASTATE,INDIAN_STATE_CODE_CSV_FILE_PATH);
            Assert.assertEquals(37,numOfRecords);
        } catch (CensusAnalyserException e) { }
    }
    @Test
    public void givenIndiaStateCodeData_WithWrongFile_ShouldThrowException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.CountryAndState.INDIASTATE);
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CensusAnalyserException.class);
            censusAnalyser.loadIndianStateCode(CensusAnalyser.CountryAndState.INDIASTATE,WRONG_CSV_FILE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM,e.type);
        }
    }
    @Test
    public void givenIndianCodeStateCSVFile_WithWrongType_ShouldThrowException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.CountryAndState.INDIASTATE);
            censusAnalyser.CensusData(CensusAnalyser.CountryAndState.INDIASTATE,INDIAN_STATE_CODE_CSV_FILE_PATH,Integer.class,',');
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.NO_SUCH_CLASS_TYPE,e.type);
        }
    }

    @Test
    public void givenIndianCodeStateCSVFile_WithWrongDelimiter_ShouldThrowException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.CountryAndState.INDIASTATE);
            censusAnalyser.CensusData(CensusAnalyser.CountryAndState.INDIASTATE,INDIAN_STATE_CODE_CSV_FILE_PATH, IndiaStateCodeCSV.class,';');
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.WRONG_DELIMETER,e.type);
        }
    }

    @Test
    public void givenIndianCodeStateCSVFile_WithRightHeader_ShouldThrowException1() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.CountryAndState.INDIASTATE);
            censusAnalyser.setColName("State Name");
            censusAnalyser.CensusData(CensusAnalyser.CountryAndState.INDIASTATE,INDIAN_STATE_CODE_CSV_FILE_PATH,IndiaStateCodeCSV.class,',');
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.WRONG_HEADER,e.type);
        }
    }

    @Test
    public void givenIndianCodeStateCSVFile_WithWrongHeader_ShouldThrowException1() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.CountryAndState.INDIASTATE);
            censusAnalyser.setColName("State1");
            censusAnalyser.CensusData(CensusAnalyser.CountryAndState.INDIASTATE,INDIAN_STATE_CODE_CSV_FILE_PATH,IndiaStateCodeCSV.class,',');
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.WRONG_HEADER,e.type);
        }
    }
    @Test
    public void givenIndianCensusData_WhenSortOnState_ShouldReturnFirstSortedResult() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.CountryAndState.INDIA);
            censusAnalyser.loadIndiaCensusData(CensusAnalyser.CountryAndState.INDIA,INDIA_CENSUS_CSV_FILE_PATH);
            String sortedCensusData = censusAnalyser.getSortedCensusData("state","ascending");
            IndiaCensusCSV censusCSV[] = new Gson().fromJson(sortedCensusData, IndiaCensusCSV[].class);
            Assert.assertEquals("Andhra Pradesh", censusCSV[0].state);
        }catch (CensusAnalyserException e) {
                Assert.assertEquals(CensusAnalyserException.ExceptionType.NO_CENSUS_DATA,e.type);
        }
    }

    @Test
    public void givenIndianCensusData_WhenSortOnState_ShouldLastReturnSortedResult() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.CountryAndState.INDIA);
            censusAnalyser.loadIndiaCensusData(CensusAnalyser.CountryAndState.INDIA,INDIA_CENSUS_CSV_FILE_PATH);
            String sortedCensusData = censusAnalyser.getSortedCensusData("state","ascending");
            IndiaCensusCSV censusCSV[] = new Gson().fromJson(sortedCensusData, IndiaCensusCSV[].class);
            Assert.assertEquals("West Bengal", censusCSV[censusCSV.length-1].state);
        }catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.NO_CENSUS_DATA,e.type);
        }
    }
    @Test
    public void givenIndianStateData_WhenSortOnStateCode_ShouldFirstReturnSortedResult() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.CountryAndState.INDIASTATE);
            censusAnalyser.loadIndianStateCode(CensusAnalyser.CountryAndState.INDIASTATE,INDIAN_STATE_CODE_CSV_FILE_PATH);
            String sortedCensusData=censusAnalyser.getSortedCensusData("stateCode","ascending");
            IndiaCensusCSV censusCSV[] = new Gson().fromJson(sortedCensusData, IndiaCensusCSV[].class);
            Assert.assertEquals("Andhra Pradesh New", censusCSV[0].state);
        }catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.NO_CENSUS_DATA,e.type);
        }
    }
    @Test
    public void givenIndianStateData_WhenSortOnStateCode_ShouldLastReturnSortedResult() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.CountryAndState.INDIASTATE);
            censusAnalyser.loadIndianStateCode(CensusAnalyser.CountryAndState.INDIASTATE,INDIAN_STATE_CODE_CSV_FILE_PATH);
            String sortedCensusData=censusAnalyser.getSortedCensusData("stateCode","ascending");
            IndiaCensusCSV censusCSV[] = new Gson().fromJson(sortedCensusData, IndiaCensusCSV[].class);
            Assert.assertEquals("West Bengal", censusCSV[censusCSV.length-1].state);
        }catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.NO_CENSUS_DATA,e.type);
        }
    }
    @Test
    public void givenIndianCensusData_WhenSortOnPopulation_ShouldReturnSortedResult() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.CountryAndState.INDIA);
            censusAnalyser.loadIndiaCensusData(CensusAnalyser.CountryAndState.INDIA,INDIA_CENSUS_CSV_FILE_PATH);
            String sortedCensusData=censusAnalyser.getSortedCensusData("population","descending");
            IndiaCensusCSV censusCSV[] = new Gson().fromJson(sortedCensusData,IndiaCensusCSV[].class);
            Assert.assertEquals("Uttar Pradesh", censusCSV[0].state);
        }catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.NO_CENSUS_DATA,e.type);
        }
    }
    @Test
    public void givenIndianCensusData_WhenSortOnDensity_ShouldReturnSortedResult() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.CountryAndState.INDIA);
            censusAnalyser.loadIndiaCensusData(CensusAnalyser.CountryAndState.INDIA,INDIA_CENSUS_CSV_FILE_PATH);
            String sortedCensusData=censusAnalyser.getSortedCensusData("populationDensity","descending");
            IndiaCensusCSV censusCSV[] = new Gson().fromJson(sortedCensusData,IndiaCensusCSV[].class);
            Assert.assertEquals("Bihar", censusCSV[0].state);
        }catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.NO_CENSUS_DATA,e.type);
        }
    }
    @Test
    public void givenIndianCensusData_WhenSortOnArea_ShouldReturnSortedResult() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.CountryAndState.INDIA);
            censusAnalyser.loadIndiaCensusData(CensusAnalyser.CountryAndState.INDIA,INDIA_CENSUS_CSV_FILE_PATH);
            String sortedCensusData=censusAnalyser.getSortedCensusData("totalArea","descending");
            IndiaCensusCSV censusCSV[] = new Gson().fromJson(sortedCensusData, IndiaCensusCSV[].class);
            Assert.assertEquals("Rajasthan", censusCSV[0].state);
        }catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.NO_CENSUS_DATA,e.type);
        }
    }
    @Test
    public void givenIndianCensusData_WhenSortOnArea_ShouldHandleException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.CountryAndState.INDIASTATE);
            String sortedCensusData=censusAnalyser.getSortedCensusData("totalArea","descending");
            IndiaCensusCSV censusCSV[] = new Gson().fromJson(sortedCensusData, IndiaCensusCSV[].class);
            Assert.assertEquals("Rajasthan", censusCSV[0].state);
        }catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.NO_CENSUS_DATA,e.type);
        }
    }
    @Test
    public void givenUSCensusCSVFile_ReturnsCorrectRecords() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.CountryAndState.US);
            int numOfRecords = censusAnalyser.loadUSCensusData(CensusAnalyser.CountryAndState.US,US_CSV_FILE_PATH);
            Assert.assertEquals(51,numOfRecords);
        } catch (CensusAnalyserException e) { }
    }
    @Test
    public void givenUSCensusData_WhenSortOnPopulation_ShouldReturnSortedResult() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.CountryAndState.US);
            censusAnalyser.loadIndiaCensusData(CensusAnalyser.CountryAndState.US,US_CSV_FILE_PATH);
            String sortedCensusData=censusAnalyser.getSortedCensusData("population","descending");
            USCensusCSV censusCSV[] = new Gson().fromJson(sortedCensusData, USCensusCSV[].class);
            Assert.assertEquals("California", censusCSV[0].state);
        }catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.NO_CENSUS_DATA,e.type);
        }
    }
    @Test
    public void givenUSCensusData_WhenSortOnStateCode_ShouldReturnSortedResult() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.CountryAndState.US);
            censusAnalyser.loadIndiaCensusData(CensusAnalyser.CountryAndState.US,US_CSV_FILE_PATH);
            String sortedCensusData=censusAnalyser.getSortedCensusData("stateCode","ascending");
            USCensusCSV censusCSV[] = new Gson().fromJson(sortedCensusData, USCensusCSV[].class);
            Assert.assertEquals("Alaska", censusCSV[0].state);
        }catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.NO_CENSUS_DATA,e.type);
        }
    }
    @Test
    public void givenUSCensusData_WhenSortOnDensity_ShouldReturnSortedResult() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.CountryAndState.US);
            censusAnalyser.loadIndiaCensusData(CensusAnalyser.CountryAndState.US,US_CSV_FILE_PATH);
            String sortedCensusData=censusAnalyser.getSortedCensusData("populationDensity","descending");
            USCensusCSV censusCSV[] = new Gson().fromJson(sortedCensusData, USCensusCSV[].class);
            Assert.assertEquals("District of Columbia", censusCSV[0].state);
        }catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.NO_CENSUS_DATA,e.type);
        }
    }
    @Test
    public void givenUSCensusData_WhenSortOnTotalArea_ShouldReturnSortedResult() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.CountryAndState.US);
            censusAnalyser.loadIndiaCensusData(CensusAnalyser.CountryAndState.US,US_CSV_FILE_PATH);
            String sortedCensusData=censusAnalyser.getSortedCensusData("totalArea","descending");
            USCensusCSV censusCSV[] = new Gson().fromJson(sortedCensusData, USCensusCSV[].class);
            Assert.assertEquals("Alaska", censusCSV[0].state);
        }catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.NO_CENSUS_DATA,e.type);
        }
    }
}













































































































































































































