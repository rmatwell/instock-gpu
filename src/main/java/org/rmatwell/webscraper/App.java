package org.rmatwell.webscraper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.rmatwell.instock.gpu.domains.Listing;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.rmatwell.webscraper.utils.DateUtils.dateTodayToString;

/**
 * @author Richard Atwell
 */
public class App {

    private final String DATE = dateTodayToString();

    public void runNow() throws IOException {
        List<Listing> listings = new ArrayList<>();

        MCListingService mcListingService = new MCListingService();
        mcListingService.getListings(listings);

        BBListingService bbListingService = new BBListingService();
        bbListingService.getListings(listings);

        postJSONtoAPI(listings);

        CSVReport report = new CSVReport("LISTINGS_" + DATE + ".csv");
        report.writeListingsToCSV(listings);
    }

    private void postJSONtoAPI(List<Listing> listings){
        ObjectMapper mapper = new ObjectMapper();

        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File("LISTINGS_" +
                            DATE + ".json"), listings);

            String JSON = mapper.writeValueAsString(listings);

            URL url = new URL("http://localhost:8080/api/add-listings");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            os.write(JSON.getBytes());
            os.flush();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

}
