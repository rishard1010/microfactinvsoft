package com.factinvsoft.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

import javax.json.JsonArray;

public class ControllerDB {
    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getParam(String name) {
        return Optional.ofNullable(System.getenv(name)).orElse(System.getProperty(name));
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(getParam("DB_URL"), getParam("DB_USER"), getParam("DB_PASSWORD"));
    }

    public JsonArray consultar(final String sql, final Object... args) throws SQLException {
        final boolean esTransaccion = esTransaccion(args);
        final PreparedStatement stmt = prepareStatement(esTransaccion, sql, args);
        final JsonArray resultado ;
        try {
            resultado = Utils.resultSetToJsonArray(stmt.executeQuery());
        } finally {
            if (!esTransaccion)
                stmt.getConnection().close();
        }
        return resultado;
    }

    public int update(final String sql, final Object... args) throws SQLException {
        final boolean esTransaccion = esTransaccion(args);
        final PreparedStatement stmt = prepareStatement(esTransaccion, sql, args);
        int filasActualizadas = 0 ;
        try {
            filasActualizadas = stmt.executeUpdate();
        } finally {
            if (!esTransaccion)
                stmt.getConnection().close();
        }
        return filasActualizadas;
    }

    private PreparedStatement prepareStatement(final boolean esTransaccion, final String sql, final Object... args)
            throws SQLException {
        final Connection conn = esTransaccion ? (Connection) args[0] : getConnection();
        final PreparedStatement stmt = conn.prepareStatement(sql);
        int ix = 1;
        for (int px = 1; ix < args.length; ix++, px++) {
            stmt.setObject(px, args[ix]);
        }
        return stmt;
    }

    private boolean esTransaccion(final Object... args) {
        return args != null && args.length > 0 && args[0] instanceof Connection;
    }
}
