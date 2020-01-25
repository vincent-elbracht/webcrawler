package school.WebCrawler;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;
import school.WebCrawler.options.CrawlOptions;
import school.WebCrawler.service.CrawlerService;

public class WebCrawler {

  public static void main(String[] args) throws JsonProcessingException, InterruptedException {

   // System.setProperty("http.proxyHost", "10.1.1.3");
   // System.setProperty("http.proxyPort", "8080");

    CrawlerService service = new CrawlerService();
    
    try {
      service.init("https://youtube.com",
          CrawlOptions.options()
              .startFromIndex(false)
              .changeHost(true)
              .onylHtml(true)
              .useRobotstxt(true)
              .useSitemap(true)
              .openResultInBrowser(true)
              .timelimit(1, TimeUnit.MINUTES)
              .visualize("C:\\Users\\Vincent\\Desktop\\visualization")
              .get());
      
      service.startCrawl();
      
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    
   // System.clearProperty("http.proxyHost");
   // System.clearProperty("http.proxyPort");
  }

}
