package com.factinvsoft.endpoint;

import java.sql.SQLException;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import com.factinvsoft.services.AuthService;

import org.eclipse.microprofile.jwt.Claim;

/**
 *
 * @author RichardZarama
 */
@Path("auth")
@RolesAllowed({ "USER" })
@RequestScoped
public class AuthEndpoint {
    private static final String AUTH_PREFIX = "Bearer";
    
    @Inject
    AuthService authService;

    @Inject
    @Claim("idusuario")
    private JsonNumber idUsuario;

    @POST
    @Path("login")
    @PermitAll
    public Response iniciarSesion(final JsonObject usuario) {
        try {
            final JsonObjectBuilder jsonToken = Json.createObjectBuilder();
            final String generatedJWT = authService.iniciarSesion(usuario);
            if (generatedJWT != null && !generatedJWT.isEmpty()) {
                jsonToken.add("success", true);
                jsonToken.add("token", AUTH_PREFIX + " " + generatedJWT);
                return Response.ok(jsonToken.build()).build();
            } else {
                return Response.status(Status.UNAUTHORIZED).entity("Usuario o contraseña invalida").build();
            }
        } catch (final Exception ex) {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }
    }

    @POST
    @Path("logout")
    public Response logout() {
        return Response.ok("logout").build();
    }

    @GET
    @Path("user")
    public Response consultarUsuario() {
        try {
            return Response.ok(authService.consultarUsuario(idUsuario.intValue())).build();
        } catch (final SQLException ex) {
            return Response.serverError().entity(ex).build();
        }
    }

    @GET
    @Path("menu")
    public Response menu(){
        try {
            return Response.ok(authService.menu(idUsuario.intValue())).build();
        } catch (Exception e) {
            return Response.status(400).entity(e.getMessage()).build();
        }
    }
}
