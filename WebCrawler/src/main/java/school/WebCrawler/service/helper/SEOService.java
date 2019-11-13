package school.WebCrawler.service.helper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import crawlercommons.robots.BaseRobotRules;
import crawlercommons.robots.SimpleRobotRules;
import crawlercommons.robots.SimpleRobotRules.RobotRule;
import crawlercommons.robots.SimpleRobotRules.RobotRulesMode;
import crawlercommons.robots.SimpleRobotRulesParser;
import crawlercommons.sitemaps.AbstractSiteMap;
import crawlercommons.sitemaps.AbstractSiteMap.SitemapType;
import crawlercommons.sitemaps.SiteMap;
import crawlercommons.sitemaps.SiteMapIndex;
import crawlercommons.sitemaps.SiteMapParser;
import crawlercommons.sitemaps.SiteMapURL;
import crawlercommons.sitemaps.UnknownFormatException;

public class SEOService {
  private Map<URL, Set<RobotRule>> robotsCache = new HashMap<>();

  public static void main(String[] args) throws MalformedURLException {
    SEOService s = new SEOService();
    s.getURL(new URL("https://google.com/sitemap.xml")).forEach(url -> {
      System.out.println(url.toString());
    });
  }

  public boolean isAllowed(String url) {
    try {
      URL urlObj = new URL(url);
      Set<RobotRule> cachedRules = robotsCache.get(urlObj);

      if (cachedRules == null) {

        String USER_AGENT = "*";
        HttpClient httpclient = HttpClientBuilder.create().build();

        String hostId = urlObj.getProtocol() + "://" + urlObj.getHost()
            + (urlObj.getPort() > -1 ? ":" + urlObj.getPort() : "");

        Map<String, BaseRobotRules> robotsTxtRules = new HashMap<String, BaseRobotRules>();
        SimpleRobotRules rules = (SimpleRobotRules) robotsTxtRules.get(hostId);

        if (rules == null) {

          HttpGet httpget = new HttpGet(hostId + "/robots.txt");
          HttpResponse response = httpclient.execute(httpget, new BasicHttpContext());

          if (response.getStatusLine() != null && response.getStatusLine().getStatusCode() == 404) {

            rules = new SimpleRobotRules(RobotRulesMode.ALLOW_ALL);
            EntityUtils.consumeQuietly(response.getEntity());
          } else {

            BufferedHttpEntity entity = new BufferedHttpEntity(response.getEntity());
            SimpleRobotRulesParser robotParser = new SimpleRobotRulesParser();

            rules = robotParser.parseContent(hostId, IOUtils.toByteArray(entity.getContent()),
                "text/plain", USER_AGENT);
          }
        }



        robotsCache.put(urlObj, rules.getRobotRules().stream().filter(rule -> !rule.isAllow())
            .collect(Collectors.toSet()));

        RobotRule matchingRule = rules.getRobotRules().stream()
            .filter(rule -> (urlObj.getProtocol() + "://" + urlObj.getHost() + rule.getPrefix())
                .equals(urlObj.toString()))
            .findFirst().orElse(null);

        if (matchingRule == null) {
          return true;
        } else {
          return false;
        }
      } else {

        RobotRule matchingRule = cachedRules.stream()
            .filter(rule -> (urlObj.getProtocol() + "://" + urlObj.getHost() + rule.getPrefix())
                .equals(urlObj.toString()))
            .findFirst().orElse(null);

        if (matchingRule == null || matchingRule.isAllow()) {
          return true;
        } else {
          return false;
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  public HashSet<String> getURL(URL url) {
    SiteMapParser parser = new SiteMapParser();
    AbstractSiteMap sm;
    HashSet<String> urlList = new HashSet<>();
    try {
      try {
        sm = parser.parseSiteMap("text/xml", IOUtils.toByteArray(url), url);
        sm.setType(SitemapType.XML);
      } catch (UnknownFormatException | IOException e) {

        sm = parser.parseSiteMap("text/plain", IOUtils.toByteArray(url), url);
        sm.setType(SitemapType.TEXT);
      }
      if (sm.isIndex()) {
        Collection<AbstractSiteMap> links = ((SiteMapIndex) sm).getSitemaps();
        for (AbstractSiteMap asm : links) {
          urlList.addAll(getURL(asm.getUrl()));
        }
      } else if (!sm.isIndex()) {
        Collection<SiteMapURL> links = ((SiteMap) sm).getSiteMapUrls();
        for (SiteMapURL smu : links) {
          urlList.add(smu.getUrl().toString());
        }
      }
    } catch (UnknownFormatException | IOException e) {
      return null;
    }
    return urlList;
  }

  public List<AbstractSiteMap> getSitemapURLs(String sitemapUrl) {
    List<AbstractSiteMap> sitemaps = new ArrayList<>();
    SiteMapParser siteMapParser = new SiteMapParser();
    try {

      HttpClient httpclient = HttpClientBuilder.create().build();
      HttpGet httpget = new HttpGet(sitemapUrl);
      HttpResponse response = httpclient.execute(httpget, new BasicHttpContext());
      BufferedHttpEntity entity = new BufferedHttpEntity(response.getEntity());

      String contentType = entity.getContentType().toString();

      AbstractSiteMap sitemap = siteMapParser.parseSiteMap(contentType,
          IOUtils.toByteArray(entity.getContent()), new URL(sitemapUrl));

      if (sitemap.isIndex()) {
        sitemaps.addAll(((SiteMapIndex) sitemap).getSitemaps());
      } else {
        sitemaps.add(sitemap);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return sitemaps;
  }
}
