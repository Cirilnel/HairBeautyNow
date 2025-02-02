package it.unisa.application.database_connection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DataSourceSingleton {
    private static DataSource instance;

    public static DataSource getInstance() {
        if (instance == null) {
            try {
                InitialContext ctx = new InitialContext();
                instance = (DataSource) ctx.lookup("java:/comp/env/jdbc/HairBeautyNowDataSource");
            } catch (NamingException e) {
                throw new RuntimeException("Error initializing DataSource", e);
            }
        }
        return instance;
    }
}

