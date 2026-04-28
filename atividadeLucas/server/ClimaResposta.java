package atividadeLucas.server;

public class ClimaResposta implements RespostaClimatica {
    @Override
    public String responder() {
        return "O clima atual é ensolarado com poucas nuvens.";
    }
}
