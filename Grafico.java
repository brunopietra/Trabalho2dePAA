import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Grafico extends JPanel {
    private static final int WIDTH = 800; // Largura do painel
    private static final int HEIGHT = 600; // Altura do painel
    private static final int MARGIN = 50; // Margem do painel para desenhar
    
    private List<Loja> melhorRota;
    private double gastoGasolinaMelhorCaso;
    private List<Double> gastoGasolinaPorEstrada;
    
    public Grafico(List<Loja> melhorRota, double gastoGasolinaMelhorCaso, List<Double> gastoGasolinaPorEstrada) {
        this.melhorRota = melhorRota;
        this.gastoGasolinaMelhorCaso = gastoGasolinaMelhorCaso;
        this.gastoGasolinaPorEstrada = gastoGasolinaPorEstrada;
        
        JFrame frame = new JFrame("Gráfico da Rota");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.getContentPane().add(this);
        frame.setVisible(true);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        int maxX = melhorRota.stream().mapToInt(Loja::getCoordenadaX).max().orElse(0);
        int maxY = melhorRota.stream().mapToInt(Loja::getCoordenadaY).max().orElse(0);
        
        // Calcula as proporções para desenhar no painel
        double scaleX = (double) (WIDTH - 2 * MARGIN) / maxX;
        double scaleY = (double) (HEIGHT - 2 * MARGIN) / maxY;
        
        // Desenha as lojas
        for (Loja loja : melhorRota) {
            int x = (int) (loja.getCoordenadaX() * scaleX) + MARGIN;
            int y = (int) (loja.getCoordenadaY() * scaleY) + MARGIN;
            
            // Desenha um círculo representando a loja
            g.setColor(Color.BLUE);
            g.fillOval(x - 5, y - 5, 10, 10);
            
            // Escreve o ID da loja próximo a ela
            g.setColor(Color.BLACK);
            g.drawString(Integer.toString(loja.getId()), x + 5, y + 5);
        }
        
        // Desenha as conexões entre as lojas na melhor rota
        g.setColor(Color.RED);
        for (int i = 0; i < melhorRota.size() - 1; i++) {
            int x1 = (int) (melhorRota.get(i).getCoordenadaX() * scaleX) + MARGIN;
            int y1 = (int) (melhorRota.get(i).getCoordenadaY() * scaleY) + MARGIN;
            int x2 = (int) (melhorRota.get(i + 1).getCoordenadaX() * scaleX) + MARGIN;
            int y2 = (int) (melhorRota.get(i + 1).getCoordenadaY() * scaleY) + MARGIN;
            
            g.drawLine(x1, y1, x2, y2);
            
            // Escreve o valor de gastoGasolinaPorEstrada acima da linha
            double gastoGasolina = gastoGasolinaPorEstrada.get(i);
            g.drawString(String.format("%.2f", gastoGasolina), (x1 + x2) / 2, (y1 + y2) / 2);
        }
        
        // Escreve o valor de gastoGasolinaMelhorCaso abaixo do gráfico
        g.setColor(Color.BLACK);
        g.drawString("Gasto de gasolina na melhor rota: " + String.format("%.2f", gastoGasolinaMelhorCaso) + " litros",
                MARGIN, HEIGHT - MARGIN);
    }
}
