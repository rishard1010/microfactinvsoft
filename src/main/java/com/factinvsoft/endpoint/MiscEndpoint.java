package com.factinvsoft.endpoint;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.DefaultValue;
import javax.inject.Inject;
import javax.ws.rs.PathParam;
import com.factinvsoft.services.MiscService;

@Path("misc")
@RequestScoped
@RolesAllowed({ "USER" })
public class MiscEndpoint {
    
    @Inject
    MiscService miscService;

    @GET
    @Path("/referencias")
    public Response consultarReferencias(@QueryParam("codclasereferencia") final int codClaseReferencia,
            @QueryParam("condicionreferencia") @DefaultValue("") final String condicionReferencia) {
        try {
            JsonArray resJson = miscService.consultarReferencias(codClaseReferencia);
            List <?> lista = resJson.stream().filter((item)->{
                JsonObject registro = item.asJsonObject();
                boolean condicion = true;
                if(!condicionReferencia.equals("")){
                    condicion = condicionReferencia.indexOf(registro.getString("CONDICIONREFERENCIA", "-1")) >= 0;
                }
                return  condicion;

            }).collect(Collectors.toList());

            return Response.ok().entity(lista).build();
        } catch (final Exception ex) {
            return Response.serverError().entity(ex).build();
        }
    }

    @GET
    @Path("/paises")
    public Response consultarPaises() {
        try {
            return Response.ok(miscService.consultarPaises()).build();
        } catch (final Exception ex) {
            return Response.serverError().entity(ex).build();
        }
    }

    @GET
    @Path("/departamentos/{codpais}")
    public Response consultarDepartamentos(@PathParam("codpais") final String codPais) {
        try {
            return Response.ok(miscService.consultarDepartamentos(codPais)).build();
        } catch (final Exception ex) {
            return Response.serverError().entity(ex).build();
        }
    }

    @GET
    @Path("/ciudades/{coddepartamento}")
    public Response consultarCiudades(@PathParam("coddepartamento") final String codDepartamento) {
        try {
            return Response.ok(miscService.consultarCiudades(codDepartamento)).build();
        } catch (final Exception ex) {
            return Response.serverError().entity(ex).build();
        }
    }

    @GET
    @Path("/barrios/{codciudad}")
    public Response consultarBarrios(@PathParam("codciudad") final String codCiudad) {
        try {
            return Response.ok(miscService.consultarBarrios(codCiudad)).build();
        } catch (final Exception ex) {
            return Response.serverError().entity(ex).build();
        }
    }
}
