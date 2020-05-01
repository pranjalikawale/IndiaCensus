package com.bl.censusanalyser;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;

import java.io.Reader;
import java.util.InputMismatchException;
import java.util.Iterator;

public class OpenCSVBuilder<E> implements ICSVBuilder {
    public String ColName=null;

    public <T> Iterator<T> getCSVIterator(Readable reader, Class<T> classType, char separator) throws CensusAnalyserException {
        try{
          if(separator!=',')
          { throw new InputMismatchException(); }

            HeaderColumnNameMappingStrategy<T> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(classType);
            String columnName=strategy.getColumnName(0);
            if(ColName != columnName){
                throw new Exception();
            }

            CsvToBeanBuilder<T> csvToBeanBuilder = new CsvToBeanBuilder<>((Reader) reader);
            //CSVParser parser = new CSVParserBuilder().withSeparator(separator).build();
            csvToBeanBuilder.withType(classType);
            csvToBeanBuilder.withIgnoreLeadingWhiteSpace(true);
            csvToBeanBuilder.withSeparator(separator);
            CsvToBean<T> csvToBean = csvToBeanBuilder.build();
            return csvToBean.iterator();
        }catch (IllegalStateException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE);
        }
        catch (InputMismatchException e){
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.WRONG_DELIMETER);
        }
        catch (RuntimeException e){
            System.out.println(e.getMessage());
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.NO_SUCH_CLASS_TYPE);
        }
        catch (Exception e){
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.WRONG_HEADER);
        }
    }

    public void getColName(String ColName){
        this.ColName=ColName;
    }
}
/*ColumnPositionMappingStrategy strategy = new ColumnPositionMappingStrategy();
            strategy.setType(IndiaStateCodeCSV.class);
            String[] fields = {"id", "name", "price"};
            strategy.setColumnMapping(fields);*/