package it.unisa.application.database_connection;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import javax.sql.DataSource;

@WebListener
public class MainContext implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Ottieni il DataSource dalla singleton
        DataSource ds = DataSourceSingleton.getInstance();

        // Ottieni il ServletContext
        ServletContext context = sce.getServletContext();

        // Imposta il DataSource come attributo del ServletContext
        context.setAttribute("DataSource", ds);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Non è necessario fare nulla qui se non c'è bisogno di fare pulizia.
    }
}
