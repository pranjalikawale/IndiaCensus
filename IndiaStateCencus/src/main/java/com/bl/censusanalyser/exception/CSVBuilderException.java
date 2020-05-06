package com.bl.censusanalyser.exception;

public class CSVBuilderException extends Exception {
    public enum ExceptionType {
        UNABLE_TO_PARSE;
    }

    public ExceptionType type;

    public CSVBuilderException(String message, ExceptionType type) {
        super(message);
        this.type = type;
    }
}
