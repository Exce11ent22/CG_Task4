package com.vsu.course2.render;

import com.vsu.course2.render.entity.EntitiyManager;
import com.vsu.course2.render.input.ClickType;
import com.vsu.course2.render.input.Mouse;
import com.vsu.course2.render.point.MyPoint;
import com.vsu.course2.render.point.PointConverter;
import com.vsu.course2.render.shapes.MyPolygon;
import com.vsu.course2.render.shapes.Tetrahedron;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferStrategy;
import java.io.IOException;

public class Display extends Canvas implements Runnable {

    private static final long SerialVersionUID = 1L;

    private Thread thread;
    private JFrame frame;
    private static String title = "3D Render";
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    private static boolean running = false;

    private EntitiyManager entitiyManager;

    private Mouse mouse;

    public Display() {
        this.frame = new JFrame();

        Dimension size = new Dimension(WIDTH, HEIGHT);
        this.setPreferredSize(size);

        this.mouse = new Mouse();

        this.entitiyManager = new EntitiyManager();

        this.addMouseListener(this.mouse);
        this.addMouseMotionListener(this.mouse);
        this.addMouseWheelListener(this.mouse);
    }

    public static void  main(String[] args) {
        Display display = new Display();
        display.frame.setTitle(title);
        display.frame.add(display);
        display.frame.pack();
        display.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.frame.setLocationRelativeTo(null);
        display.frame.setResizable(false);
        display.frame.setVisible(true);

        display.start();
    }

    public synchronized void start(){
        running = true;
        thread = new Thread(this, "com.vsu.course2.render.Display");
        this.thread.start();
    }

    public synchronized void stop(){
        running = false;
        try {
            this.thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        final double ns = 1000000000.0 / 60;
        double delta = 0;
        int frames = 0;

        try {
            this.entitiyManager.init();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (running){
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1){
                update();
                delta--;
            }
            render();
            frames++;

            if(System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                this.frame.setTitle(title + " | " + frames + " fps");
                frames = 0;
            }
        }

        stop();
    }

    private void render(){
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0,0,WIDTH,HEIGHT);

        this.entitiyManager.render(g);

        g.dispose();
        bs.show();
    }


    private void update(){
        this.entitiyManager.update(this.mouse);
    }
}
