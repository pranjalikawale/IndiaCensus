package com.bl.censusanalyser.opencsv;

import com.bl.censusanalyser.model.IndiaCensusCSV;
import com.bl.censusanalyser.model.IndiaStateCodeCSV;
import com.bl.censusanalyser.exception.CSVBuilderException;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.Reader;
import java.util.*;

public class OpenCSVBuilder<E> implements ICSVBuilder {
    @Override
    public <T> Iterator<T> getCSVIterator(Reader reader, Class<T> classType, char separator) throws CSVBuilderException {
        return this.getCSVBean(reader,classType,separator).iterator();
    }
    @Override
    public <T> List<T> getCSVList(Reader reader, Class<T> classType, char separator) throws CSVBuilderException {
        return this.getCSVBean(reader,classType,separator).parse();
    }
    @Override
    public <T> Map<String,T> getCSVMap(Reader reader, Class<T> classType, char separator) throws CSVBuilderException {
        List CSVList=this.getCSVBean(reader,classType,separator).parse();
        Map<String,T> csvMapData=new HashMap<>();
               for(Object o : CSVList){
          if(classType.getName()=="com.bl.censusanalyser.model.IndiaStateCodeCSV") {
              IndiaStateCodeCSV cs = (IndiaStateCodeCSV) o;
              csvMapData.put(cs.state, (T)cs);
          }
          else {
              IndiaCensusCSV cs = (IndiaCensusCSV) o;
              csvMapData.put(cs.state, (T)cs);
          }
        }
        return csvMapData;
    }
    public <T> CsvToBean<T> getCSVBean(Reader reader, Class<T> classType, char separator) throws CSVBuilderException {
        try{
            CsvToBeanBuilder<T> csvToBeanBuilder = new CsvToBeanBuilder<>(reader);
            csvToBeanBuilder.withType(classType);
            csvToBeanBuilder.withIgnoreLeadingWhiteSpace(true);
            csvToBeanBuilder.withSeparator(separator);
            return csvToBeanBuilder.build();
        }catch (IllegalStateException e) {
            throw new CSVBuilderException(e.getMessage(),
                    CSVBuilderException.ExceptionType.UNABLE_TO_PARSE);
        }

    }
}
