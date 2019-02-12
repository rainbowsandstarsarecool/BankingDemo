package net.ukr.just_void;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "transactions")
@DiscriminatorValue("Exchange")
public class ExchangeTransaction extends Transaction {

    @Column(name = "to_account_id")
    private long toAccountId;

    @Column(name = "to_currency")
    private CurrencyType toCurrencyType;

    @Column(name = "to_amount")
    private double toAmount;

    public ExchangeTransaction() {
        super(TransactionType.EXCHANGE);
    }

    public ExchangeTransaction(Date date, long accountId, CurrencyType сurrency, double amount, BankManager bm,
                               long toAccountId, CurrencyType toCurrencyType) {
        super(date, TransactionType.EXCHANGE, accountId, сurrency, amount, bm);
        this.toAccountId = toAccountId;
        this.toCurrencyType = toCurrencyType;
        this.toAmount = getAmount() * getСurrencyType().getCurrency().getSell() / getToCurrencyType().getCurrency().getBuy();
        new Thread(this).start();
    }

    public long getToAccountId() {
        return toAccountId;
    }

    public CurrencyType getToCurrencyType() {
        return toCurrencyType;
    }

    public double getToAmount() {
        return toAmount;
    }

    @Override
    public String toString() {
        return "#" + getId() + "\t" + getDate() + "\t" + getType().name() + "\tFrom: " + getAccountId() + "\t"
                + getAmount() + "\t" + getСurrencyType().name() + "\tTo: " + toAccountId + "\t" + toAmount + "\t"
                + toCurrencyType.name() + "\t" + (isComplete()?"Complete":"Not Complete");
    }
}
