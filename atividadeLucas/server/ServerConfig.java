package atividadeLucas.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerConfig {
    private ServerSocket servidor;
    private ExecutorService pool;

    private Map<String, RespostaClimatica> baseDeDados;

    public void startServer(int porta) {
        pool = Executors.newFixedThreadPool(5);
        inicializarBaseDeDados();

        try {
            servidor = new ServerSocket(porta);
            System.out.println("Servidor Climático operando na porta " + porta);

            while (true) {
                Socket cliente = servidor.accept();
                String ipCliente = cliente.getInetAddress().getHostAddress();

                System.out.println("Cliente " + ipCliente + " conectado.");

             
                pool.execute(new ServerThread(cliente, ipCliente, baseDeDados));
            }
        } catch (IOException e) {
            System.err.println("Erro ao iniciar o servidor: " + e.getMessage());
        }
    }

    private void inicializarBaseDeDados() {
        baseDeDados = new HashMap<>();

        baseDeDados.put("CLIMA", new ClimaResposta());
        baseDeDados.put("TEMPERATURA", new TemperaturaResposta());
        baseDeDados.put("UMIDADE", new UmidadeResposta());
        baseDeDados.put("VENTO", new VentoResposta());
    }

}