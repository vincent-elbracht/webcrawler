package school.WebCrawler.options;

import java.util.HashMap;
import java.util.Map;
import school.WebCrawler.service.helper.SEOService;

public class CrawlOptionsChecker {

  private Map<String, Object> optionsMap = new HashMap<>();
  private String initURLHost = "";
  private SEOService robotsService = new SEOService();
  private long timeAppStart;

  public CrawlOptionsChecker(Map<String, Object> optionsMap, String initURLHost) {
    this.optionsMap = optionsMap;
    this.initURLHost = initURLHost;
    this.timeAppStart = System.currentTimeMillis();
  }

  public boolean changeHost(String foundUrl) {
    return (boolean) optionsMap.get(CrawlOptions.CHANGEHOST) || foundUrl.contains(initURLHost);
  }

  public boolean onylHtml(String contentType) {
    return !(boolean) optionsMap.get(CrawlOptions.ONLYHTML) || contentType.contains("text/html");
  }

  public boolean startFromIndex() {
    return (boolean) optionsMap.get(CrawlOptions.STARTFROMINDEX);
  }

  public boolean useRobotstxt(String foundUrl) {
    return !(boolean) optionsMap.get(CrawlOptions.USEROBOTSTXT) || robotsService.isAllowed(foundUrl);
  }

  public boolean useSitemap() {
    return (boolean) optionsMap.get(CrawlOptions.USESITEMAP);
  }

  public String visualizePath() {
    return (String) optionsMap.get(CrawlOptions.VISUALIZE);
  }

  public boolean depth(int depthCounter) {
    try {
      if ((int) optionsMap.get(CrawlOptions.DEPTH) == 0 || depthCounter < (int) optionsMap.get(CrawlOptions.DEPTH)) {
        return true;
      } else {
        return false;
      }
    } catch (NullPointerException e) {
      return true;
    }
  }

  public boolean openResultInBrowser() {
    return (boolean) optionsMap.get(CrawlOptions.OPENRESULTINBROWSER);
  }

  public boolean timeLimit() {
    Long timeLimit = (Long) optionsMap.get(CrawlOptions.TIMELIMIT);
    if (timeLimit != null) {

      long timeCurrent = System.currentTimeMillis();
      long timeDelta = timeCurrent - timeAppStart;

      if (timeDelta >= timeLimit) {
        return false;
      } else {
        return true;
      }
    }
    return true;
  }

  public boolean maxWaitingTime() {
    Long maxWaitingTime = (Long) optionsMap.get(CrawlOptions.MAXWAITINGTIME);
    if (maxWaitingTime != null) {

      long timeCurrent = System.currentTimeMillis();
      long timeDelta = timeCurrent - timeAppStart;

      if (timeDelta >= maxWaitingTime) {
        return false;
      } else {
        return true;
      }
    }
    return true;
  }
}
