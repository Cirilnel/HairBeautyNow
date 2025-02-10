package it.unisa.application.sottosistemi.utilities;

import it.unisa.application.model.entity.MetodoDiPagamento;

public class MasterCardStrategy implements PagamentoStrategy {

    @Override
    public void effettuaPagamento(MetodoDiPagamento metodoDiPagamento, double importo) throws IllegalArgumentException {
        // Validazione dei dati specifici per MasterCard
        validateMasterCard(metodoDiPagamento);

        // Logica per il pagamento con MasterCard
        System.out.println("Pagamento con MasterCard effettuato per l'importo di: " + importo);
        // Inserisci qui la logica per processare il pagamento MasterCard
    }

    private void validateMasterCard(MetodoDiPagamento metodoDiPagamento) {
        // Esegui la validazione dei dati (ad esempio, lunghezza numero carta, CVV, scadenza)
        if (metodoDiPagamento.getnCarta() == null || metodoDiPagamento.getnCarta().length() != 16) {
            throw new IllegalArgumentException("Il numero della carta MasterCard deve essere di 16 cifre.");
        }

        // Convertiamo il CVV in stringa prima di verificare la sua lunghezza
        String cvvString = String.valueOf(metodoDiPagamento.getCvv());
        if (cvvString.length() != 3) {  // Ora possiamo usare length() su una stringa
            throw new IllegalArgumentException("Il CVV della carta deve essere di 3 cifre.");
        }

        if (metodoDiPagamento.getDataScadenza() == null) {
            throw new IllegalArgumentException("La data di scadenza non può essere null.");
        }

        // Esegui il controllo sul formato della data di scadenza (MM/YY)
        String dataScadenzaString = metodoDiPagamento.getDataScadenza().toString(); // Converte la data in stringa
        if (!dataScadenzaString.matches("^(0[1-9]|1[0-2])\\/\\d{2}$")) {
            throw new IllegalArgumentException("La data di scadenza deve essere nel formato MM/YY.");
        }
    }
}
