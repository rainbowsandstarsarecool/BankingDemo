package net.ukr.just_void;

public enum CurrencyType {
    UAH, USD, EUR;

    private Currency currency;

    private CurrencyType() {
        this.currency = new Currency(this);
    }

    public Currency getCurrency() {
        return currency;
    }
}
