package school.WebCrawler.log;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class EmailLogFormatter extends Formatter {

  @Override
  public String format(LogRecord record) {
    StringBuilder sb = new StringBuilder();
    sb.append("EMAIL").append(':');
    sb.append(record.getMessage()).append('\n');
    return sb.toString();
  }
}
