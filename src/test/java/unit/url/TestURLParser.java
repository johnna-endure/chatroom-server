package unit.url;

import org.junit.Test;
import parser.url.URLParser;

import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class TestURLParser {

    @Test
    public void testValidateUrl_hasBraceUrl() {
        String urlFormat = "/hello/{id}/{123}";
        String requestUrl = "/hello/2/choi";

        assertThat(URLParser.validateUrl(urlFormat, requestUrl)).isTrue();
    }

    @Test
    public void testValidateUrl_normalUrl() {
        String urlFormat = "/hello/{id}/{name}";
        String requestUrl = "/hello/id/123";

        assertThat(URLParser.validateUrl(urlFormat, requestUrl)).isTrue();
    }

    public boolean validateUrl(String urlFormat, String url) {
        Pattern braceConvertPattern = Pattern.compile("\\{[\\w\\d]+\\}");
        String braceConvertedUrlFormat = braceConvertPattern.matcher(urlFormat)
                .replaceAll("[\\\\w\\\\d]+");

        return Pattern.compile(braceConvertedUrlFormat)
                .matcher(url).find();
    }

}
