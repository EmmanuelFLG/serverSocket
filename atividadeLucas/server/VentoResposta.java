package atividadeLucas.server;

public class VentoResposta implements RespostaClimatica {
    @Override
    public String responder() {
        return "Ventos moderados soprando a 15 km/h na direção Norte.";
    }
}
