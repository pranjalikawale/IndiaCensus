package com.bl.censusanalyser.exception;

public class CensusAnalyserException extends Exception {

    public enum ExceptionType {
        CENSUS_FILE_PROBLEM,NO_SUCH_CLASS_TYPE,WRONG_DELIMETER,WRONG_HEADER,NO_CENSUS_DATA,STATECODE_FILE_PROBLEM;
    }

    public ExceptionType type;

    public CensusAnalyserException(ExceptionType type) {
        this.type = type;
    }
    public CensusAnalyserException(String message, ExceptionType type) {
        super(message);
        this.type = type;
    }
    public CensusAnalyserException(String message, String name) {
        super(message);
        this.type = ExceptionType.valueOf(name);
    }

}
