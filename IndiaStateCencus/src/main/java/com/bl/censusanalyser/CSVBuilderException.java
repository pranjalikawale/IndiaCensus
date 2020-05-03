package com.bl.censusanalyser;

public class CSVBuilderException extends Exception {
    enum ExceptionType {
        UNABLE_TO_PARSE;
    }

    ExceptionType type;

    public CSVBuilderException(String message, ExceptionType type) {
        super(message);
        this.type = type;
    }
}
