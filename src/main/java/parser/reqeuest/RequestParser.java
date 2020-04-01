package parser.reqeuest;

import io.request.Method;
import io.request.Request;

public class RequestParser {
    /**
     * RequestBody 가 존재하는 Message 를 파싱해 Request 객체를 반환합니다.
     */
    public static Request parse(String message) {
        String[] splittedMessage = message.split("\\\\n\\\\n");

        String messageHeader = splittedMessage[0];
        String[] splittedHeader = messageHeader.split(" ");
        Method method = Method.valueOf(splittedHeader[0]);
        String url = splittedHeader[1];

        if(splittedMessage.length == 1){
            return new Request(method, url);
        } else {
            String messageBody = splittedMessage[1];
            return new Request(method, url, messageBody);
        }
    }
}
