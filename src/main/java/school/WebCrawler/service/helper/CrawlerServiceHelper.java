package school.WebCrawler.service.helper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import org.apache.commons.validator.routines.UrlValidator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class CrawlerServiceHelper {

  public String getHTMLSource(String urlString) {
    try {
      
      Document doc = Jsoup.connect(urlString).get();
      String body = doc.body().html();

      return body;
    } catch (IOException e) {
      return "";
    }
  }

  public String getValidUrl(String foundUrl, String srcUrl) {
    try {        
      
      String[] schemes = {"http", "https"}; // DEFAULT schemes = "http", "https", "ftp"
      UrlValidator urlValidator = new UrlValidator(schemes);

      URL src = new URL(srcUrl);
      URL url = src.toURI().resolve(foundUrl).toURL();
      if (urlValidator.isValid(url.toString())) {
        try {
          return url.toString().substring(0, url.toString().indexOf("#"));
        } catch (IndexOutOfBoundsException e) {
          return url.toString();
        }
      } else {
        return "";
      }
    } catch (MalformedURLException | URISyntaxException | IllegalArgumentException e1) {
      System.err.println("CrawlerService.getHostFromUrl: src: " + srcUrl + "  found: " + foundUrl);
      return "";
    }

  }

  public String getURLContentType(String url) {
    try {
      
      HttpURLConnection.setFollowRedirects(false);
      HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
      con.setRequestMethod("HEAD");
      return con.getContentType();
    } catch (Exception e) {
      return "";
    }
  }

}
