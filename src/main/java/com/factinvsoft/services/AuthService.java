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

    public JsonArray consultarUsuario(final int idUsuario) throws SQLException {
        return db.consultar("SELECT IDUSUARIO,NOMBREUSUARIO FROM USUARIOS WHERE ACTIVO = 1 AND IDUSUARIO = ?", null,
                idUsuario);
    }

    public JsonArray menu (final int idUsuario) throws SQLException{
        String sql = "SELECT COM.OPCION,CM.MENU,CM.DESCRIPCIONMENU,CSM.SUBMENU,CSM.DESCRIPCIONSUBMENU, COM.DESCRIPCION, COM.RUTA, CM.ICONO AS ICONOMENU,COM.ICONO AS ICONOOPCION, "
        + "CSM.ICONO AS ICONOSUBMENU,CM.ORDEN, CSM.ORDEN AS ORDENSUBMENU,COM.ORDEN AS ORDENOPCION  "
        + "FROM PERMISOSPERFILES CP "
        + "INNER JOIN USUARIOSPERFILES CU ON CP.CODPERFIL = CU.CODPERFIL " 
        + "INNER JOIN OPCIONESMENU COM ON CP.OPCION = COM.OPCION "
        + "INNER JOIN MENU CM ON COM.CODMENU = CM.CODMENU "
        + "LEFT JOIN SUBMENU CSM ON COM.CODSUBMENU = CSM.CODSUBMENU "
        + "WHERE CU.IDUSUARIO = ? AND COM.ESVISIBLE= 1 "
        + "GROUP BY COM.OPCION,CM.MENU,CM.DESCRIPCIONMENU,CSM.SUBMENU,CSM.DESCRIPCIONSUBMENU, COM.DESCRIPCION, COM.RUTA, CM.ICONO,COM.ICONO, "
        + "CSM.ICONO, CM.ORDEN, CSM.ORDEN,COM.ORDEN "
        + "ORDER BY CM.ORDEN,CSM.SUBMENU,COM.ORDEN";
        return db.consultar(sql, null, idUsuario);
    }
}
