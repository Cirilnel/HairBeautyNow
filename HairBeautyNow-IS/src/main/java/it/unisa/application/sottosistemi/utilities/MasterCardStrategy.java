package it.unisa.application.sottosistemi.utilities;

import it.unisa.application.model.entity.MetodoDiPagamento;

public class MasterCardStrategy implements PagamentoStrategy {
    @Override
    public void effettuaPagamento(MetodoDiPagamento metodoDiPagamento, double importo) {
        // Logica per il pagamento con MasterCard
        System.out.println("Pagamento con MasterCard effettuato per l'importo di: " + importo);
        // Qui inserisci il codice per processare il pagamento MasterCard
    }
}
