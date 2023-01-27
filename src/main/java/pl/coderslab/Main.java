package pl.coderslab;

import com.github.slugify.Slugify;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Main {

    final static String MAIN_WEBSITE_URL = "https://www.infoworld.com";

    public static void main(String[] args) {
        try {
            Map<String, String> titlesAndLinksMap = getTitlesAndLinks();
            ExecutorService executorService = Executors.newFixedThreadPool(10);
            titlesAndLinksMap.forEach((name, link) -> executorService.execute(() -> {
                try {
                    saveContentToFile(name, link);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }));
            executorService.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, String> getTitlesAndLinks() throws IOException {
        final String WEBSITE_CATEGORY = "/category/java";
        Document doc = Jsoup.connect(MAIN_WEBSITE_URL.concat(WEBSITE_CATEGORY)).get();
        return doc.select("div.article h3 a").stream()
                .collect(Collectors.toMap(Element::text, e -> MAIN_WEBSITE_URL.concat(e.attr("href"))));
    }

    private static void saveContentToFile(String name, String link) throws IOException {
        String content = getContent(link);
        File file = new File(createFileNAme(name));
        FileUtils.writeStringToFile(file, content, "UTF-8");
    }

    private static String createFileNAme(String name) {
        final Slugify slg = Slugify.builder().build();
        String uuid = UUID.randomUUID().toString();
        return uuid + "-" + slg.slugify(name) + ".txt";
    }

    private static String getContent(String link) throws IOException {
        Document doc = Jsoup.connect(link).get();
        return doc.select("div[id=drr-container]").text();
    }
}
