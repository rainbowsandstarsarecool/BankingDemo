package net.ukr.just_void;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "transactions")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "discriminator", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("Transaction")
public abstract class Transaction implements Runnable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "type", nullable = false)
    private TransactionType type;

    @Column(name = "account_id", nullable = false)
    private long accountId;

    @Column(name = "currency", nullable = false)
    private CurrencyType сurrencyType;

    @Column(name = "amount", nullable = false)
    private double amount;

    @Column(name = "complete", nullable = false)
    private boolean complete;

    @Transient
    private BankManager bm;

    public Transaction() {
        complete = false;
    }

    protected Transaction(TransactionType type) {
        this.type = type;
        complete = false;
    }

    public Transaction(Date date, TransactionType type, long accountId, CurrencyType сurrencyType, double amount, BankManager bm) {
        this.date = date;
        this.type = type;
        this.accountId = accountId;
        this.сurrencyType = сurrencyType;
        this.amount = amount;
        this.bm = bm;
    }

    public void setAsComplete() {
        complete = true;
    }

    public long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public TransactionType getType() {
        return type;
    }

    public long getAccountId() {
        return accountId;
    }

    public CurrencyType getСurrencyType() {
        return сurrencyType;
    }

    public double getAmount() {
        return amount;
    }

    public boolean isComplete() {
        return complete;
    }

    public BankManager getBm() {
        return bm;
    }

    @Override
    public void run() {
        bm.executeTransaction(this);
    }
}
