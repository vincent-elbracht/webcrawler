package school.WebCrawler.visualize;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Visualization {

  public static final String path =
      "C:\\Users\\viel198\\Desktop\\Ausbildung\\Schule\\Webcrawler\\visualization\\";

  public void writeHtml(String host) {
    try {
      File html = new File(path + "visualize.html");

      Document document = Jsoup.parse(html, "UTF-8");
      document.head().getElementById("responseJson").attr("src", host + ".json");

      BufferedWriter writer = new BufferedWriter(new FileWriter(path + "saved\\" + host + ".html"));
      writer.write(document.html());
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void writeURL(String host, Collection<String> foundUrls) {
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(path + "saved\\" + host + ".txt"));
      foundUrls.forEach(url -> {
        try {
          writer.append(url);
          writer.newLine();
        } catch (IOException e) {
          e.printStackTrace();
        }
      });

      writer.close();
    } catch (IOException e) {
      System.err.println("Visualization.writeJson() IOException");
    }
  }

  public void writeJson(String host, String json) {
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(path + "saved\\" + host + ".json"));
      writer.write("responseJson= " + json);
      writer.close();
    } catch (IOException e) {
      System.err.println("Visualization.writeJson() IOException");
    }
  }
}
