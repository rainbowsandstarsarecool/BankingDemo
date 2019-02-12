package net.ukr.just_void;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "transactions")
@DiscriminatorValue("Transfer")
public class TransferTransaction extends Transaction {
    @Column(name = "to_account_id")
    private long toAccountId;

    public TransferTransaction() {
        super(TransactionType.TRANSFER);
    }

    public TransferTransaction(Date date, long accountId, CurrencyType сurrency, double amount, BankManager bm, long toAccountId) {
        super(date, TransactionType.TRANSFER, accountId, сurrency, amount, bm);
        this.toAccountId = toAccountId;
        new Thread(this).start();
    }

    public long getToAccountId() {
        return toAccountId;
    }

    @Override
    public String toString() {
        return "#" + getId() + "\t" + getDate() + "\t" + getType().name() + "\tFrom: " + getAccountId() + "\t"
                + getAmount() + "\t" + getСurrencyType().name() + "\tTo: " + toAccountId + "\t" + (isComplete() ? "Complete" : "Not Complete");
    }

}
