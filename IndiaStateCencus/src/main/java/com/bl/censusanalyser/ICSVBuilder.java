package com.bl.censusanalyser;

import java.io.Reader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public interface ICSVBuilder {
    public <T> Iterator<T> getCSVIterator(Reader reader, Class<T> classType, char separator) throws CSVBuilderException;
    public <T> List<T> getCSVList(Reader reader, Class<T> classType, char separator) throws CSVBuilderException;
    public <T> Map<String,T> getCSVMap(Reader reader, Class<T> classType, char separator) throws CSVBuilderException;
}
