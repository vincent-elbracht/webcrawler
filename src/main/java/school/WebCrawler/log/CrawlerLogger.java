package school.WebCrawler.log;

import java.io.IOException;
import java.util.Collection;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

public class CrawlerLogger {

  Logger urlLogger = Logger.getLogger("WebCrawlerUrlLog");
  FileHandler urlFileHandler;
  Logger emailLogger = Logger.getLogger("WebCrawlerEmailLog");
  FileHandler emailFileHandler;
  Handler handler;

  public CrawlerLogger() {

    try {
      urlFileHandler = new FileHandler(
          "C:\\Users\\viel198\\Desktop\\Ausbildung\\Schule\\Webcrawler\\webcrawlerUrl.log");
      urlFileHandler.setFormatter(new UrlLogFormatter());
      emailFileHandler = new FileHandler(
          "C:\\Users\\viel198\\Desktop\\Ausbildung\\Schule\\Webcrawler\\webcrawlerEmail.log");
      emailFileHandler.setFormatter(new EmailLogFormatter());
      urlLogger.addHandler(urlFileHandler);
      emailLogger.addHandler(emailFileHandler);
      urlLogger.setUseParentHandlers(false);
      emailLogger.setUseParentHandlers(false);
    } catch (SecurityException | IOException e) {
    }
  }

  public void email(String email) {
    emailLogger.info(email);
  }

  public void email(Collection<String> emails) {
    emails.forEach(email -> {
      emailLogger.info(email);
    });
  }

  public void url(String url) {
    urlLogger.info(url);
  }

  public void url(Collection<String> urls) {
    urls.forEach(url -> {
      urlLogger.info(url);
    });
  }

  public void close() {
    for (Handler h : urlLogger.getHandlers()) {
      h.close();
    }
    for (Handler h : emailLogger.getHandlers()) {
      h.close();
    }
  }
}
