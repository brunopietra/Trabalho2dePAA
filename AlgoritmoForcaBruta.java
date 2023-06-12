import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

public class AlgoritmoForcaBruta {

    static int k = 3;

    static List<Loja> melhorRota = new ArrayList<>();
    static double gastoGasolinaMelhorCaso = 100000000;
    static List<Double> gastoGasolinaPorEstrada = new ArrayList<>();
    static Caminhao caminhao = new Caminhao();

    
    public static void main(String[] args) {
        long tempoInicial = System.currentTimeMillis();

        System.out.println("Forca Bruta!");

        List<Loja> lojas = lerArquivo("lojas.txt"); //le o arquivo e armazena seus dados em um array do tipo loja. Cada loja contem um id, suas coordenadas e itens para fornecer a outras lojas
        
        boolean estourouCarga = false; //variavel que serve para informar se a carga do caminhao foi excedida

        calcularMelhorRota(new ArrayList<>(), 0, lojas, estourouCarga); //calcula a melhor rota com base no custo de gasolina

        setGastoGasolinaPorEstrada(); //calcula o gasto de gasolina entre as lojas


        //------------------------------------------------------inicio da exibicao de resultados no terminal------------------------------------------------------------


        System.out.print("\nMelhor rota: ");
        for (Loja loja : melhorRota){
            System.out.print(loja.getId() + " ");
        }

        System.out.println("\nGasto de gasolina na melhor rota: " + String.format("%.2f", gastoGasolinaMelhorCaso) + " litros");

        System.out.println("Gasto de gasolina ao longo do percurso: ");

        for (double gastoGasolina : gastoGasolinaPorEstrada){
            System.out.println(gastoGasolina);
        }

        long tempoFinal = System.currentTimeMillis();

        long tempoTotal = tempoFinal - tempoInicial;

        System.out.println("Tempo de execução: " + tempoTotal + " milissegundos"); //exibe o tempo de execucao do codigo



        //------------------------------------------------------fim da exibicao de resultados no terminal--------------------------------------------------------------


        Grafico grafico = new Grafico(melhorRota, gastoGasolinaMelhorCaso, gastoGasolinaPorEstrada); //exibe um grafico com base na melhor rota, seu custo total e o custo entre lojas

    }

    public static void calcularMelhorRota(List<Loja> rota, double gastoGasolina, List<Loja> lojas, boolean estourouCarga){
        if (rota.size() == 0){
            rota.add(lojas.get(0)); //A rota deve sempre começar na loja 0. Ao inserir uma loja na rota, nesse caso, é necessário atualizar apenas a carga do caminhao (não se atualiza o gasto de gasolina já que se começa com o caminhão na loja)
            caminhao.atualizarCarga(lojas.get(0));
        }
    
        if (rota.size() == lojas.size()){
            rota.add(lojas.get(0)); //A rota deve sempre terminar na loja 0. Ao inserir uma loja na rota, geralmente, é necessário atualizar a carga do caminhao e o gasto de gasolina
            double novoGastoGasolina = gastoGasolina + calcularGastoGasolinaAdicional(rota.get(rota.size() - 1), rota.get(rota.size() - 2));
            caminhao.atualizarCarga(lojas.get(0));
    
            if (caminhao.getCargaAtual().size() == 0 && !estourouCarga){ //se a carga estiver vazia, isso significa que o caminhao entregou todos os itens e, se a carga do caminhao não estourou o limite, significa que a rota foi valida
                if (novoGastoGasolina < gastoGasolinaMelhorCaso){ //se o novo gasto da nova rota for menor que o antigo melhor gasto, este passará a ser o atual menor gasto e a melhor rota receberá o valor da rota recem calculada
                    gastoGasolinaMelhorCaso = novoGastoGasolina;
                    melhorRota = new ArrayList<>(rota);
                }
            }
    
            //Ao remover uma loja da rota, deve-se atualizar as variaveis relacionadas a ela, que são, nesse caso, a carga do caminhão

            rota.remove(rota.size() - 1); 
            caminhao.setCargaAtual(new ArrayList<>());
        }
    
        for (int i = 1; i < lojas.size(); i++){
            if (!rota.contains(lojas.get(i))){ //criterio para evitar repeticoes

                //Ao inserir uma loja na rota, deve-se atualizar as variaveis relacionadas a ela, que são, nesse caso, o gasto de gasolina e a carga do caminhão

                rota.add(lojas.get(i));
                double novoGastoGasolina = gastoGasolina + calcularGastoGasolinaAdicional(rota.get(rota.size() - 1), rota.get(rota.size() - 2));
                
                Caminhao caminhaoVersaoAnterior = new Caminhao();
                caminhaoVersaoAnterior.setCargaAtual(new ArrayList<>(caminhao.getCargaAtual()));
    
                caminhao.atualizarCarga(lojas.get(i));
    
                if (caminhao.getCargaAtual().size() > k){ //a carga do caminhao tera estourado caso ela ultrapasse o valor da variável k
                    estourouCarga = true;
                }
    
                calcularMelhorRota(rota, novoGastoGasolina, lojas, estourouCarga);
                
                //Ao remover uma loja da rota, deve-se atualizar as variaveis relacionadas a ela, que são, nesse caso, o gasto de gasolina e a carga do caminhão
    
                caminhao.setCargaAtual(new ArrayList<>(caminhaoVersaoAnterior.getCargaAtual()));
                rota.remove(rota.size() - 1);
                estourouCarga = false;
            }
        }
    }

    private static double calcularGastoGasolinaAdicional(Loja l1, Loja l2){
        double pitagoras = Math.sqrt(Math.pow(l1.getCoordenadaX() - l2.getCoordenadaX(), 2) + Math.pow(l1.getCoordenadaY() - l2.getCoordenadaY(), 2));

        double gastoGasolina = pitagoras / (10 - (0.5 * caminhao.getCargaAtual().size())); 

        return gastoGasolina;
    }

    public static void setGastoGasolinaPorEstrada(){
        caminhao.setCargaAtual(new ArrayList<>());
        caminhao.atualizarCarga(melhorRota.get(0));
        double gastoGasolina = 0;
        for (int i = 1; i < melhorRota.size(); i++){
            gastoGasolina = calcularGastoGasolinaAdicional(melhorRota.get(i), melhorRota.get(i - 1));
            gastoGasolinaPorEstrada.add(gastoGasolina);
            caminhao.atualizarCarga(melhorRota.get(i));
        }
    }

    
    
    public static List<Loja> lerArquivo(String nomeArquivo) {
        List<Loja> lojas = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(nomeArquivo))) {
            String linha;
            
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(" ");
                int id = Integer.parseInt(partes[0]);
                int coordenadaX = Integer.parseInt(partes[1]);
                int coordenadaY = Integer.parseInt(partes[2]);
                
                List<Integer> lojasReceber = new ArrayList<>();
                
                for (int i = 3; i < partes.length; i++) {
                    lojasReceber.add(Integer.parseInt(partes[i]));
                    
                }
                
                Loja loja = new Loja(id, coordenadaX, coordenadaY, lojasReceber);
                lojas.add(loja);
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }
        
        return lojas;
    }
}




