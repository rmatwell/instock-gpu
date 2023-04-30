package org.rmatwell.instock.gpu.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.rmatwell.instock.gpu.config.BBClientConfig;
import org.rmatwell.instock.gpu.config.MCClientConfig;
import org.rmatwell.instock.gpu.domain.Listing;
import org.rmatwell.instock.gpu.repository.ListingRepository;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;
import static org.rmatwell.instock.gpu.utils.CSVReport.writeListingsToCSV;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Richard Atwell
 */
@Component
public class ListingService{
    @Autowired
    private final ListingRepository listingRepository;
    @Autowired
    private final MCClientConfig mcClientConfig;
    @Autowired
    private final BBClientConfig bbClientConfig;

    public ListingService(
            ListingRepository listingRepository,
            MCClientConfig mcClientConfig,
            BBClientConfig bbClientConfig){
        this.listingRepository = listingRepository;
        this.mcClientConfig = mcClientConfig;
        this.bbClientConfig = bbClientConfig;
    }
    public void addListings(List<Listing> list) { listingRepository.saveAll(list); }
    public List<Listing> getListings() { return (listingRepository.findAll()); }

    public List<Listing> getListingsByMostRecentDate(){
        return listingRepository.findListingsByMostRecentDate();
    }

    public List<Listing> getListingsByModel(String model) throws Exception {
        return listingRepository
                .findAllByModel(model)
                .stream()
                .collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparing(Listing::getDate))),
                            ArrayList::new));git s
    }
    public Listing getListingById(int id) throws Exception {
        Optional<Listing> optionalListing = listingRepository.findById(id);
        return optionalListing.orElseThrow(ChangeSetPersister.NotFoundException::new);
    }
    public Listing findFirstByOrderByPriceAsc(){
        return listingRepository.findFirstByOrderByPriceAsc();
    }
    public List<Object[]>  findMinPricesByModel(){
        return listingRepository.findMinPricesByModel();
    }

    public void getMCListings(){
        List<Elements> elements = new ArrayList<>();
        int totalPages = 1, pageIndex = 1;

        boolean arePagesChecked = false;

        while(totalPages >= pageIndex) {

            String currentPageURL = mcClientConfig.getCLIENT_QUERY() + pageIndex++;
            System.out.println(currentPageURL);

            String response = mcClientConfig.webClientResponse(currentPageURL);

            if (response != null) {
                Document document = Jsoup.parse(Objects.requireNonNull(response));

                if (!arePagesChecked) {
                    String pagingNumber = Objects.requireNonNull(document)
                            .select("p.status")
                            .text();
                    totalPages = getNumberOfPages(pagingNumber);
                    arePagesChecked = true;
                }
                elements.addAll(extractTagValues(document));
                document.empty();
            }
        }
            addListings(getListingData(elements));
            }


    public List<Listing> getListingData(List<Elements> elements){
        return elements.stream().map(element-> {
                    String brand = element.attr("data-brand");
                    double price = Double.parseDouble(element.attr("data-price"));
                    String relativeURL = element.attr("href");
                    System.out.println(relativeURL);
                    String image = element.select("img.SearchResultProductImage").attr("src");

                        String listingPageResponse = mcClientConfig.webClientResponse(relativeURL);

                            Document listingPageDoc = Jsoup.parse(listingPageResponse);

                            String partNum = Objects.requireNonNull(listingPageDoc)
                                    .select("div:containsOwn(Mfr) + div").text();
                            String chipSet = listingPageDoc
                                    .select("div:containsOwn(GPU Chipset) + div").text();

                            return new Listing(partNum,
                                    brand,
                                    price,
                                    chipSet,
                                    (mcClientConfig.getBASE_URL() + relativeURL),
                                    image);
        }).collect(Collectors.toList());
    }

    private List<Elements> extractTagValues(Document document) {
        return  document.select("li.product_wrapper")
                .stream()
                .map(e->e.select("a[class^=image]"))
                .collect(Collectors.toList());
    }
    private int getNumberOfPages(String pagingNumber){
        final int ITEMS_PER_PAGE = 24;
        int pages = 1;
        int leftIndex = pagingNumber.indexOf("of") + 3, rightIndex = leftIndex;

        while(!Character.isWhitespace(pagingNumber.charAt(rightIndex)))
            rightIndex++;

        String totalNumberOfItems = pagingNumber.substring(leftIndex,rightIndex);

        int items = Integer.parseInt(totalNumberOfItems);

        if(ITEMS_PER_PAGE < items)
            pages = (int) Math.ceil((double)items / ITEMS_PER_PAGE);

        return pages;
    }

    public void getBBListings() throws JsonProcessingException {

        String ROOT_URL = "https://www.bestbuy.com/site/";
        String responseBody = bbClientConfig.webClientResponse();

        final JsonNode node = new ObjectMapper().readTree(responseBody);

        String products = node.get("products").toPrettyString();

        List<HashMap<String, Object>> prodList
                = new ObjectMapper().readValue(products, List.class);

        var myList = prodList.stream().map(results -> {

                    List<HashMap<String, Object>> details =
                            (List<HashMap<String, Object>>) results.get("details");

                    if (isGPUPresent(details)) {
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

                        return new Listing(
                                model,
                                brand,
                                price,
                                chipSet,
                                link,
                                image);
                    }
                    else { return new Listing(); }
                })
                .filter(listing -> !(listing.getModel() == null))
                .toList();

        addListings(myList);
    }
    private boolean isGPUPresent(List<HashMap<String, Object>> details){
        return  details.stream()
                .anyMatch(entry -> details.stream()
                        .anyMatch(val -> val.containsValue("Graphics Processing Unit (GPU)")));
    }

    public ByteArrayInputStream createCSVFile() {
        return writeListingsToCSV(listingRepository.findListingsByMostRecentDate());
    }

}
