package it.unisa.application.database_connection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DataSourceSingleton {
    private static volatile DataSource instance;

    // Costruttore privato per evitare istanziazioni esterne
    private DataSourceSingleton() {}

    // Metodo per ottenere l'istanza del DataSource
    public static DataSource getInstance() {
        // Controllo se l'istanza è già stata creata
        if (instance == null) {
            // Blocco per garantire che sia thread-safe
            synchronized (DataSourceSingleton.class) {
                if (instance == null) {
                    try {
                        // Creiamo un contesto iniziale per il lookup
                        Context initCtx = new InitialContext();
                        Context envCtx = (Context) initCtx.lookup("java:comp/env");

                        // Cerchiamo il DataSource nel contesto
                        instance = (DataSource) envCtx.lookup("jdbc/HairBeautyNowDataSource");
                    } catch (NamingException e) {
                        // Gestiamo l'eccezione con un'eccezione runtime, poiché il DataSource non è disponibile
                        throw new RuntimeException("Error initializing DataSource", e);
                    }
                }
            }
        }
        // Restituiamo l'istanza del DataSource
        return instance;
    }
}
