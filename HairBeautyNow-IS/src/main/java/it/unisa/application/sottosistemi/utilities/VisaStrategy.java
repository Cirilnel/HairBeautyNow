package it.unisa.application.sottosistemi.utilities;

import it.unisa.application.model.entity.MetodoDiPagamento;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class VisaStrategy implements PagamentoStrategy {

    @Override
    public void effettuaPagamento(MetodoDiPagamento metodoDiPagamento, double importo) throws IllegalArgumentException {
        // Validazione dei dati specifici per VISA
        validateVisa(metodoDiPagamento);

        // Logica per il pagamento con VISA
        System.out.println("Pagamento con VISA effettuato per l'importo di: " + importo);
        // Inserisci qui la logica per processare il pagamento Visa
    }

    private void validateVisa(MetodoDiPagamento metodoDiPagamento) {
        // Esegui la validazione dei dati (ad esempio, lunghezza numero carta, CVV, scadenza)
        if (metodoDiPagamento.getnCarta() == null || metodoDiPagamento.getnCarta().length() != 16) {
            throw new IllegalArgumentException("Il numero della carta VISA deve essere di 16 cifre.");
        }

        // Convertiamo il CVV in stringa prima di verificare la sua lunghezza
        String cvvString = String.valueOf(metodoDiPagamento.getCvv());
        if (cvvString.length() != 3) {  // Ora possiamo usare length() su una stringa
            throw new IllegalArgumentException("Il CVV della carta deve essere di 3 cifre.");
        }

        if (metodoDiPagamento.getDataScadenza() == null) {
            throw new IllegalArgumentException("La data di scadenza non può essere null.");
        }

        // La data di scadenza è già LocalDate, possiamo usarla direttamente
        LocalDate dataScadenza = metodoDiPagamento.getDataScadenza();

        // Verifica il formato MM/YY
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
        String dataScadenzaString = dataScadenza.format(formatter);

        if (!dataScadenzaString.matches("^(0[1-9]|1[0-2])\\/\\d{2}$")) {
            throw new IllegalArgumentException("La data di scadenza deve essere nel formato MM/YY.");
        }

        // Controllo se la carta è scaduta
        LocalDate dataCorrente = LocalDate.now();  // Otteniamo la data corrente
        if (dataScadenza.isBefore(dataCorrente)) {
            throw new IllegalArgumentException("La carta è scaduta.");
        }
    }
}