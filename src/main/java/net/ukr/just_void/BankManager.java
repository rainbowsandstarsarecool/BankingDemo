package net.ukr.just_void;

import javax.persistence.*;
import java.util.*;

import static net.ukr.just_void.Utils.*;

public class BankManager {
    private static EntityManagerFactory emf;
    private static EntityManager em;

    public BankManager(EntityManagerFactory emf) {
        this.emf = emf;
        initializeBankManager();
    }

    private void initializeBankManager() {
        em = emf.createEntityManager();
        refreshCurrencies();
        Timer currencyRefreshTimer = new Timer(true);
        currencyRefreshTimer.schedule(new TimerTask() {
            public void run() {
                refreshCurrencies();
            }
        }, 600000, 600000);
    }

    private void refreshCurrencies() {
        EntityManager emLocal = emf.createEntityManager();
        try {
            updateCurrencyRates();
            for (CurrencyType i : CurrencyType.values()) {
                emLocal.getTransaction().begin();
                try {
                    emLocal.merge(i.getCurrency());
                    emLocal.getTransaction().commit();
                } catch (Exception e) {
                    emLocal.getTransaction().rollback();
                }
            }
        } finally {
            emLocal.close();
        }
    }

    public void generateRandomClients() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the number of clients to generate:");
        int n = sc.nextInt();
        for (int i = 0; i < n; i++) {
            Client client;
            String name = randomizer("Bla", "La");
            for (int j = 0; j < rng.nextInt(2); j++) name += randomizer("bla", "la");
            name += randomizer(" Bla", " La");
            for (int j = 0; j < rng.nextInt(2); j++) name += "bla";
            name += randomizer("vich", "vko", "yev", "blenko");
            client = new Client(name);
            client.createAccount(CurrencyType.UAH);
            client.createAccount(CurrencyType.USD);
            client.createAccount(CurrencyType.EUR);
            addToDb(client);
        }
    }

    public void manualCreateAccount() {
        listClients();
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter the client name:");
            String clientName = sc.nextLine();
            System.out.println("Enter the currency type:");
            CurrencyType currencyType = CurrencyType.valueOf(sc.nextLine().toUpperCase());
            try {
                Client c = null;
                Query query = em.createQuery("SELECT c FROM Client c WHERE c.name = :name", Client.class);
                query.setParameter("name", clientName);
                c = (Client) query.getSingleResult();
                c.createAccount(currencyType);
                addToDb(c);
            } catch (NoResultException e) {
                System.out.println("ERROR! Client not found");
            } catch (NonUniqueResultException e) {
                System.out.println("ERROR! Found multiple clients");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR! Incorrect input");
        }
    }

    public void manualCreateChargeTransaction() {
        listAccounts();
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter an ID of the account you want to charge:");
            long accountId = Long.parseLong(sc.nextLine());
            System.out.println("Enter the charge sum:");
            double amount = Double.parseDouble(sc.nextLine());
            System.out.println("Enter the currency type:");
            CurrencyType currencyType = CurrencyType.valueOf(sc.nextLine().toUpperCase());
            new ChargeTransaction(new Date(), accountId, currencyType, amount, this);
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR! Incorrect input");
        }
    }

    public void manualCreateTransferTransaction() {
        listAccounts();
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter an ID of the account you want to transfer from:");
            long accountId = Long.parseLong(sc.nextLine());
            System.out.println("Enter an ID of the account you want to transfer to:");
            long toAccountId = Long.parseLong(sc.nextLine());
            System.out.println("Enter the transfer sum:");
            double amount = Double.parseDouble(sc.nextLine());
            System.out.println("Enter the currency type:");
            CurrencyType currencyType = CurrencyType.valueOf(sc.nextLine().toUpperCase());
            new TransferTransaction(new Date(), accountId, currencyType, amount, this, toAccountId);
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR! Incorrect input");
        }
    }

    public void manualCreateExchangeTransaction() {
        listAccounts();
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter an ID of the account you want to exchange from:");
            long accountId = Long.parseLong(sc.nextLine());
            System.out.println("Enter an ID of the account you want to exchange to:");
            long toAccountId = Long.parseLong(sc.nextLine());
            System.out.println("Enter the currency type you wish to exchange from:");
            CurrencyType currencyType = CurrencyType.valueOf(sc.nextLine().toUpperCase());
            System.out.println("Enter the currency type you wish to exchange to:");
            CurrencyType toCurrencyType = CurrencyType.valueOf(sc.nextLine().toUpperCase());
            System.out.println("Enter the sum you want to exchange:");
            double amount = Double.parseDouble(sc.nextLine());
            new ExchangeTransaction(new Date(), accountId, currencyType, amount, this, toAccountId, toCurrencyType);
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR! Incorrect input");
        }
    }

    private <T> void addToDb(T object) {
        em.getTransaction().begin();
        try {
            em.persist(object);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        }
    }

    public void listClients() {
        Query query = em.createQuery("SELECT c FROM Client c");
        List<Client> clients = (List<Client>) query.getResultList();
        System.out.println("============ Clients ============");
        for (Client client : clients) {
            System.out.println(client);
        }
    }

    public void listAccounts() {
        Query query = em.createQuery("SELECT a FROM Account a");
        List<Account> accounts = (List<Account>) query.getResultList();
        System.out.println("============ Accounts ============");
        for (Account account : accounts) {
            System.out.println(account);
        }
    }

    public void listCurrencies() {
        Query query = em.createQuery("SELECT c FROM Currency c");
        List<Currency> currencies = (List<Currency>) query.getResultList();
        System.out.println("============ Currencies ============");
        for (Currency currency : currencies) {
            if (!currency.getCurrencyType().equals(CurrencyType.UAH)) {
                System.out.println(currency);
            }
        }
    }

    public void listTransactions() {
        Query query = em.createQuery("SELECT t FROM Transaction t");
        List<Transaction> transactions = (List<Transaction>) query.getResultList();
        System.out.println("============ Currencies ============");
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
    }

    public synchronized <T extends Transaction> void executeTransaction(T transaction) {
        if (transaction.isComplete()) {
            System.out.println("ERROR! This transaction is already complete");
            return;
        }
        addToDb(transaction);
        boolean result = false;
        try {
            switch (transaction.getType()) {
                case CHARGE: {
                    result = processChargeTransaction((ChargeTransaction) transaction);
                    break;
                }
                case EXCHANGE: {
                    result = processExchangeTransaction((ExchangeTransaction) transaction);
                    break;
                }
                case TRANSFER: {
                    result = processTransferTransaction((TransferTransaction) transaction);
                    break;
                }
            }
            if (result) {
                transaction.setAsComplete();
            }
        } catch (TransactionException e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean processChargeTransaction(ChargeTransaction transaction) throws TransactionException {
        Account account = null;
        if (transaction.getAmount() <= 0) {
            throw new TransactionException("ERROR! Invalid transaction sum");
        }
        try {
            Query query = em.createQuery("SELECT a FROM Account a WHERE a.id = :id", Account.class);
            query.setParameter("id", transaction.getAccountId());
            account = (Account) query.getSingleResult();
        } catch (NoResultException e) {
            throw new TransactionException("ERROR! Account not found");
        } catch (NonUniqueResultException e) {
            throw new TransactionException("ERROR! Invalid account");
        }
        if (!account.getCurrencyType().equals(transaction.getСurrencyType())) {
            throw new TransactionException("ERROR! Account currency and transaction currency do not match");
        }
        em.getTransaction().begin();
        try {
            account.setBalance(account.getBalance() + transaction.getAmount());
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new TransactionException("ERROR! Database Error");
        }
        return true;
    }

    private boolean processTransferTransaction(TransferTransaction transaction) throws TransactionException {
        Account fromAccount = null, toAccount = null;
        if (transaction.getAmount() <= 0) {
            throw new TransactionException("ERROR! Invalid transaction sum");
        }
        try {
            Query query = em.createQuery("SELECT a FROM Account a WHERE a.id = :id", Account.class);
            query.setParameter("id", transaction.getAccountId());
            fromAccount = (Account) query.getSingleResult();
            query = em.createQuery("SELECT a FROM Account a WHERE a.id = :id", Account.class);
            query.setParameter("id", transaction.getToAccountId());
            toAccount = (Account) query.getSingleResult();
        } catch (NoResultException e) {
            throw new TransactionException("ERROR! Account not found");
        } catch (NonUniqueResultException e) {
            throw new TransactionException("ERROR! Invalid account");
        }
        if (!fromAccount.getCurrencyType().equals(transaction.getСurrencyType()) || !toAccount.getCurrencyType()
                .equals(transaction.getСurrencyType())) {
            throw new TransactionException("ERROR! Transaction currency does not match account currency");
        }
        if (fromAccount.getBalance() < transaction.getAmount()) {
            throw new TransactionException("ERROR! Not enough funds");
        }
        em.getTransaction().begin();
        try {
            fromAccount.setBalance(fromAccount.getBalance() - transaction.getAmount());
            toAccount.setBalance(toAccount.getBalance() + transaction.getAmount());
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new TransactionException("ERROR! Database Error");
        }
        return true;
    }

    private boolean processExchangeTransaction(ExchangeTransaction transaction) throws TransactionException {
        refreshCurrencies();
        Account fromAccount = null, toAccount = null;
        if (transaction.getAmount() <= 0) {
            throw new TransactionException("ERROR! Invalid transaction sum");
        }
        if (transaction.getСurrencyType().equals(transaction.getToCurrencyType())) {
            throw new TransactionException("ERROR! Source currency is the same as the resulting currency");
        }
        try {
            Query query = em.createQuery("SELECT a FROM Account a WHERE a.id = :id", Account.class);
            query.setParameter("id", transaction.getAccountId());
            fromAccount = (Account) query.getSingleResult();
            query = em.createQuery("SELECT a FROM Account a WHERE a.id = :id", Account.class);
            query.setParameter("id", transaction.getToAccountId());
            toAccount = (Account) query.getSingleResult();
        } catch (NoResultException e) {
            throw new TransactionException("ERROR! Account not found");
        } catch (NonUniqueResultException e) {
            throw new TransactionException("ERROR! Invalid account");
        }
        if (fromAccount.getClient().getId() != toAccount.getClient().getId()) {
            throw new TransactionException("ERROR! Can exchange money only between same client's accounts");
        }
        if (!fromAccount.getCurrencyType().equals(transaction.getСurrencyType()) || !toAccount.getCurrencyType()
                .equals(transaction.getToCurrencyType())) {
            throw new TransactionException("ERROR! Transaction currency does not match account currency");
        }
        if (fromAccount.getBalance() < transaction.getAmount()) {
            throw new TransactionException("ERROR! Not enough funds");
        }
        em.getTransaction().begin();
        try {
            fromAccount.setBalance(fromAccount.getBalance() - transaction.getAmount());
            toAccount.setBalance(toAccount.getBalance() + transaction.getToAmount());
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new TransactionException("ERROR! Database Error");
        }
        return true;
    }

    public void showAllFundsForClient() {
        refreshCurrencies();
        listClients();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the client name:");
        String clientName = sc.nextLine();
        try {
            double totalFunds = 0;
            Client c = null;
            Query query = em.createQuery("SELECT c FROM Client c WHERE c.name = :name", Client.class);
            query.setParameter("name", clientName);
            c = (Client) query.getSingleResult();
            System.out.println("Funds on client's account:");
            for (Account account : c.getAccounts()) {
                System.out.println(account.getBalance() + "\t" + account.getCurrencyType().name());
                totalFunds += account.getBalance() * account.getCurrencyType().getCurrency().getSell();
            }
            System.out.println("Total sum in UAH: " + totalFunds);
        } catch (NoResultException e) {
            System.out.println("ERROR! Client not found");
        } catch (NonUniqueResultException e) {
            System.out.println("ERROR! Found multiple clients");
        }
    }
}
