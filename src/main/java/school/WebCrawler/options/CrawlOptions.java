package school.WebCrawler.options;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CrawlOptions {

  private Map<String, Object> optionsMap = new HashMap<>();

  public static final String CHANGEHOST = "changeHost";
  public static final String USEROBOTSTXT = "useRobotstxt";
  public static final String USESITEMAP = "useSitemap";
  public static final String ONLYHTML = "onlyHtml";
  public static final String STARTFROMINDEX = "startFromIndex";
  public static final String OPENRESULTINBROWSER = "openResultInBrowser";
  public static final String DEPTH = "depth";
  public static final String MAXWAITINGTIME = "maxWaitingTime";
  public static final String VISUALIZE = "visualize";

  public static final String TIMELIMIT = "timelimit";


  public static CrawlOptions options() {
    return new CrawlOptions();
  }

  public CrawlOptions changeHost(boolean changeHost) {
    optionsMap.put(CrawlOptions.CHANGEHOST, changeHost);
    return this;
  }

  public CrawlOptions useRobotstxt(boolean useRobotstxt) {
    optionsMap.put(CrawlOptions.USEROBOTSTXT, useRobotstxt);
    return this;
  }

  public CrawlOptions useSitemap(boolean useSitemap) {
    optionsMap.put(CrawlOptions.USESITEMAP, useSitemap);
    return this;
  }

  public CrawlOptions onylHtml(boolean onlyHtml) {
    optionsMap.put(CrawlOptions.ONLYHTML, onlyHtml);
    return this;
  }

  public CrawlOptions startFromIndex(boolean startFromIndex) {
    optionsMap.put(CrawlOptions.STARTFROMINDEX, startFromIndex);
    return this;
  }

  public CrawlOptions openResultInBrowser(boolean openResultInBrowser) {
    optionsMap.put(CrawlOptions.OPENRESULTINBROWSER, openResultInBrowser);
    return this;
  }

  public CrawlOptions timelimit(int timelimit_number, TimeUnit timelimit_unit) {
    Long millis = TimeUnit.MILLISECONDS.convert(timelimit_number, timelimit_unit);
    optionsMap.put(CrawlOptions.TIMELIMIT, millis);
    return this;
  }

  public CrawlOptions depth(int depth) {
    optionsMap.put(CrawlOptions.DEPTH, depth);
    return this;
  }

  public CrawlOptions maxWaitingTime(int maxWaitingTime_number, TimeUnit maxWaitingTime_unit) {
    Long millis = TimeUnit.MILLISECONDS.convert(maxWaitingTime_number, maxWaitingTime_unit);
    optionsMap.put(CrawlOptions.MAXWAITINGTIME, millis);
    return this;
  }

  public CrawlOptions visualize(String outputPath) {
    if (outputPath != null) {
      optionsMap.put(CrawlOptions.VISUALIZE, outputPath);
    } else {
      optionsMap = null;
    }
    return this;
  }

  public Map<String, Object> get() {
    return this.optionsMap;
  }
}
