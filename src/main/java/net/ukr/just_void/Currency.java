package net.ukr.just_void;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "currencies")
public class Currency {

    @Id
    private CurrencyType currencyType;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "buy_price")
    private Double buy;

    @Column(name = "sell")
    private Double sell;

    public Currency() {
    }

    public Currency(CurrencyType currencyType) {
        this.currencyType = currencyType;
        this.name = currencyType.name();
    }

    public Currency(CurrencyType currencyType, Double buy, Double sell) {
        this.currencyType = currencyType;
        this.name = currencyType.name();
        this.buy = buy;
        this.sell = sell;
    }

    public Double getBuy() {
        return buy;
    }

    public void setBuy(Double buy) {
        this.buy = buy;
    }

    public CurrencyType getCurrencyType() {
        return currencyType;
    }

    public String getName() {
        return name;
    }

    public Double getSell() {
        return sell;
    }

    public void setSell(Double sell) {
        this.sell = sell;
    }

    @Override
    public String toString() {
        return name + "\t" + buy + "\t" + sell;
    }
}
