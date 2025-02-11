package integration.GestioneAccesso;

import it.unisa.application.sottosistemi.GestioneAccesso.view.InformazioniAccountServlet;
import it.unisa.application.model.entity.UtenteAcquirente;
import it.unisa.application.model.entity.UtenteGestoreSede;
import it.unisa.application.model.entity.UtenteGestoreCatena;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import unit.DAO.DatabaseSetupForTest;

import java.lang.reflect.Method;

import static org.mockito.Mockito.*;

public class InformazioniAccountServletIntegrationTest {

    private InformazioniAccountServlet informazioniAccountServlet;
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private HttpSession sessionMock;
    private RequestDispatcher dispatcherMock;

    @BeforeAll
    static void setupDatabase() {
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() {
        informazioniAccountServlet = new InformazioniAccountServlet();
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        sessionMock = mock(HttpSession.class);
        dispatcherMock = mock(RequestDispatcher.class);

        when(requestMock.getSession()).thenReturn(sessionMock);
    }

    @Test
    void testDoGetSuccessUtenteAcquirente() throws Exception {
        UtenteAcquirente utente = new UtenteAcquirente("user123", "email@example.com", "password", "Mario", "Rossi", "Roma");
        when(sessionMock.getAttribute("user")).thenReturn(utente);
        when(requestMock.getRequestDispatcher("/WEB-INF/jsp/informazioniAccount.jsp")).thenReturn(dispatcherMock);

        // Stampa dei dati dell'utente
        System.out.println("Test con UtenteAcquirente:");
        System.out.println("Username: " + utente.getUsername());
        System.out.println("Email: " + utente.getEmail());
        System.out.println("Nome: " + utente.getNome());
        System.out.println("Cognome: " + utente.getCognome());
        System.out.println("Città: " + utente.getCitta());

        invokeDoGet();
        verify(requestMock).setAttribute("user", utente);
        verify(dispatcherMock).forward(requestMock, responseMock);
    }

    @Test
    void testDoGetSuccessGestoreCatena() throws Exception {
        UtenteGestoreCatena gestoreCatena = new UtenteGestoreCatena("passwordCatena", "gestoreCatena");
        when(sessionMock.getAttribute("user")).thenReturn(gestoreCatena);
        when(requestMock.getRequestDispatcher("/WEB-INF/jsp/informazioniAccount.jsp")).thenReturn(dispatcherMock);

        // Stampa dei dati del gestore catena
        System.out.println("Test con UtenteGestoreCatena:");
        System.out.println("Username: " + gestoreCatena.getUsername());
        System.out.println("Password: " + gestoreCatena.getPassword());

        invokeDoGet();
        verify(requestMock).setAttribute("user", gestoreCatena);
        verify(dispatcherMock).forward(requestMock, responseMock);
    }

    @Test
    void testDoGetSuccessGestoreSede() throws Exception {
        UtenteGestoreSede gestoreSede = new UtenteGestoreSede("gestoreSede", "passwordSede", 1);
        when(sessionMock.getAttribute("user")).thenReturn(gestoreSede);
        when(requestMock.getRequestDispatcher("/WEB-INF/jsp/informazioniAccount.jsp")).thenReturn(dispatcherMock);

        // Stampa dei dati del gestore sede
        System.out.println("Test con UtenteGestoreSede:");
        System.out.println("Username: " + gestoreSede.getUsernameUGS());
        System.out.println("Password: " + gestoreSede.getPassword());
        System.out.println("ID Sede: " + gestoreSede.getSedeID());


        invokeDoGet();
        verify(requestMock).setAttribute("user", gestoreSede);
        verify(dispatcherMock).forward(requestMock, responseMock);
    }

    @Test
    void testDoGetRedirectToLoginWhenNotAuthenticated() throws Exception {
        when(sessionMock.getAttribute("user")).thenReturn(null);
        when(requestMock.getContextPath()).thenReturn("/app");

        // Stampa quando l'utente non è autenticato
        System.out.println("Test senza utente autenticato, si aspetta il redirect alla login.");
        System.out.println("Sessione utente: " + sessionMock.getAttribute("user"));

        invokeDoGet();
        verify(responseMock).sendRedirect("/app/loginPage");
    }

    private void invokeDoGet() throws Exception {
        Method doGetMethod = InformazioniAccountServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);
        doGetMethod.invoke(informazioniAccountServlet, requestMock, responseMock);
    }
}
