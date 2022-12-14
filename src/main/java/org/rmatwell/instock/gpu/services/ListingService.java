package org.rmatwell.instock.gpu.services;

import org.rmatwell.instock.gpu.domains.Listing;
import org.rmatwell.instock.gpu.repositories.ListingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author Richard Atwell
 */
@Component
public class ListingService{

    @Autowired
    private final ListingRepository repository;

    public ListingService(ListingRepository repository){
        this.repository = repository;
    }

    public void add(Listing listing){
        repository.save(listing);
    }

    public void addListings(List<Listing> list) { repository.saveAll(list); }

    public void delete(int id) {
        repository.deleteById(id);
    }

    public List<Listing> getListings() { return (repository.findAll()); }

    public Listing getListingById(int id) throws Exception {
        Optional<Listing> optionalListing = repository.findById(id);
        return optionalListing.orElseThrow(ChangeSetPersister.NotFoundException::new);
    }

    public Listing findFirstByOrderByPriceAsc(){
        return repository.findFirstByOrderByPriceAsc();
    }

    public List<Object[]>  findMinPricesByModel(){
        return repository.findMinPricesByModel();
    }
}
