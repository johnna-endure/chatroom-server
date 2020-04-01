package controller;

import database.ChatRoomDatabase;
import exception.response.NotFoundException;
import handler.RequestHandler;
import handler.RequestMapping;
import io.request.Request;
import io.response.Response;
import parser.url.URLParser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChatRoomController {
    RequestHandler requestHandler;
    ChatRoomDatabase database;

    public ChatRoomController() {
        init();
    }

    public void init() {
        requestHandler = new RequestHandler();
        database = new ChatRoomDatabase();

        requestHandler.setDatabase(database);
    }

    public Response dispatch(Request request) {
//        System.out.println(request);
        Method[] methods = RequestHandler.class.getMethods();
        Optional<Method> handlerOpt = findHandler(methods, request.getUrl(), request.getMethod());

        try {
            Method handler = handlerOpt.orElseThrow(() ->
                    new NotFoundException("url 에 해당하는 핸들러를 찾지 못했습니다."));
            Response response = invoke(requestHandler ,handler, request);  // 이 부분도 여러 인수를 받을 수 있도록 수정 필요함
//            System.out.println("핸들러 호출/ 응답 : " + response);
            return response;
        } catch (NotFoundException e) {
            e.printStackTrace();
            return new Response(404, "not found handler");
        }
    }

    private Optional<Method> findHandler(Method[] methods, String url, io.request.Method protocolMethod) {
        return Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .filter(method -> method.getAnnotation(RequestMapping.class).method().equals(protocolMethod))
                .filter(method -> URLParser.validateUrl(method.getAnnotation(RequestMapping.class).url(), url))
                .findFirst();
    }

    public <T> Response invoke(T underlyingObject, java.lang.reflect.Method method, Request request) {
        String urlFormat = method.getAnnotation(RequestMapping.class).url();
        String requestUrl = request.getUrl();

        Map<String, String> map = getInnerBraceKeyMap(urlFormat, requestUrl);
        Object[] parameters = mappingParameter(method, request, map);

        try {
            return (Response) method.invoke(underlyingObject, parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return new Response(500, "invoke reflection error");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return new Response(500, "argument type match error");
        }
    }

    private Map<String, String> getInnerBraceKeyMap(String urlFormat, String requestUrl) {
        String[] formatFractions = urlFormat.split("/");
        String[] requestUrlFractions = requestUrl.split("/");

        return Stream.iterate(0, i -> i+1)
                .limit(formatFractions.length)
                .filter(i -> formatFractions[i].contains("{"))
                .collect(Collectors.toMap(i -> formatFractions[i].substring(1,formatFractions[i].length()-1),
                        i -> requestUrlFractions[i]));
    }

    private Object[] mappingParameter( java.lang.reflect.Method method,
                                       Request request, Map<String, String> innerBraceKeyMap) {
        Parameter[] declaredParameters = method.getParameters();
        Object[] parameters = new Object[declaredParameters.length];

        Iterator<Map.Entry<String, String>> entryIterator = innerBraceKeyMap.entrySet().iterator();
        while(entryIterator.hasNext()) {
            Map.Entry<String, String> entry = entryIterator.next();
            String key = entry.getKey();
            String value = entry.getValue();
            // url 포맷의 중괄호 값 매핑
            Stream.iterate(0, i -> i+1)
                    .limit(declaredParameters.length)
                    .filter(i -> declaredParameters[i].getName().equals(key))
                    .forEach(i -> {
                        if(declaredParameters[i].getType() == int.class) {
                            parameters[i] = Integer.parseInt(value);
                        }else if(declaredParameters[i].getType() == long.class){
                            parameters[i] = Long.parseLong(value);
                        }else if(declaredParameters[i].getType() == double.class){
                            parameters[i] = Double.parseDouble(value);
                        }else{ //객체형 파라미터의 경우
                            parameters[i] = value;
                        }
                    });
        }
        // 파라미터에 Request 있을 경우 매핑
        Stream.iterate(0, i -> i+1)
                .limit(declaredParameters.length)
                .filter(i -> declaredParameters[i].getType() == Request.class)
                .forEach(i -> {
                    parameters[i] = request;
                });
        return parameters;
    }

    public void setRequestHandler(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    public void setDatabase(ChatRoomDatabase database) {
        this.database = database;
    }
}
