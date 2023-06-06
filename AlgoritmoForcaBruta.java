import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

public class AlgoritmoForcaBruta {

    static List<Loja> melhorRota = new ArrayList<>();
    static double gasolinaGastaMelhorCaso = 100000000;
    static Caminhao caminhao = new Caminhao();

    public static void main(String[] args) {
        long tempoInicial = System.currentTimeMillis();

        String nomeArquivo = "lojas.txt";
        List<Loja> lojas = lerArquivo(nomeArquivo);
        

        calcularMelhorRota(new ArrayList<>(), 0, lojas);

        System.out.print("\nMelhor rota: ");
        for (Loja loja : melhorRota){
            System.out.print(loja.getId() + " ");
        }

        System.out.println("\nGato de gasolina na melhor rota: " + String.format("%.2f", gasolinaGastaMelhorCaso) + " litros");

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
            caminhao.atualizarCarga(lojas.get(0));
            if(caminhao.getCargaAtual().size() == 0){
                if(gastoGasolina < gasolinaGastaMelhorCaso){
                    gasolinaGastaMelhorCaso = gastoGasolina;
                    melhorRota = new ArrayList<>();
                    melhorRota.addAll(rota);
                }
            }

            rota.remove(rota.size() -1);
            caminhao.resetarCarga();

            return;
        }

        for(int i = 1; i < lojas.size(); i++){
            if(!rota.contains(lojas.get(i))){
                rota.add(lojas.get(i));
                caminhao.atualizarCarga(lojas.get(i));
                double novoGastoGasolina = gastoGasolina + calcularGastoGasolinaAdicional(rota.get(rota.size() - 1), rota.get(rota.size() -2));

                if(caminhao.getCargaAtual().size() < 3){
                    calcularMelhorRota(rota, novoGastoGasolina, lojas);
                }

                rota.remove(rota.size() -1);

                caminhao.resetarCarga();

            }
        }
        
    }

    
    public static double calcularGastoGasolinaAdicional(Loja l1, Loja l2){
        double pitagoras = Math.sqrt(Math.pow(l1.getCoordenadaX(), 2) + Math.pow(l1.getCoordenadaY(), 2) - (Math.sqrt(Math.pow(l2.getCoordenadaX(), 2) + Math.pow(l2.getCoordenadaY(), 2))));
        
        double gastoGasolina = pitagoras / (10 - 0.5 * caminhao.getCargaAtual().size()); 

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

class Loja {
    private int id;
    private int coordenadaX;
    private int coordenadaY;
    private List<Integer> lojasReceber;
    
    public Loja(int id, int coordenadaX, int coordenadaY, List<Integer> lojasReceber) {
        this.id = id;
        this.coordenadaX = coordenadaX;
        this.coordenadaY = coordenadaY;
        this.lojasReceber = lojasReceber;
    }
    
    public int getId() {
        return id;
    }
    
    public int getCoordenadaX() {
        return coordenadaX;
    }
    
    public int getCoordenadaY() {
        return coordenadaY;
    }
    
    public List<Integer> getLojasReceber() {
        return lojasReceber;
    }

}

class Caminhao {
    private List<Integer> cargaAtual;
    private List<Integer> cargaAntiga;
    
    public Caminhao() {
        this.cargaAtual = new ArrayList<>();
        this.cargaAntiga = new ArrayList<>();
    }
    
    public List<Integer> getCargaAtual() {
        return cargaAtual;
    }
    
    public void atualizarCarga(Loja loja) {
        cargaAntiga = cargaAtual;
        int lojaId = loja.getId();
        
        for (Integer lojaReceber : loja.getLojasReceber()) {
            if (!cargaAtual.contains(lojaReceber)) {
                cargaAtual.add(lojaReceber);
            }
        }
        
        cargaAtual.remove(Integer.valueOf(lojaId));
    }

    public void resetarCarga(){
        cargaAtual = cargaAntiga;
    }
}
