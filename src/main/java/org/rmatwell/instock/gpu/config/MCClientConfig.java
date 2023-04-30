package org.rmatwell.instock.gpu.config;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

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
                .defaultHeader("Cache-Control", " no-cache")
                .defaultHeader("User-Agent", " Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Mobile Safari/537.36")
                .defaultHeader("Accept", "*/*")
                .defaultHeader("Accept-Encoding", "gzip, deflate, br")
                .defaultHeader("Connections", "keep-alive")
                .build();
    }

    public int webClientStatus(String URL){
        return this.client
                .get()
                .uri(URL)
                .exchangeToMono(response -> Mono.just(response.statusCode()))
                .block()
                .value();
    }
    public String webClientResponse(String URL){
        return this.client
                .get()
                .uri(URL)
                .accept(MediaType.ALL)
                .retrieve()
                .bodyToMono(String.class)
                .retryWhen(Retry.fixedDelay(20, Duration.ofSeconds(3/2))
                        .filter(this::is4xxClientError)
                        .doBeforeRetry(System.out::println))
                .onErrorResume(WebClientResponseException.class,
                        ex -> ex.getRawStatusCode() == 403 ? Mono.empty()  : Mono.error(ex))
                .block();
    }
    private boolean is4xxClientError(Throwable throwable) {
        return throwable instanceof WebClientResponseException &&
                ((WebClientResponseException) throwable).getStatusCode().is4xxClientError();
    }

}
