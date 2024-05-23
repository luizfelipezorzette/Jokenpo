package jokenpo;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        // Define o endereço do servidor e a porta a ser usada
        final String ENDERECO_SERVIDOR = "localhost"; // "localhost" refere-se ao endereço do próprio computador
        final int PORTA = 12345;

        try {
            // Conecta-se ao servidor utilizando o endereço e a porta especificados
            Socket socket = new Socket(ENDERECO_SERVIDOR, PORTA);
            // Imprime uma mensagem indicando que a conexão com o servidor foi estabelecida com sucesso
            System.out.println("Conectado ao servidor.");

            // Configura um scanner para receber dados do servidor através do socket
            Scanner entrada = new Scanner(socket.getInputStream());
            // Configura um PrintWriter para enviar dados para o servidor através do socket
            PrintWriter saida = new PrintWriter(socket.getOutputStream(), true);
            // Configura um scanner para ler entrada do teclado do usuário
            Scanner teclado = new Scanner(System.in);

            // Declara variáveis para armazenar mensagens do servidor e do cliente
            String mensagemDoServidor;
            String mensagemDoCliente;

            // Loop para receber mensagens do servidor continuamente
            while (entrada.hasNextLine()) {
                // Lê uma linha da mensagem enviada pelo servidor
                mensagemDoServidor = entrada.nextLine();
                // Imprime a mensagem recebida do servidor no console do cliente
                System.out.println("Servidor: " + mensagemDoServidor);

                // Verifica se a mensagem recebida indica uma mensagem especial do servidor
                if (mensagemDoServidor.equals("Bem-vindo ao jogo Pedra, Papel e Tesoura!") ||
                    mensagemDoServidor.equals("Você venceu!") ||
                    mensagemDoServidor.equals("Você perdeu!") ||
                    mensagemDoServidor.equals("Empate!")) {
                    // Se for uma mensagem especial, continua esperando por mais mensagens do servidor
                    continue;
                }

                // Verifica se a mensagem recebida indica que o servidor encerrou a conexão
                if (mensagemDoServidor.equals("sair")) {
                    // Se o servidor indicar que o cliente deve sair, o loop é encerrado
                    break;
                }

                // Solicita entrada do usuário para enviar ao servidor
                mensagemDoCliente = teclado.nextLine();
                // Envia a mensagem do cliente para o servidor através do socket
                saida.println(mensagemDoCliente);
            }

            // Fecha os scanners e o socket após a conclusão da interação
            entrada.close();
            saida.close();
            teclado.close();
            socket.close();
        } catch (IOException e) {
            // Em caso de exceção de entrada/saída (IO), imprime o rastreamento do erro
            e.printStackTrace();
        }
    }
}
