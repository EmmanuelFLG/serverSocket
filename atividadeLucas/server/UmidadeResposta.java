package atividadeLucas.server;

public class UmidadeResposta implements RespostaClimatica {
    @Override
    public String responder() {
        return "A umidade relativa do ar está em 60%.";
    }
}
