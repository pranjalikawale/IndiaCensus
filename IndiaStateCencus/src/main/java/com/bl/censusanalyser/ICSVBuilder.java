package com.bl.censusanalyser;

import java.io.Reader;
import java.util.Iterator;
import java.util.List;

public interface ICSVBuilder {
    public <T> Iterator<T> getCSVIterator(Reader reader, Class<T> classType, char separator) throws CSVBuilderException;
    public <T> List<T> getCSVList(Reader reader, Class<T> classType, char separator) throws CSVBuilderException;
}
