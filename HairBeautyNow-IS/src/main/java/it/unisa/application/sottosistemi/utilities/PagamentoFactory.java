package it.unisa.application.sottosistemi.utilities;

public class PagamentoFactory {
    public static PagamentoStrategy getPagamentoStrategy(String metodoPagamento) throws IllegalArgumentException {
        try {
            switch (metodoPagamento.toLowerCase()) {
                case "paypal":
                    return new PayPalStrategy();
                case "visa":
                    return new VisaStrategy();
                case "mastercard":
                    return new MasterCardStrategy();
                default:
                    throw new IllegalArgumentException("Metodo di pagamento non supportato: " + metodoPagamento);
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Errore nella selezione del metodo di pagamento: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Errore imprevisto durante la selezione del metodo di pagamento.", e);
        }
    }
}
