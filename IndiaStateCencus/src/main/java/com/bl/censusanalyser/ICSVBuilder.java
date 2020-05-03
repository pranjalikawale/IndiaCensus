package com.bl.censusanalyser;

import java.util.Iterator;

public interface ICSVBuilder {
    public <T> Iterator<T> getCSVIterator(Readable reader, Class<T> classType, char separator) throws CensusAnalyserException, CSVBuilderException;
}
