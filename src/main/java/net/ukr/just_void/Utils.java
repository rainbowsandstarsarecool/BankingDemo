package net.ukr.just_void;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

public class Utils {
    static Random rng = new Random();

    public static String randomizer(String... args) {
        return args[rng.nextInt(args.length)];
    }

    public static void updateCurrencyRates() {
        CurrencyJsonElement[] cje = getJson("https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json");
        CurrencyType.UAH.getCurrency().setBuy(1.0);
        CurrencyType.UAH.getCurrency().setSell(1.0);
        for (CurrencyJsonElement i : cje) {
            for (CurrencyType j : CurrencyType.values()) {
                if (i.getCc().equalsIgnoreCase(j.toString())) {
                    j.getCurrency().setSell(i.getRate());
                    j.getCurrency().setBuy(i.getRate());
                }
            }
        }
    }

    public static CurrencyJsonElement[] getJson(String urlString) {
        String json = readStringFromURL(urlString);
        Gson gson = new GsonBuilder().create();
        CurrencyJsonElement[] cje = gson.fromJson(json, CurrencyJsonElement[].class);
        return cje;
    }

    private static String readStringFromURL(String urlString) {
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlString);
            URLConnection request = url.openConnection();
            request.connect();
            String str;
            BufferedReader br = new BufferedReader(new InputStreamReader((InputStream) request.getContent()));
            while ((str = br.readLine()) != null) {
                sb.append(str).append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}
