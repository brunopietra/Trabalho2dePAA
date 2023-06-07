import java.util.List;

public class Loja {
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
