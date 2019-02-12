package net.ukr.just_void;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "name", nullable = false)
    private String name;
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Account> accounts = new ArrayList<>();

    public Client() {
    }

    public Client(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void createAccount(CurrencyType currencyType) {
        Account account = new Account(this, currencyType);
        accounts.add(account);
    }

    @Override
    public String toString() {
        return id + "\t" + name;
    }
}
