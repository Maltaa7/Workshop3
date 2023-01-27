package pl.coderslab;

import com.github.slugify.Slugify;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class Main {

    final static String MAIN_WEBSITE_URL = "https://www.infoworld.com";


    public static void main(String[] args) {
        saveToFile();
    }

    private static void saveToFile() {
        Map<String, String> titlesAndLinksMap = getTitlesAndLinks();

        for (Map.Entry<String, String> titleAndLink : titlesAndLinksMap.entrySet()) {
            String fileNAme = createFileNAme(titleAndLink.getKey());
            System.out.println(fileNAme);
            String content = getContent(titleAndLink.getValue());
            System.out.println(content);
        }
    }

    private static Map<String, String> getTitlesAndLinks() {
        final String WEBSITE_CATEGORY = "/category/java";
        Document doc = null;
        try {
            doc = Jsoup.connect(MAIN_WEBSITE_URL.concat(WEBSITE_CATEGORY)).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return doc.select("div.article h3 a").stream()
                .collect(Collectors.toMap(Element::text, e -> MAIN_WEBSITE_URL.concat(e.attr("href"))));
    }

    private static String getContent(String link) {
        Document doc = null;
        try {
            doc = Jsoup.connect(link).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return doc.select("div[id=drr-container]").text();
    }

    private static String createFileNAme(String name) {
        final Slugify slg = Slugify.builder().build();
        String uuid = UUID.randomUUID().toString();
        return uuid + "-" + slg.slugify(name) + ".txt";
    }
}
