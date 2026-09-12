import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Heart extends JPanel {
    private final List<Particle> particles = new ArrayList<>();
    private final Random random = new Random();

    private int waitTime = 0;
    private boolean returning = false;
    private int currentOrder = 0;
    private int orderTime = 0;
    private int maxOrder;
    private int returnOrder;
    //private int returnTime = 0;

    public Heart() {
        setBackground(Color.BLACK);

        createHeart();

        // Анімація 
        Timer timer = new Timer(30, e -> {
            if (forming) {
                moveParticles();

                orderTime += 30;

                if (orderTime >= 1) {
                    currentOrder++;
                    orderTime = 0;
                }

                if (isHeartFormed()) {
                    forming = false;
                    waiting = true;
                }
            }
            if (waiting) {
                waitTime += 10;
                if (waitTime >= 1000) {
                    waiting = false;
                    returning = true;
                    returnOrder = maxOrder;

                    System.out.print(returnOrder);
                } 
            }
            if (returning) {
                returnParticles();
            }
            repaint();
        });
        timer.start();
    }

    private void moveParticles() {
        for (Particle particle: particles) {
            if (particle.order <= currentOrder) {
                particle.currentX += (particle.targetX - particle.currentX) * 0.03;
                particle.currentY += (particle.targetY - particle.currentY) * 0.03;
            }
        }
    }

    private void returnParticles() {
        for (Particle particle: particles) {
            //if (particle.order == returnOrder) {

                //System.out.println("Search!");

            particle.currentX += (400 - particle.currentX) * 0.03;
            particle.currentY += (400 - particle.currentY) * 0.03;
            //}
        }
        //returnTime += 30;
        //if (returnTime >= 1) { 
        //    returnOrder--;
        //    returnTime = 0;
        //}
    }

    private boolean forming = true;
    private boolean waiting = false;

    private boolean isHeartFormed() {
        for (Particle particle: particles) {
            double distanceX = Math.abs(particle.targetX - particle.currentX);
            double distanceY = Math.abs(particle.targetY - particle.currentY);

            if (distanceX > 1 || distanceY > 1) {
                return false;
            }
        }
        return true;
    }

    private void createHeart() {
        int order = 0;
        for (double t = Math.PI; t < Math.PI * 3; t += 0.015) {
            double x = 16 * Math.pow(Math.sin(t), 3);
            double y = 13 * Math.cos(t) - 5 * Math.cos(2*t) - 2 * Math.cos(3*t) - Math.cos(4*t);

            // частинки контуру навколо
            for (int i = 0; i < 100; i++) {
                double noiseX = random.nextGaussian() * 0.8;
                double noiseY = random.nextGaussian() * 0.8;

                double targetX = x + noiseX;
                double targetY = y + noiseY;

                double currentX = 400;
                double currentY = 400;

                int screenX = 400 + (int) (targetX * 20);
                int screenY = 400 - (int) (targetY * 20);

                particles.add(
                    new Particle(
                        currentX,
                        currentY,
                        screenX,
                        screenY,
                        order
                    )
                );
            }
            order++;
        }
        maxOrder = order - 1;
    }

    @Override
    protected  void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );
        for (Particle particle : particles) {
            int screenX = (int) particle.currentX;
            int screenY = (int) particle.currentY;

            int size = particle.size;

            g2.setColor(
                new Color(
                    255,
                    245,
                    245,
                    particle.alpha
                )
            );

            g2.fillOval(screenX, screenY, size, size);
        }
    }
    private static class Particle {
        int size;
        int alpha;
        int order;

        // місцезнаходження зараз
        double currentX;
        double currentY;

        // місцезнаходження куди полетить
        double targetX;
        double targetY; 


        Particle(double currentX, double currentY, double targetX, double targetY, int order) {
            this.order = order;
            this.currentX = currentX;
            this.currentY = currentY;
            this.targetX = targetX;
            this.targetY = targetY;

            Random random = new Random();

            size = 1 + random.nextInt(2);
            alpha = 100 + random.nextInt(156);
        }
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("Heart");
        Heart heart = new Heart();
        frame.add(heart);
        frame.setSize(800,800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
