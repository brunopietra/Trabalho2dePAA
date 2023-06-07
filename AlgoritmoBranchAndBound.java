import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

public class AlgoritmoBranchAndBound {

    static List<Loja> melhorRota = new ArrayList<>();
    static double gastoGasolinaMelhorCaso = 100000000;
    static Caminhao caminhao = new Caminhao();

    public static void main(String[] args) {
        long tempoInicial = System.currentTimeMillis();

        System.out.println("Branch and Bound!");

        List<Loja> lojas = lerArquivo("lojas.txt");
        

        calcularMelhorRota(new ArrayList<>(), 0, lojas);

        System.out.print("\nMelhor rota: ");
        for (Loja loja : melhorRota){
            System.out.print(loja.getId() + " ");
        }

        System.out.println("\nGasto de gasolina na melhor rota: " + String.format("%.2f", gastoGasolinaMelhorCaso) + " litros");

        long tempoFinal = System.currentTimeMillis();

        long tempoTotal = tempoFinal - tempoInicial;

        System.out.println("Tempo de execução: " + tempoTotal + " milissegundos");
    }

    public static void calcularMelhorRota(List<Loja> rota, double gastoGasolina, List<Loja> lojas){
        if(rota.size() == 0){
            rota.add(lojas.get(0));
        }

        if(rota.size() == lojas.size()){
            
            rota.add(lojas.get(0));
            gastoGasolina = gastoGasolina + calcularGastoGasolinaAdicional(rota.get(rota.size() - 1), rota.get(rota.size() -2));
            caminhao.atualizarCarga(lojas.get(0));

            if(caminhao.getCargaAtual().size() == 0){
                if(gastoGasolina < gastoGasolinaMelhorCaso){
                    gastoGasolinaMelhorCaso = gastoGasolina;
                    melhorRota = new ArrayList<>();
                    melhorRota.addAll(rota);

                }
            }

            rota.remove(rota.size() -1);
            caminhao.resetarCarga();

        }

        for(int i = 1; i < lojas.size(); i++){
            if(!rota.contains(lojas.get(i))){
                rota.add(lojas.get(i));
                double novoGastoGasolina = gastoGasolina + calcularGastoGasolinaAdicional(rota.get(rota.size() - 1), rota.get(rota.size() -2));
                caminhao.atualizarCarga(lojas.get(i));

                if(caminhao.getCargaAtual().size() <= 3){
                    calcularMelhorRota(rota, novoGastoGasolina, lojas);
                }


                rota.remove(rota.size() -1);

                caminhao.resetarCarga();

            }
        }
        
    }
    
    private static double calcularGastoGasolinaAdicional(Loja l1, Loja l2){
        double pitagoras = Math.sqrt(Math.pow(l1.getCoordenadaX() - l2.getCoordenadaX(), 2) + Math.pow(l1.getCoordenadaY() - l2.getCoordenadaY(), 2));

        
        double gastoGasolina = pitagoras / (10 - (0.5 * caminhao.getCargaAtual().size())); 

        return gastoGasolina;
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
