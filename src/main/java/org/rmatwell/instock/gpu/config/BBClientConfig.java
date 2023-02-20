package org.rmatwell.instock.gpu.config;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component("bbWebClient")
public class BBClientConfig {
    final private String API_BASE_URL;
    final private String CLIENT_QUERY;
    final private WebClient client;

    public BBClientConfig(){
        this.API_BASE_URL = "https://api.bestbuy.com/v1/";
        this.CLIENT_QUERY = "products(categoryPath.id=abcat0507002&onlineAvailability=true&condition=new)?" +
                "apiKey=VTQP2JV9TFF3On8xgXdhAPp5" +
                "&sort=salePrice.dsc" +
                "&show=modelNumber,manufacturer,salePrice,sku,upc,details.name,image" +
                "&pageSize=100" +
                "&format=json";
        this.client = webClient(WebClient.builder());
    }

    public String webClientResponse(){
        return this.client.get()
                .uri(CLIENT_QUERY)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
    private WebClient webClient(final WebClient.Builder webclientBuilder){
        return webclientBuilder
                .baseUrl(API_BASE_URL)
                .build();
    }
}