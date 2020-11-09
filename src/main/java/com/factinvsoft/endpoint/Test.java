package com.factinvsoft.endpoint;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("test")
public class Test {

    @GET
    public Response test() {
        return Response.ok("HOLA MUNDO").build();
    }
}
