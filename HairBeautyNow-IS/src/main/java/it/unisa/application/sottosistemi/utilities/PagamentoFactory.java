package it.unisa.application.sottosistemi.utilities;

public class PagamentoFactory {
    public static PagamentoStrategy getPagamentoStrategy(String metodoPagamento) {
        switch (metodoPagamento) {
            case "paypal":
                return new PayPalStrategy();
            case "visa":
                return new VisaStrategy();
            case "mastercard":
                return new MasterCardStrategy();
            default:
                throw new IllegalArgumentException("Metodo di pagamento non supportato.");
        }
    }
}
