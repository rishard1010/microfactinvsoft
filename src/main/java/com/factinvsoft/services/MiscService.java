package com.factinvsoft.services;

import java.sql.SQLException;

import javax.inject.Inject;
import javax.json.JsonArray;

import com.factinvsoft.core.ControllerDB;

public class MiscService {
    
    @Inject
    ControllerDB db;
    

    public JsonArray consultarReferencias(final int codClaseReferencia) throws SQLException {
        final String sql = "SELECT CODREFERENCIA, NOMREFERENCIA, CODREFERENCIATXT, CONDICIONREFERENCIA FROM DETALLEREFERENCIAS WHERE ACTIVO = 1 AND CODCLASEREFERENCIA = ? ";
        return db.consultar(sql, null, codClaseReferencia);
    }

    public JsonArray consultarPaises() throws SQLException {
        final String sql = "SELECT CODPAIS,NOMBREPAIS,NACIONALIDAD FROM PAISES ORDER BY NOMBREPAIS";
        return db.consultar(sql);
    }

    public JsonArray consultarDepartamentos(final String codPais) throws SQLException {
        final String sql = "SELECT CODDEPARTAMENTO, NOMDEPARTAMENTO  "
        + " FROM DEPARTAMENTOS WHERE CODPAIS = ? ORDER BY 2 ASC ";
        return db.consultar(sql, null, codPais);
    }

    public JsonArray consultarCiudades(final String codDepartamento) throws SQLException {
        final String sql = "SELECT CODCIUDAD, NOMCIUDAD  "
        + " FROM CIUDADES WHERE CODDEPARTAMENTO = ? ORDER BY 2 ASC ";
        return db.consultar(sql, null, codDepartamento);
    }

    public JsonArray consultarBarrios(final String codCiudad) throws SQLException {
        final String sql = "SELECT CODBARRIO, NOMBARRIO "
        + " FROM BARRIOS WHERE CODCIUDAD = ? ORDER BY 2 ASC ";
        return db.consultar(sql, null, codCiudad);
    }
}
