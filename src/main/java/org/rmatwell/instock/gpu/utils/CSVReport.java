package org.rmatwell.instock.gpu.utils;

import org.rmatwell.instock.gpu.domain.Listing;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author Richard Atwell
 */

public class CSVReport {

    public static ByteArrayInputStream writeListingsToCSV(List<Listing> listings) {

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PrintWriter csv = new PrintWriter(outputStream);){

            listings.forEach(csv::println);
            csv.close();
            outputStream.close();
            return new ByteArrayInputStream(outputStream.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("Failed to import data to CSV file: " + e.getMessage());
        }
    }
}
