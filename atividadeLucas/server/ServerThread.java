package atividadeLucas.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerThread implements Runnable {

    private Socket cliente;
    private String ipCliente;
    private Map<String, RespostaClimatica> baseDeDados;

    public ServerThread(Socket cliente, String ipCliente, Map<String, RespostaClimatica> baseDeDados) {
        this.cliente = cliente;
        this.ipCliente = ipCliente;
        this.baseDeDados = baseDeDados;
    }

    @Override
    public void run() {

        BlockingQueue<String> fila = new LinkedBlockingQueue<>();

        try (
            BufferedReader leitor = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            PrintStream escritor = new PrintStream(cliente.getOutputStream(), true)
        ) {

    
            escritor.println("Digite seu nome:");
            String nomeCliente = leitor.readLine();

            System.out.println("Cliente conectado: " + nomeCliente + " (" + ipCliente + ")");

            escritor.println("Bem-vindo, " + nomeCliente +
                    "! Perguntas: CLIMA, TEMPERATURA, UMIDADE, VENTO. Digite SAIR para encerrar.");

            Thread leituraThread = new Thread(() -> {
                try {
                    String msg;

                    while ((msg = leitor.readLine()) != null) {
                        msg = msg.toUpperCase().trim();

                        System.out.println(nomeCliente + " enviou: " + msg);

                        if (msg.equals("SAIR")) {
                            System.out.println("Cliente " + nomeCliente + " solicitou desconexão.");

                            fila.put("Desconectando... Até logo!");
                            fila.put("__FIM__"); 
                            break;
                        }

                        RespostaClimatica resposta = baseDeDados.get(msg);

                        if (resposta != null) {
                            fila.put(resposta.responder());
                        } else {
                            fila.put("Comando inválido.");
                        }
                    }

                } catch (Exception e) {
                    System.err.println("Erro na leitura do cliente " + nomeCliente);
                }
            });

            Thread escritaThread = new Thread(() -> {
                try {
                    while (true) {
                        String resposta = fila.take();

                        if (resposta.equals("__FIM__")) break;

                        escritor.println(resposta);
                    }
                } catch (Exception e) {
                    System.err.println("Erro na escrita do cliente " + nomeCliente);
                }
            });

            leituraThread.start();
            escritaThread.start();

            leituraThread.join();
            escritaThread.join();

        } catch (Exception e) {
            System.err.println("Erro no cliente " + ipCliente + ": " + e.getMessage());
        } finally {
            try {
                if (cliente != null && !cliente.isClosed()) {
                    cliente.close();
                }
                System.out.println("Cliente " + ipCliente + " desconectado.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
