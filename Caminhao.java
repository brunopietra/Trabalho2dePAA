import java.util.ArrayList;
import java.util.List;

public class Caminhao {
    private List<Integer> cargaAtual;
    
    public Caminhao() {
        this.cargaAtual = new ArrayList<>();
    }
    
    public List<Integer> getCargaAtual() {
        return cargaAtual;
    }

    public void setCargaAtual(List<Integer> cargaAtual){
        this.cargaAtual = cargaAtual;
    }
    
    public void atualizarCarga(Loja loja) {
        int lojaId = loja.getId();
        
        for (Integer lojaReceber : loja.getLojasReceber()) {
            if (!cargaAtual.contains(lojaReceber)) {
                cargaAtual.add(lojaReceber);
            }
        }
        
        cargaAtual.remove(Integer.valueOf(lojaId));
    }

}
