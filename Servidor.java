import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class Servidor {
    public static void main(String[] args) {
        final int PORTA = 12345;

        try {
            // Inicia um servidor na porta especificada (12345 neste caso)
            ServerSocket serverSocket = new ServerSocket(PORTA);
            // Imprime uma mensagem indicando que o servidor foi iniciado e está aguardando conexões
            System.out.println("Servidor iniciado. Aguardando conexões...");
 
            // Loop infinito para esperar por conexões de clientes
            while (true) {
                // Aguarda até que um cliente se conecte e, em seguida, aceita essa conexão
                Socket clienteSocket = serverSocket.accept();
                // Quando um cliente se conecta, imprime uma mensagem indicando a conexão bem-sucedida e o endereço do cliente
                System.out.println("Novo cliente conectado: " + clienteSocket);

                // Cria uma nova thread para lidar com as operações desse cliente específico
                ThreadCliente threadCliente = new ThreadCliente(clienteSocket);
                // Inicia a thread
                threadCliente.start();
            }
        } catch (IOException e) {
            // Em caso de exceção de entrada/saída (IO), imprime o rastreamento do erro
            e.printStackTrace();
        }
    }
}

// Classe para lidar com cada cliente em uma thread separada
class ThreadCliente extends Thread {
    private Socket clienteSocket;
    private Scanner entrada;
    private PrintWriter saida;
    private Random random;

    public ThreadCliente(Socket socket) {
        this.clienteSocket = socket;
        random = new Random();
        try {
            // Configura o fluxo de entrada para receber dados do cliente
            entrada = new Scanner(clienteSocket.getInputStream());
            // Configura o fluxo de saída para enviar dados para o cliente
            saida = new PrintWriter(clienteSocket.getOutputStream(), true);
        } catch (IOException e) {
            // Em caso de exceção de entrada/saída (IO), imprime o rastreamento do erro
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            // Envio de uma mensagem de boas-vindas para o cliente assim que se conecta
            saida.println("Bem-vindo ao jogo Pedra, Papel e Tesoura!");
            // Loop para continuar a interação até que o cliente decida sair
            while (true) {
                // Solicita ao cliente que faça uma escolha (Pedra, Papel ou Tesoura)
                saida.println("Escolha: Pedra, Papel ou Tesoura?");
                // Aguarda a resposta do cliente
                String escolhaCliente = entrada.nextLine();

                // Se o cliente escolher "sair", encerra o loop e desconecta o cliente
                if (escolhaCliente.equalsIgnoreCase("sair")) {
                    break;
                }

                // Gera uma escolha aleatória para o servidor (Pedra, Papel ou Tesoura)
                String escolhaServidor = gerarEscolhaServidor();
                // Envia as escolhas do cliente e do servidor de volta para o cliente
                saida.println("Você escolheu: " + escolhaCliente);
                saida.println("O servidor escolheu: " + escolhaServidor);

                // Determina o resultado do jogo com base nas escolhas do cliente e do servidor e envia de volta para o cliente
                String resultado = determinarResultado(escolhaCliente, escolhaServidor);
                saida.println(resultado);
            }
            // Fecha o socket do cliente após a conclusão da interação
            clienteSocket.close();
        } catch (IOException e) {
            // Em caso de exceção de entrada/saída (IO), imprime o rastreamento do erro
            e.printStackTrace();
        }
    }

    // Método para gerar uma escolha aleatória para o servidor (Pedra, Papel ou Tesoura)
    private String gerarEscolhaServidor() {
        // Usa a classe Random para gerar um número aleatório entre 0 e 2
        int escolha = random.nextInt(3);
        // Converte o número aleatório em uma escolha (0 = Pedra, 1 = Papel, 2 = Tesoura)
        switch (escolha) {
            case 0:
                return "Pedra";
            case 1:
                return "Papel";
            default:
                return "Tesoura";
        }
    }

    // Método para determinar o resultado do jogo com base nas escolhas do cliente e do servidor
    private String determinarResultado(String escolhaCliente, String escolhaServidor) {
        // Verifica se as escolhas do cliente e do servidor são iguais (empate)
        if (escolhaCliente.equalsIgnoreCase(escolhaServidor)) {
            return "Empate!";
        } else if (escolhaCliente.equalsIgnoreCase("Pedra") && escolhaServidor.equalsIgnoreCase("Tesoura") ||
                   escolhaCliente.equalsIgnoreCase("Papel") && escolhaServidor.equalsIgnoreCase("Pedra") ||
                   escolhaCliente.equalsIgnoreCase("Tesoura") && escolhaServidor.equalsIgnoreCase("Papel")) {
            // Verifica as combinações possíveis para determinar o vencedor (cliente)
            return "Você venceu!";
        } else {
            // Se não houver empate e o cliente não ganhar, então o servidor ganha
            return "Você perdeu!";
        }
    }
}
