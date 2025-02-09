package it.unisa.application.sottosistemi.utilities;

import it.unisa.application.model.entity.MetodoDiPagamento;

public interface PagamentoStrategy {
    void effettuaPagamento(MetodoDiPagamento metodoDiPagamento, double importo);
}
