package org.rmatwell.instock.gpu.repositories;

import org.rmatwell.instock.gpu.domains.Listing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author Richard Atwell
 */
@Repository
public interface ListingRepository extends JpaRepository<Listing, Integer> {

    Listing findFirstByOrderByPriceAsc();

    @Query( value = "WITH cte AS" +
            " (SELECT model, gpu, price, date, " +
            " RANK() OVER ( PARTITION BY gpu " +
            " ORDER BY price ASC, gpu ASC, model)" +
            " AS r FROM listings) " +
            "SELECT model, gpu, price, date " +
            "FROM cte " +
            "WHERE r = 1 and date = (select max(date) from listings) " +
            "ORDER BY gpu ASC"
            , nativeQuery = true)
    List<Object[]> findMinPricesByModel();

}
