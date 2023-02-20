package org.rmatwell.instock.gpu.config;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component("mcWebClient")
public class MCClientConfig {
    final private String BASE_URL;
    final private String CLIENT_QUERY;
    final private WebClient client;
    public MCClientConfig(){
        BASE_URL  = "https://www.microcenter.com";
        CLIENT_QUERY =
                "/search/search_results.aspx?"
                        + "N=4294966937"
                        + "&sortby=pricehigh"
                        + "&NTK=all"
                        + "&storeid=081"
                        + "&page=";
        client = webClient(WebClient.builder());
    }
    public String getBASE_URL() {
        return BASE_URL;
    }

    public String getCLIENT_QUERY() {
        return CLIENT_QUERY;
    }

    private WebClient webClient(final WebClient.Builder webclientBuilder){
        return webclientBuilder
                .codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(-1))
                .baseUrl(BASE_URL)
                .build();
    }
    public String webClientResponse(String URL){
        return this.client.get()
                .uri(URL)
                .accept(MediaType.TEXT_HTML)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
