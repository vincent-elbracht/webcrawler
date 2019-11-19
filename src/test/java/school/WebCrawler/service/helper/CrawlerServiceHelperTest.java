package school.WebCrawler.service.helper;


import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CrawlerServiceHelperTest {

  @Test
  public void getValidUrl() {
    CrawlerServiceHelper instance = new CrawlerServiceHelper();

    String result = instance.getValidUrl("../c", "http://csbme.de/a/b");
    String expResult = "http://csbme.de/c";

    assertEquals(result, expResult);


  }
}
