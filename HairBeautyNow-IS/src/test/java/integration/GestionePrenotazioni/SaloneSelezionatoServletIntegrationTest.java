package integration.GestionePrenotazioni;

import it.unisa.application.model.entity.FasciaOraria;
import it.unisa.application.model.entity.Professionista;
import it.unisa.application.sottosistemi.GestionePrenotazioni.view.SaloneSelezionatoServlet;
import it.unisa.application.sottosistemi.GestionePrenotazioni.service.SaloneService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unit.DAO.DatabaseSetupForTest;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

public class SaloneSelezionatoServletIntegrationTest {
    private SaloneSelezionatoServlet servlet;
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private HttpSession sessionMock;
    private RequestDispatcher dispatcherMock;
    private SaloneService saloneServiceMock;

    @BeforeAll
    static void setupDatabase() {
        // Configura il database, se necessario
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() throws Exception {
        servlet = new SaloneSelezionatoServlet();
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        sessionMock = mock(HttpSession.class);
        dispatcherMock = mock(RequestDispatcher.class);
        saloneServiceMock = mock(SaloneService.class);

        // Mock per i metodi di SaloneService
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(requestMock.getRequestDispatcher("/WEB-INF/jsp/professionista.jsp")).thenReturn(dispatcherMock);

        // Iniettare il mock di SaloneService
        injectMock("saloneService", saloneServiceMock);
    }

    @Test
    void testDoPost() throws Exception {
        String saloneId = "1";  // ID del salone che passiamo alla servlet

        // Imposta i parametri della richiesta
        when(requestMock.getParameter("saloneId")).thenReturn(saloneId);

        // Creazione delle FasciaOraria per ciascun professionista
        List<FasciaOraria> fasceOrarie1 = Arrays.asList(
                new FasciaOraria(1, 1, LocalDate.of(2025, 2, 15), "10:00-12:00", true),
                new FasciaOraria(2, 1, LocalDate.of(2025, 2, 15), "14:00-16:00", true)
        );

        List<FasciaOraria> fasceOrarie2 = Arrays.asList(
                new FasciaOraria(3, 2, LocalDate.of(2025, 2, 16), "09:00-11:00", true),
                new FasciaOraria(4, 2, LocalDate.of(2025, 2, 16), "15:00-17:00", true)
        );

        // Mock dei professionisti con le fasce orarie
        List<Professionista> professionistiMock = Arrays.asList(
                new Professionista(1, "Mario Rossi", 1, fasceOrarie1),
                new Professionista(2, "Luigi Bianchi", 1, fasceOrarie2)
        );

        Map<LocalDate, List<String>> fasceOrarieMock = new HashMap<>();
        fasceOrarieMock.put(LocalDate.of(2025, 2, 15), Arrays.asList("10:00-12:00", "14:00-16:00"));
        fasceOrarieMock.put(LocalDate.of(2025, 2, 16), Arrays.asList("09:00-11:00", "15:00-17:00"));

        // Mock per il comportamento del SaloneService
        when(saloneServiceMock.getProfessionistiBySalone(Integer.parseInt(saloneId))).thenReturn(professionistiMock);
        when(saloneServiceMock.getFasceOrarieByProfessionista(anyInt())).thenReturn(fasceOrarieMock);

        // Mock della sessione utente
        doNothing().when(sessionMock).setAttribute(eq("saloneSelezionato"), eq(saloneId));

        // Invoca il metodo doPost tramite riflessione
        invokeDoPost();

        // Verifica che i professionisti siano stati recuperati correttamente
        verify(saloneServiceMock).getProfessionistiBySalone(Integer.parseInt(saloneId));

        // Verifica che le fasce orarie siano state recuperate correttamente per ogni professionista
        verify(saloneServiceMock, times(2)).getFasceOrarieByProfessionista(anyInt());

        // Verifica che i dati siano stati impostati correttamente nella richiesta
        verify(requestMock).setAttribute("professionisti", professionistiMock);
        verify(requestMock).setAttribute("fasceOrarieByProfessionista", Map.of(
                1, fasceOrarieMock,
                2, fasceOrarieMock
        ));
        verify(requestMock).setAttribute("saloneId", saloneId);

        // Verifica che la servlet faccia il forward alla pagina professionista.jsp
        verify(requestMock).getRequestDispatcher("/WEB-INF/jsp/professionista.jsp");
        verify(dispatcherMock).forward(requestMock, responseMock);

        // Stampa i professionisti e le fasce orarie per il debug
        System.out.println("Professionisti disponibili:");
        professionistiMock.forEach(professionista -> System.out.println(professionista));

        System.out.println("Fasce orarie per i professionisti:");
        fasceOrarieMock.forEach((date, times) -> {
            System.out.println("Data: " + date + " Orari: " + times);
        });
    }

    // Metodo per invocare doPost con riflessione
    private void invokeDoPost() throws Exception {
        Method doPostMethod = SaloneSelezionatoServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);
        doPostMethod.invoke(servlet, requestMock, responseMock);
    }

    private void injectMock(String fieldName, Object mockInstance) throws Exception {
        // Inietta il mock nel campo della servlet
        var field = SaloneSelezionatoServlet.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(servlet, mockInstance);
    }
}
