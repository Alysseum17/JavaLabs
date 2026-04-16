package lab6;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class ConwayPanel extends JPanel {
    private boolean[][] grid;
    private static final int CELL_SIZE = 8;
    private final int rows, cols;
    private int generation = 0;

    public ConwayPanel(int cols, int rows) {
        this.cols = cols; this.rows = rows;
        grid = new boolean[rows][cols];
        setPreferredSize(new Dimension(cols * CELL_SIZE, rows * CELL_SIZE));
        setBackground(Color.WHITE);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int c = e.getX()/CELL_SIZE, r = e.getY()/CELL_SIZE;
                if (r>=0 && r<rows && c>=0 && c<cols) {
                    grid[r][c] = !grid[r][c];
                    repaint();
                }
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int c = e.getX()/CELL_SIZE, r = e.getY()/CELL_SIZE;
                if (r>=0 && r<rows && c>=0 && c<cols) {
                    grid[r][c] = true;
                    repaint();
                }
            }
        });
    }

    public void step() {
        boolean[][] next = new boolean[rows][cols];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++) {
                int n = countNeighbors(i, j);
                next[i][j] = grid[i][j] ? (n==2||n==3) : (n==3);
            }
        grid = next;
        generation++;
        repaint();
    }

    public void reset() { grid = new boolean[rows][cols]; generation=0; repaint(); }
    public int getGeneration() { return generation; }

    private int countNeighbors(int r, int c) {
        int count = 0;
        for (int dr=-1; dr<=1; dr++)
            for (int dc=-1; dc<=1; dc++) {
                if (dr==0 && dc==0) continue;
                int nr=r+dr, nc=c+dc;
                if (nr>=0&&nr<rows&&nc>=0&&nc<cols&&grid[nr][nc]) count++;
            }
        return count;
    }

    @Override
    protected void paintComponent(Graphics gg) {
        super.paintComponent(gg);
        Graphics2D g = (Graphics2D) gg;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        for (int i=0; i<rows; i++)
            for (int j=0; j<cols; j++)
                if (grid[i][j]) {
                    g.setColor(new Color(30,80,180));
                    g.fillRect(j*CELL_SIZE+1,i*CELL_SIZE+1,CELL_SIZE-2,CELL_SIZE-2);
                }
        g.setColor(new Color(220,220,220));
        for (int i=0;i<=rows;i++)
            g.drawLine(0,i*CELL_SIZE,cols*CELL_SIZE,i*CELL_SIZE);
        for (int j=0;j<=cols;j++)
            g.drawLine(j*CELL_SIZE,0,j*CELL_SIZE,rows*CELL_SIZE);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial",Font.BOLD,12));
        g.drawString("Покоління: "+generation, 4, 15);
    }
}
