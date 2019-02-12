package net.ukr.just_void;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "transactions")
@DiscriminatorValue("Charge")
public class ChargeTransaction extends Transaction {

    public ChargeTransaction() {
        super(TransactionType.CHARGE);
    }

    public ChargeTransaction(Date date, long accountId, CurrencyType сurrency, double amount, BankManager bm) {
        super(date, TransactionType.CHARGE, accountId, сurrency, amount, bm);
        new Thread(this).start();
    }

    @Override
    public String toString() {
        return "#" + getId() + "\t" + getDate() + "\t" + getType().name() + "\tAccount: " + getAccountId() + "\t" + getAmount()
                + "\t" + getСurrencyType().name() + "\t" + (isComplete()?"Complete":"Not Complete");
    }
}
