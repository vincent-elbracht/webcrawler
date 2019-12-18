package school.WebCrawler.service.helper;


import static org.junit.Assert.assertEquals;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;

public class CrawlerServiceHelperTest {
  
  public AtomicInteger counter= new AtomicInteger();
  public static void main(String[] args) {
    CrawlerServiceHelperTest test = new CrawlerServiceHelperTest();
    test.test();
  }
  public void test() {
    Map<String, Object> map = new HashMap<>();
    urlMap(map, 10);
    
  }
  
  public void urlMap(Map<String, Object> map, int limit) {
    if (counter.get() < limit) {
      Map<String, Object> childmap = new HashMap<>();
      map.put("csbme.de/"+counter.getAndIncrement(), childmap);
      urlMap(map, 10);  
    }
    else {
      System.out.println(map);
    }
  }
  

  @Test
  public void getValidUrl() {
    CrawlerServiceHelper instance = new CrawlerServiceHelper();

    String result = instance.getValidUrl("https://google.com/users/index", "http://csbme.de/a/b/c");
    String expResult = "https://google.com/users/index";

    assertEquals(result, expResult);
  }
}
