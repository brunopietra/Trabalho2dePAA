import java.util.ArrayList;
import java.util.List;

public class Caminhao {
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

    public void zerarCarga(){
        cargaAtual = new ArrayList<>();
    }
}
