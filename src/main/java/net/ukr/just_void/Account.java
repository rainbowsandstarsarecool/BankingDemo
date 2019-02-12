package net.ukr.just_void;

import javax.persistence.*;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(name = "currencyType", nullable = false)
    private CurrencyType currencyType;

    @Column(name = "balance", nullable = false)
    private double balance;

    public Account() {
    }

    public Account(Client client, CurrencyType currencyType) {
        this.client = client;
        this.currencyType = currencyType;
        this.balance = 0;
    }

    public long getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public CurrencyType getCurrencyType() {
        return currencyType;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "#" + id + "\t" + client + "\t" + balance + "\t" + currencyType.name();
    }
}
