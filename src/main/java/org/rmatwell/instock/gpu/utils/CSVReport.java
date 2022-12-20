package org.rmatwell.instock.gpu.utils;

import org.rmatwell.instock.gpu.domains.Listing;

import java.io.PrintWriter;
import java.util.List;


/**
 * @author Richard Atwell
 */

public class CSVReport {

    private String fileName;

    public CSVReport(String fileName) {   this.fileName = fileName;   }

    public void writeListingsToCSV(List<Listing> listings){

        try {
            PrintWriter csv = new PrintWriter(fileName);
            listings.forEach(csv::println);
            csv.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {   this.fileName = fileName;   }

}
