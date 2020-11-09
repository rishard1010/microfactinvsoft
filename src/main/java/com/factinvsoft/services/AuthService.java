package com.factinvsoft.services;

import java.sql.SQLException;
import java.util.Arrays;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.factinvsoft.core.ControllerDB;
import com.factinvsoft.core.Encryption;

import org.eclipse.microprofile.jwt.Claims;
import static com.factinvsoft.core.JwtTokenGenerator.generateJWTString;

/**
 *
 * @author RichardZarama
 */
@RequestScoped
public class AuthService {

    @Inject
    ControllerDB db;

    public String iniciarSesion(JsonObject usuario) throws Exception {
        JsonArray resultado = db.consultar(
                "SELECT IDUSUARIO,NOMBREUSUARIO FROM USUARIOS WHERE ACTIVO = 1 AND USUARIO = ? AND CLAVE = ?", null,
                usuario.getString("usuario"), Encryption.Encriptar(usuario.getString("clave")));
        if (resultado.size() > 0) {
            final JsonObjectBuilder userJson = Json.createObjectBuilder();
            userJson.add(Claims.upn.name(), resultado.getJsonObject(0).getString("NOMBREUSUARIO"));
            userJson.add("idusuario", resultado.getJsonObject(0).getInt("IDUSUARIO"));
            userJson.add(Claims.sub.name(), resultado.getJsonObject(0).getString("NOMBREUSUARIO"));
            userJson.add(Claims.groups.name(), Json.createArrayBuilder(Arrays.asList("USER")));
            return generateJWTString(userJson.build().toString());
        }
        return null;
    }

    public JsonArray consultarUsuario(final int idusuario) throws SQLException {
        return db.consultar("SELECT IDUSUARIO,NOMBREUSUARIO FROM USUARIOS WHERE ACTIVO = 1 AND IDUSUARIO = ?", null,
                idusuario);
    }
}
