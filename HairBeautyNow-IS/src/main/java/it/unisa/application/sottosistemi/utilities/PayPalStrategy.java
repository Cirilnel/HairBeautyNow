package it.unisa.application.sottosistemi.utilities;

import it.unisa.application.model.entity.MetodoDiPagamento;

public class PayPalStrategy implements PagamentoStrategy {
    @Override
    public void effettuaPagamento(MetodoDiPagamento metodoDiPagamento, double importo) {
        // Logica per il pagamento con PayPal
        System.out.println("Pagamento con PayPal effettuato per l'importo di: " + importo);
        // Qui inserisci il codice per processare il pagamento PayPal
    }
}
