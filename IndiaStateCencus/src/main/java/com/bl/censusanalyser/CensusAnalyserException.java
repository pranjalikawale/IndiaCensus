package com.bl.censusanalyser;

public class CensusAnalyserException extends Exception {

    enum ExceptionType {
        CENSUS_FILE_PROBLEM,UNABLE_TO_PARSE,NO_SUCH_CLASS_TYPE,WRONG_DELIMETER,WRONG_HEADER;
    }

    ExceptionType type;

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
    public CensusAnalyserException(String message, ExceptionType type, Throwable cause) {
        super(message, cause);
        this.type = type;
    }
}
