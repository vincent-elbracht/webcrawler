package school.WebCrawler.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import school.WebCrawler.log.CrawlerLogger;
import school.WebCrawler.options.CrawlOptionsChecker;
import school.WebCrawler.service.helper.CrawlerServiceHelper;
import school.WebCrawler.service.helper.PatternMatcher;
import school.WebCrawler.service.helper.SEOService;
import school.WebCrawler.visualize.Visualization;

public class CrawlerService {
  private CrawlerServiceHelper helper = new CrawlerServiceHelper();
  private SEOService seoService = new SEOService();
  private CrawlerLogger logger = new CrawlerLogger();
  private PatternMatcher matcher = new PatternMatcher();
  private CrawlOptionsChecker optionsChecker;
  private Visualization visualization = new Visualization();

  private ObjectMapper mapper = new ObjectMapper();
  private Map<String, Object> visualizationMap = new HashMap<>();
  public List<Thread> threadList = new ArrayList<>();
  public ExecutorService executorService = Executors.newCachedThreadPool();
  public List<Future<?>> futureList = new ArrayList<>();

  private String initURL = "";
  private String initURLHost = "";
  private String protocol = "";

  public HashSet<String> foundEmails = new HashSet<>();
  public HashSet<String> foundUrls = new HashSet<>();
  private HashSet<String> urlQueue = new HashSet<>();

  // private Object lock = new Object();

  public void init(String initURL, Map<String, Object> options) throws MalformedURLException {
    URL url = new URL(initURL);
    if (!initURL.endsWith("/")) {
      initURL += "/";
    }

    this.initURL = initURL;
    this.initURLHost = url.getHost().toString();
    this.protocol = url.getProtocol();
    this.optionsChecker = new CrawlOptionsChecker(options, initURLHost);

    if (optionsChecker.startFromIndex()) {
      this.initURL = url.getProtocol() + "://" + url.getHost();
    }

    if (optionsChecker.useSitemap()) {
      urlQueue.addAll(seoService.getUrlsOnSitemap(this.protocol+"://"+this.initURLHost+"/sitemap.xml"));
    }

    visualizationMap.put(initURL, null);
    mapper.setSerializationInclusion(Include.NON_NULL);
    mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
  }

  public Map<String, Object> startCrawl() {
    visualization.writeHtml(initURLHost);

    crawlLoop(initURL, visualizationMap, new Integer(0));
    urlQueue.forEach(url -> {
      crawlLoop(url, visualizationMap, new Integer(0));
    });

    return visualizationMap;
  }

  private void crawlLoop(String url, Map<String, Object> urlMap, int depth) {

    String sourceHTML = helper.getHTMLSource(url);
    HashSet<String> urlMatches = matcher.findURL(sourceHTML);
    HashSet<String> emailMatches = matcher.findEmail(sourceHTML);

    emailMatches.forEach(email -> {
      if (!foundEmails.contains(email)) {
        System.out.println("EMAIL: " + email);
        logger.email(email);
      }
      foundEmails.add(email);
    });

    Map<String, Object> childMap = new HashMap<>();
    urlMatches.forEach(foundUrl -> {

      String validUrl = helper.getValidUrl(foundUrl, url);
      if (!validUrl.isEmpty() && !foundUrls.contains(validUrl)) {
        if (optionsChecker.changeHost(validUrl)) {
          if (optionsChecker.onylHtml(helper.getURLContentType(validUrl))) {
            if (optionsChecker.useRobotstxt(validUrl)) {
              if (optionsChecker.depth(depth)) {
                if (optionsChecker.timeLimit()) {

                  int depthValue = depth + 1;

                  foundUrls.add(validUrl);

                  System.out.println("URL: " + validUrl);
                  logger.url(validUrl);

                  childMap.put(validUrl, null);
                  urlMap.put(url, childMap);

                  futureList.add(executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                      crawlLoop(validUrl, childMap, new Integer(depthValue));
                    }
                  }));

                } else {
                  // try {
                  // String json = mapper.writeValueAsString(visualizationMap);
                  // visualization.writeHtml(initURLHost, json);
                  // visualization.writeURL(initURLHost, foundUrls);
                  //
                  // } catch (JsonProcessingException e) {
                  // System.err.println("cant map object to json");
                  // e.printStackTrace();
                  // }
                  System.exit(0);
                }
              }
            }
          }
        }
      }
    });

    // if (Thread.currentThread().getId() == 1) {

    // futureList.forEach(future -> {
    // try {
    // future.get();
    // } catch (InterruptedException | ExecutionException e) {
    // e.printStackTrace();
    // }
    // });

    try {
      String json = mapper.writeValueAsString(visualizationMap);
      visualization.writeURL(initURLHost, foundUrls);
      visualization.writeJson(initURLHost, json);
    } catch (JsonProcessingException e1) {
      e1.printStackTrace();
    }

    // if (optionsChecker.openResultInBrowser()) {

    // try {
    // File htmlFile = new File(Visualization.path + "saved\\" + initURLHost + ".html");
    // Desktop.getDesktop().browse(htmlFile.toURI());
    // } catch (IOException e) {
    // e.printStackTrace();
    // }

    // }

    // }
  }
}
