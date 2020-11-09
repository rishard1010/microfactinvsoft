package com.factinvsoft.core;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

public class Utils {

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat sdfSinHMS = new SimpleDateFormat("yyyy-MM-dd");
    
    public static JsonArray resultSetToJsonArray(final ResultSet rs) throws SQLException {
        final ResultSetMetaData metaData = rs.getMetaData();

        final JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        JsonObjectBuilder jsonObject;
        final int columns = metaData.getColumnCount();
        String column;
        Object value;
        while (rs.next()) {
            jsonObject = Json.createObjectBuilder();
            for (int index = 1; index <= columns; index++) {
                column = metaData.getColumnName(index).toUpperCase();
                value = rs.getObject(column);
                if (value == null) {
                    jsonObject.addNull(column);
                } else if (value instanceof Integer) {
                    jsonObject.add(column, (Integer) value);
                } else if (value instanceof String) {
                    jsonObject.add(column, (String) value);
                } else if (value instanceof Boolean) {
                    jsonObject.add(column, (Boolean) value);
                } else if (value instanceof Date) {
                    jsonObject.add(column, sdf.format((Date) value));
                } else if (value instanceof Long) {
                    jsonObject.add(column, (Long) value);
                } else if (value instanceof Double) {
                    jsonObject.add(column, (Double) value);
                } else if (value instanceof Float) {
                    jsonObject.add(column, (Float) value);
                } else if (value instanceof BigDecimal) {
                    jsonObject.add(column, (BigDecimal) value);
                } else if (value instanceof Byte) {
                    jsonObject.add(column, (Byte) value);
                } else {
                    jsonObject.add(column, rs.getString(index));
                }
            }
            arrayBuilder.add(jsonObject);
        }
        return arrayBuilder.build();
    }
}
