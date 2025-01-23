package controladores;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import servicios.Basico;
import servicios.Medio;

public class Inicio {

    public static void main(String[] args) {

        JFrame marcoMenu = new JFrame("Menú de Brick Breaker");
        marcoMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        marcoMenu.setSize(400, 400);
        marcoMenu.setLocationRelativeTo(null);
        
        JPanel panelMenu = new JPanel(new GridBagLayout());
        panelMenu.setBackground(Color.BLACK);
        
        GridBagConstraints gbc = new GridBagConstraints();
        
        JLabel etiqueta = new JLabel("Selecciona el nivel:");
        etiqueta.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        etiqueta.setForeground(Color.WHITE);

        gbc.gridx = 0;
        gbc.gridy = 0;

        gbc.insets = new Insets(20, 0, 10, 0);

        panelMenu.add(etiqueta, gbc);

        JPanel panelBotones = new JPanel(new GridLayout(2, 1, 10, 10));
        panelBotones.setBackground(Color.BLACK);
        
        JButton botonBasico = crearBoton("Básico", marcoMenu);

        JButton botonMedio = crearBoton("Medio", marcoMenu);

        panelBotones.add(botonBasico);
        panelBotones.add(botonMedio);

        gbc.gridy = 1;

        panelMenu.add(panelBotones, gbc);
        marcoMenu.add(panelMenu);
        marcoMenu.setVisible(true);
    }

    private static JButton crearBoton(String nivel, JFrame marcoMenu) {

        JButton boton = new JButton(nivel);
        boton.setPreferredSize(new Dimension(200, 50));
        boton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                marcoMenu.setVisible(false);
                mostrarMenuJuego(nivel);
            }
        });
        return boton;
    }

    private static void mostrarMenuJuego(String nivel) {

        JFrame marcoMenuJuego = new JFrame("Menú de Juego");
        marcoMenuJuego.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        marcoMenuJuego.setSize(400, 400);
        marcoMenuJuego.setLocationRelativeTo(null);
        
        JPanel panelMenuJuego = new JPanel(new BorderLayout());
        panelMenuJuego.setBackground(Color.BLACK);

        JButton botonIniciar = new JButton("Iniciar Partida");

        botonIniciar.setPreferredSize(new Dimension(200, 50));
        botonIniciar.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
        botonIniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                marcoMenuJuego.dispose();

                JFrame marcoJuego = new JFrame();
                marcoJuego.setBounds(10, 10, 700, 600);
                marcoJuego.setTitle("Brick Breaker - " + nivel);
                marcoJuego.setResizable(false);
                marcoJuego.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                marcoJuego.add(nivel.equals("Básico") ? new Basico() : new Medio());
                marcoJuego.setVisible(true);
            }
        });

        JButton botonVolver = new JButton("Volver");
        botonVolver.setPreferredSize(new Dimension(100, 50));
        botonVolver.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        botonVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                marcoMenuJuego.dispose();
                main(null);
            }
        });

        JPanel panelBotones = new JPanel(new GridLayout(2, 1, 10, 10));

        panelBotones.setBackground(Color.BLACK);
        panelBotones.add(botonIniciar);
        panelBotones.add(botonVolver);
        panelMenuJuego.add(panelBotones, BorderLayout.CENTER);
        marcoMenuJuego.add(panelMenuJuego);
        marcoMenuJuego.setVisible(true);
    }
}
