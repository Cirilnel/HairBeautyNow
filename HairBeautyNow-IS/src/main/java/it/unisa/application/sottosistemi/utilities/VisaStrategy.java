package it.unisa.application.sottosistemi.utilities;

import it.unisa.application.model.entity.MetodoDiPagamento;

public class VisaStrategy implements PagamentoStrategy {
    @Override
    public void effettuaPagamento(MetodoDiPagamento metodoDiPagamento, double importo) {
        // Logica per il pagamento con Visa
        System.out.println("Pagamento con VISA effettuato per l'importo di: " + importo);
        // Qui inserisci il codice per processare il pagamento Visa
    }
}
