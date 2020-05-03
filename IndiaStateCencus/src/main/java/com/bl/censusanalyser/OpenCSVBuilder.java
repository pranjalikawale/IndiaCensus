package com.bl.censusanalyser;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.Reader;
import java.util.Iterator;
import java.util.List;

public class OpenCSVBuilder<E> implements ICSVBuilder {
    @Override
    public <T> Iterator<T> getCSVIterator(Reader reader, Class<T> classType, char separator) throws CSVBuilderException {
        return this.getCSVBean(reader,classType,separator).iterator();
    }
    @Override
    public <T> List<T> getCSVList(Reader reader, Class<T> classType, char separator) throws CSVBuilderException {
        return this.getCSVBean(reader,classType,separator).parse();
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
