package school.WebCrawler.service.helper;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public class PatternMatcher {
  private final String MAILPATTERN = "[a-zA-Z0-9_!#$%&’*+\\/=?`{|}~^.-]+@[a-zA-Z0-9.-]+[.][A-z]+";
  private final String LINKPATTERN = "<a.*?href\\ *?\\=\"?'?([^\\ \\>]*)\"?'?.*?>";

  public HashSet<String> findURL(String sourceHTML) {
    HashSet<String> result = new HashSet<>();
    Pattern pattern = Pattern.compile(LINKPATTERN);
    Matcher matcher = pattern.matcher(sourceHTML);
    while (matcher.find()) {
      String match = sourceHTML.substring(matcher.start(), matcher.end());
      try {
        Element tag = Jsoup.parse(match).body().child(0);
        result.add(tag.attr("href").toString());
      } catch (IndexOutOfBoundsException e) {
        System.err.println("PatternMatcher.findURL: match: " + match);
      }

    }
    return result;
  }

  public HashSet<String> findEmail(String sourceHTML) {
    HashSet<String> result = new HashSet<>();
    Pattern pattern = Pattern.compile(MAILPATTERN);
    Matcher matcher = pattern.matcher(sourceHTML);
    while (matcher.find()) {
      result.add(sourceHTML.substring(matcher.start(), matcher.end()));
    }
    return result;
  }
}
