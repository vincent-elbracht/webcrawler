package school.WebCrawler.visualize;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Visualization {
  String path = "";

  public Visualization(String path) {
    this.path = path;

    File visualizeFile = new File(getClass().getClassLoader().getResource("templates/visualize.html").getFile());
    File localVisualizeFile = new File(path + "\\visualize.html");

    File cssFolder = new File(getClass().getClassLoader().getResource("static/css").getFile());
    File localCssFolder = new File(path + "\\css");

    File jsFolder = new File(getClass().getClassLoader().getResource("static/js").getFile());
    File localJsFolder = new File(path + "\\js");

    try {

      if (!localVisualizeFile.exists()) {
        FileUtils.copyFile(visualizeFile, localVisualizeFile);

      }
      if (!localCssFolder.exists()) {
        FileUtils.copyDirectory(cssFolder, localCssFolder);
      }
      if (!localJsFolder.exists()) {
        FileUtils.copyDirectory(jsFolder, localJsFolder);
      }
      File savedDir = new File(path + "\\saved");
      if (!savedDir.exists()) {
        savedDir.mkdir();
      }

    } catch (IOException e) {
      e.printStackTrace();
      System.err.println("could not create visualization-base-files");
    }
  }

  public void writeHtml(String host) {
    try {
      File html = new File(path + "\\visualize.html");

      Document document = Jsoup.parse(html, "UTF-8");
      document.head().getElementById("responseJson").attr("src", host + ".json");

      BufferedWriter writer = new BufferedWriter(new FileWriter(path + "\\saved\\" + host + ".html"));
      writer.write(document.html());
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void writeURL(String host, Collection<String> foundUrls) {
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(path + "\\saved\\" + host + ".txt"));
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
      BufferedWriter writer = new BufferedWriter(new FileWriter(path + "\\saved\\" + host + ".json"));
      writer.write("responseJson= " + json);
      writer.close();
    } catch (IOException e) {
      System.err.println("Visualization.writeJson() IOException");
    }
  }
}
