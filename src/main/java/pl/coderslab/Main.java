package pl.coderslab;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

public class Main {

    final static String WEBSITE_URL = "https://www.infoworld.com/category/java/";

    public static void main(String[] args) {
        ArrayList<Element> links = getLinks();
        links.forEach(System.out::println);
    }

    public static ArrayList<Element> getLinks() {
        Document doc = null;
        try {
            doc = Jsoup.connect(WEBSITE_URL).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc.select("div.article h3 a");
    }

}
