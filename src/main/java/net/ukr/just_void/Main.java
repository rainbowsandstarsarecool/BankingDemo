package net.ukr.just_void;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Scanner;

public class Main {
    static EntityManagerFactory emf;
    static BankManager bm;

    public static void main(String[] args) {
        emf = Persistence.createEntityManagerFactory("Lesson06_HW_2_2");
        bm = new BankManager(emf);
        try {
            menu();
        } finally {
            emf.close();
        }
    }

    private static void menu() {
        Scanner sc = new Scanner(System.in);
        String input;
        do {
            System.out.println("======= Main Menu ======");
            System.out.println("\t1. Generate random clients with accounts");
            System.out.println("\t2. Create account");
            System.out.println("\t3. Charge account");
            System.out.println("\t4. Transfer money");
            System.out.println("\t5. Exchange money");
            System.out.println("\t6. Get a total sum on all client's accounts");
            System.out.println("\t7. Show all clients");
            System.out.println("\t8. Show all accounts");
            System.out.println("\t9. Show currencies");
            System.out.println("\t0. Show transactions");
            System.out.println("\tx. Exit");
            input = sc.nextLine();
            switch (input) {
                case "1": {
                    bm.generateRandomClients();
                    break;
                }
                case "2": {
                    bm.manualCreateAccount();
                    break;
                }
                case "3": {
                    bm.manualCreateChargeTransaction();
                    break;
                }
                case "4": {
                    bm.manualCreateTransferTransaction();
                    break;
                }
                case "5": {
                    bm.manualCreateExchangeTransaction();
                    break;
                }
                case "6": {
                    bm.showAllFundsForClient();
                    break;
                }
                case "7": {
                    bm.listClients();
                    break;
                }
                case "8": {
                    bm.listAccounts();
                    break;
                }
                case "9": {
                    bm.listCurrencies();
                    break;
                }
                case "0": {
                    bm.listTransactions();
                    break;
                }

            }
        } while (!input.equalsIgnoreCase("x"));
    }
}