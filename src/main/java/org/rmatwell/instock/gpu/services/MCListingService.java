package org.rmatwell.instock.gpu.services;

import org.rmatwell.instock.gpu.domains.Listing;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * @author Richard Atwell
 */
@Service
public class MCListingService {

    public void getListings(List<Listing> listings) throws IOException {

        final String BASE_URL = "https://www.microcenter.com";
        final String url = BASE_URL
                        + "/search/search_results.aspx?"
                        + "N=4294966937"
                        + "&sortby=pricehigh"
                        + "&NTK=all"
                        + "&storeid=081"
                        + "&page=";

        int totalPages = 1, pageIndex = 1;

        boolean arePagesChecked = false;

            while(totalPages >= pageIndex){
                String resultsPageURL = url + pageIndex++;

                Document document = null;
                try {
                    document = Jsoup.connect(resultsPageURL)
                            .followRedirects(true)
                            .userAgent("Mozilla")
                            .referrer("https://www.google.com")
                            .cookie("auth", "token")
                            .timeout(10000)
                            .get();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(!arePagesChecked){
                    String pagingNumber = Objects.requireNonNull(document)
                            .select("p.status")
                            .text();
                    totalPages = getNumberOfPages(pagingNumber);

                    arePagesChecked = true;
                }

                for(Element listing: Objects.requireNonNull(document)
                                            .select("li.product_wrapper")){

                    Element priceLink = listing.select("a[class^=image]").first();
                    assert priceLink != null;
                    if(priceLink.attr("data-price").isEmpty()){  continue;  }

                    String brand = listing
                            .select("a[class^=image]")
                            .attr("data-brand");
                    double price = Double.parseDouble(priceLink.attr("data-price"));
                    String relativeURL = listing
                            .select("a[class^=image]")
                            .attr("href");
                    String image = listing
                            .select("img.SearchResultProductImage")
                            .attr("src");

                    String listingPageURL = BASE_URL + relativeURL;
                    final Document listingPageDoc;
                    listingPageDoc = Jsoup.connect(listingPageURL)
                            .followRedirects(true)
                            .userAgent("Mozilla")
                            .referrer("https://www.google.com")
                            .cookie("auth", "token")
                            .timeout(10000)
                            .get()
                            .body()
                            .ownerDocument();

                    if (listingPageDoc == null) throw new AssertionError();
                    String partNum = listingPageDoc.select("div:containsOwn(Mfr) + div").text();
                    String chipSet = listingPageDoc.select("div:containsOwn(GPU Chipset) + div").text();
                    Listing gpuListing = new Listing(partNum
                                                    , brand
                                                    , price
                                                    , chipSet
                                                    , listingPageURL
                                                    , image);
                    listings.add(gpuListing);
                    listingPageDoc.empty();
                }
                document.empty();
            }
    }

        public int getNumberOfPages(String pagingNumber){

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
}
