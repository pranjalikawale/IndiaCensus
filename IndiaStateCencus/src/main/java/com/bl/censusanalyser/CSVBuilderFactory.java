package com.bl.censusanalyser;

public class CSVBuilderFactory {
    public static ICSVBuilder createCSVBuilder(){
        return new OpenCSVBuilder();
    }
}
