package org.rmatwell.instock.gpu.domains;

import lombok.*;
import org.rmatwell.instock.gpu.utils.DateUtils;

import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;


/**
 * @author Richard Atwell
 */
@Entity
@Table(name = "listings")
@NoArgsConstructor
@Setter @Getter
public class Listing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "model")
    private String model;
    @Column(name = "gpu")
    private String chipSet;
    @Column(name = "price")
    private double price;
    @Column(name = "brand")
    private String brand;
    @Column(name = "url")
    private String url;
    @Column(name = "date")
    private String date;
    @Column(name = "image")
    private String image;

    public Listing(String model, String brand, double price, String chipSet, String url, String image) {
        this.model = model;
        this.chipSet = trimChipset(chipSet);
        this.price = price;
        this.brand = brand;
        this.url = url;
        this.date = DateUtils.dateTodayToString();
        this.image = image;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        appendVal(sb, model);
        appendVal(sb, chipSet);
        appendVal(sb, String.valueOf(price));
        appendVal(sb, brand);
        appendVal(sb, url);
        appendVal(sb, image);

        return sb.toString();
    }

    private void appendVal(StringBuilder sb, String value) {
        if(value != null) { sb.append(value).append(","); }
        else { sb.append(","); }
    }

    private String trimChipset(String chipSet) {
        if(chipSet.contains("NVI")) { return chipSet.substring(7); }
        else if(chipSet.contains("AMD")) { return chipSet.substring(4); }
        else { return chipSet; }
    }

}
