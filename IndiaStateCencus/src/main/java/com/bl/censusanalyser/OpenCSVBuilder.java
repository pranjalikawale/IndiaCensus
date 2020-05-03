package com.bl.censusanalyser;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.InputMismatchException;
import java.util.Iterator;

public class OpenCSVBuilder<E> implements ICSVBuilder {

    public <T> Iterator<T> getCSVIterator(Readable reader, Class<T> classType, char separator) throws CSVBuilderException {
        try{
            CsvToBeanBuilder<T> csvToBeanBuilder = new CsvToBeanBuilder<>((Reader) reader);
            //CSVParser parser = new CSVParserBuilder().withSeparator(separator).build();
            csvToBeanBuilder.withType(classType);
            csvToBeanBuilder.withIgnoreLeadingWhiteSpace(true);
            csvToBeanBuilder.withSeparator(separator);
            CsvToBean<T> csvToBean = csvToBeanBuilder.build();
            return csvToBean.iterator();
        }catch (IllegalStateException e) {
            throw new CSVBuilderException(e.getMessage(),
                    CSVBuilderException.ExceptionType.UNABLE_TO_PARSE);
        }

    }
}
