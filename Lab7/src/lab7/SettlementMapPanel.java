package lab7;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SettlementMapPanel extends JPanel {
    private final List<Settlement> settlements;
    private static final double MIN_LON=22, MAX_LON=40, MIN_LAT=44, MAX_LAT=53;
    private static final int MARGIN=40, W=720, H=480, MAX_R=40, MIN_R=8;

    public SettlementMapPanel(List<Settlement> settlements) {
        this.settlements = settlements;
        setPreferredSize(new Dimension(W, H));
        setBackground(new Color(220, 235, 250));
    }

    private int mapX(double lon) {
        return MARGIN+(int)((lon-MIN_LON)/(MAX_LON-MIN_LON)*(W-2*MARGIN));
    }
    private int mapY(double lat) {
        return H-MARGIN-(int)((lat-MIN_LAT)/(MAX_LAT-MIN_LAT)*(H-2*MARGIN));
    }
    private int radius(Settlement s, int maxPop) {
        return maxPop==0 ? MIN_R :
                MIN_R+(int)((double)s.getPopulation()/maxPop*(MAX_R-MIN_R));
    }
    private Color typeColor(Settlement s) {
        switch (s.getType()) {
            case "Столиця":      return new Color(200,40,40);
            case "Велике місто": return new Color(230,120,20);
            case "Місто":        return new Color(40,100,210);
            default:             return new Color(40,160,60);
        }
    }

    @Override
    protected void paintComponent(Graphics gg) {
        super.paintComponent(gg);
        Graphics2D g = (Graphics2D) gg;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(200,215,235));
        for (double lon=24; lon<=40; lon+=4)
            g.drawLine(mapX(lon),MARGIN,mapX(lon),H-MARGIN);
        for (double lat=45; lat<=52; lat+=2)
            g.drawLine(MARGIN,mapY(lat),W-MARGIN,mapY(lat));

        int maxPop = settlements.stream()
                .mapToInt(Settlement::getPopulation).max().orElse(1);

        for (Settlement s : settlements) {
            int x=mapX(s.getLongitude()), y=mapY(s.getLatitude());
            int r=radius(s,maxPop);
            Color c=typeColor(s);
            g.setColor(new Color(0,0,0,40));
            g.fillOval(x-r+3,y-r+3,2*r,2*r);
            g.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),200));
            g.fillOval(x-r,y-r,2*r,2*r);
            g.setColor(c.darker());
            g.setStroke(new BasicStroke(2f));
            g.drawOval(x-r,y-r,2*r,2*r);
            Crossroad[] crs = s.getCrossroads();
            for (int i=0; i<crs.length; i++) {
                double angle=2*Math.PI*i/Math.max(crs.length,1);
                int cx=x+(int)(r*0.55*Math.cos(angle));
                int cy=y+(int)(r*0.55*Math.sin(angle));
                g.setColor(crs[i].isRegulated()
                        ? new Color(0,200,80) : new Color(220,50,50));
                g.fillOval(cx-3,cy-3,6,6);
            }
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial",Font.BOLD,11));
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(s.getName());
            g.drawString(s.getName(), x - textWidth/2, y+r+13);
        }
        drawLegend(g);
        g.setFont(new Font("Arial",Font.BOLD,13));
        g.setColor(new Color(40,60,100));
        g.drawString("Карта населених пунктів України", MARGIN, MARGIN-10);
    }

    private void drawLegend(Graphics2D g) {
        int lx=W-175, ly=MARGIN+5;
        g.setColor(new Color(255,255,255,210));
        g.fillRoundRect(lx-8,ly-5,165,130,10,10);
        g.setColor(Color.GRAY);
        g.setStroke(new BasicStroke(1));
        g.drawRoundRect(lx-8,ly-5,165,130,10,10);
        g.setFont(new Font("Arial",Font.BOLD,11));
        g.setColor(Color.DARK_GRAY);
        g.drawString("Тип населеного пункту:", lx, ly+10);
        String[] labels={"Столиця","Велике місто","Місто","Село"};
        Color[] colors={new Color(200,40,40),new Color(230,120,20),
                new Color(40,100,210),new Color(40,160,60)};
        for (int i=0;i<labels.length;i++) {
            g.setColor(colors[i]);
            g.fillOval(lx,ly+20+i*22,14,14);
            g.setColor(Color.DARK_GRAY);
            g.setFont(new Font("Arial",Font.PLAIN,11));
            g.drawString(labels[i], lx+20, ly+32+i*22);
        }
    }
}