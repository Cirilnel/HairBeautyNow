package it.unisa.application.sottosistemi.utilities;

import it.unisa.application.model.entity.MetodoDiPagamento;

public class PayPalStrategy implements PagamentoStrategy {

    @Override
    public void effettuaPagamento(MetodoDiPagamento metodoDiPagamento, double importo) throws IllegalArgumentException {
        // Validazione dei dati specifici per PayPal
        validatePayPal(metodoDiPagamento);

        // Logica per il pagamento con PayPal
        System.out.println("Pagamento con PayPal effettuato per l'importo di: " + importo);
        // Inserisci qui la logica per processare il pagamento PayPal
    }

    private void validatePayPal(MetodoDiPagamento metodoDiPagamento) {
        // Validazione dell'email PayPal
        if (metodoDiPagamento.getEmail() == null || metodoDiPagamento.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("L'email PayPal è obbligatoria.");
        }
    }
}
