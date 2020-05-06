package com.bl.censusanalyser.opencsv;

public class CSVBuilderFactory {
    public static ICSVBuilder createCSVBuilder(){
        return new OpenCSVBuilder();
    }
}
