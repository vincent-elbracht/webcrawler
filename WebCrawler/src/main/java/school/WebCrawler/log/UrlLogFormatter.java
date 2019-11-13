package school.WebCrawler.log;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class UrlLogFormatter extends Formatter {

  @Override
  public String format(LogRecord record) {
    StringBuilder sb = new StringBuilder();
    sb.append("URL").append(':');
    sb.append(record.getMessage()).append('\n');
    return sb.toString();
  }
}
