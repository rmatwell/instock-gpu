package org.rmatwell.instock.gpu.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.rmatwell.instock.gpu.domains.Listing;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Richard Atwell
 */

@Service
public class BBListingService {

    private final String API_URL = "https://api.bestbuy.com/v1/" +
            "products(categoryPath.id=abcat0507002&onlineAvailability=true&condition=new&condition=new)?" +
            "apiKey=VTQP2JV9TFF3On8xgXdhAPp5" +
            "&sort=salePrice.dsc" +
            "&show=modelNumber,manufacturer,salePrice,sku,upc,details.name,image" +
            "&pageSize=100" +
            "&format=json";

    private final String ROOT_URL = "https://www.bestbuy.com/site/";

    public void getListings(List<Listing> listings) {

        URL url;

        {
            try {
                url = new URL(API_URL);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestProperty("Content-Type", "application/json");
                con.setDoOutput(true);
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                final ObjectNode node = new ObjectMapper()
                        .readValue(br.readLine(), ObjectNode.class);

                String products = node
                        .get("products")
                        .toPrettyString();

                ObjectMapper productsMapper = new ObjectMapper();
                List<HashMap<String, Object>> prodList = productsMapper.readValue(products, List.class);

                List<Listing> myList = prodList.stream().map(results -> {

                            List<HashMap<String, Object>> details =
                                    (List<HashMap<String, Object>>) results.get("details");

                            if (isPresent("Graphics Processing Unit (GPU)", details)) {
                                String model, brand, chipSet, link, image;
                                double price;

                                //Ignoring Redundant Inspection - OptionalGetWithoutIsPresent
                                chipSet = details.stream()
                                        .filter(name -> name.containsValue("Graphics Processing Unit (GPU)"))
                                        .findFirst()
                                        .get()
                                        .get("value")
                                        .toString();

                                model = results.get("modelNumber").toString();
                                brand = results.get("manufacturer").toString();
                                link = ROOT_URL + results.get("sku") + ".p";
                                price = Double.parseDouble(results.get("salePrice").toString());
                                image = results.get("image").toString();

                                return new Listing(model,
                                        brand,
                                        price,
                                        chipSet,
                                        link,
                                        image);
                            }
                            else{ return new Listing(); }
                        })
                        .filter(listing -> !(listing.getModel() == null))
                        .collect(Collectors.toList());

                listings.addAll(myList);

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public boolean isPresent(String featureName, List<HashMap<String, Object>> details){
        return  details.stream()
                .anyMatch(entry -> details.stream()
                        .anyMatch(val -> val.containsValue(featureName)));
    }
}
