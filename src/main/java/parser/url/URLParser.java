package parser.url;

import java.util.regex.Pattern;

public class URLParser {
    public static boolean validateUrl(String urlFormat, String url) {
        Pattern braceConvertPattern = Pattern.compile("\\{[\\w\\d]+\\}");
        String braceConvertedUrlFormat = braceConvertPattern.matcher(urlFormat)
                .replaceAll("[\\\\w\\\\d]+");

        return Pattern.compile(braceConvertedUrlFormat)
                .matcher(url).find();
    }
}
