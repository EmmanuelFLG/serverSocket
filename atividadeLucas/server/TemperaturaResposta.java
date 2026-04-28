package atividadeLucas.server;

public class TemperaturaResposta implements RespostaClimatica {
    @Override
    public String responder() {
        return "A temperatura atual é de 25°C.";
    }
}
