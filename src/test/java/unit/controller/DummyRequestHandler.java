package unit.controller;

import com.google.common.annotations.VisibleForTesting;
import handler.RequestMapping;
import io.request.Method;
import io.request.Request;
import io.response.Response;

import java.lang.invoke.MethodHandle;

public class DummyRequestHandler {
    @RequestMapping(method = Method.POST, url = "/hello/{id}/{name}")
    public Response bracedURLMethod(long id, Request request, String name) {
        System.out.println("Request : " + request);
        System.out.println("id : " + id);
        System.out.println("name : " + name);

        return new Response(200);
    }

    @RequestMapping(method = Method.GET, url = "/hello")
    public Response plainURLMethod(Request request) {
        return new Response(200);
    }

    @RequestMapping(method = Method.GET, url = "/hello/{id}/name")
    public Response hybridURLMethod(int id, Request request) {
//        System.out.println(id);
//        System.out.println(request);
        return new Response(200);
    }

    @RequestMapping(method = Method.GET, url = "/hello")
    public Response noArgumentMethod() {
        return new Response(200);
    }
}
