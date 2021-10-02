package com.assessment.mercedesreceiver.services;

import org.springframework.beans.factory.annotation.Value;

import java.io.File;

public class CSVFileCreator implements Cloneable {
    private static File csvFile;
    @Value("${csvpath}")
    private static String csvFilePath;
    private CSVFileCreator(){
    }
    public static File getInstance(){
        if(csvFile != null)
            csvFile = new File(csvFilePath);
        return csvFile;
    }
    @Override
    protected Object clone() throws CloneNotSupportedException
    {
        throw new CloneNotSupportedException();
    }
}
