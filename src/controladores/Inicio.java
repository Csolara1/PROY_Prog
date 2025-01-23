package controladores;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import servicios.Basico;
import servicios.Medio;

public class Inicio {

    public static void main(String[] args) {
        // Método principal que se ejecuta al iniciar la aplicación.

        JFrame marcoMenu = new JFrame("Menú de Brick Breaker");
        // Crea un nuevo JFrame titulado "Menú de Brick Breaker".

        marcoMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Establece la operación por defecto al cerrar la ventana, que termina la aplicación.

        marcoMenu.setSize(400, 400);
        // Establece el tamaño de la ventana a 400x400 píxeles.

        marcoMenu.setLocationRelativeTo(null);
        // Centra la ventana en la pantalla.

        JPanel panelMenu = new JPanel(new GridBagLayout());
        // Crea un JPanel con un diseño GridBagLayout para colocar componentes de manera flexible.

        panelMenu.setBackground(Color.BLACK);
        // Establece el fondo del panel en negro.

        GridBagConstraints gbc = new GridBagConstraints();
        // Crea un objeto GridBagConstraints para manejar las posiciones de los componentes en el GridBagLayout.

        JLabel etiqueta = new JLabel("Selecciona el nivel:");
        // Crea una etiqueta con el texto "Selecciona el nivel:".

        etiqueta.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        // Establece la fuente de la etiqueta a "Comic Sans MS", en negrita y de tamaño 20.

        etiqueta.setForeground(Color.WHITE);
        // Cambia el color del texto de la etiqueta a blanco.

        gbc.gridx = 0;
        gbc.gridy = 0;
        // Establece la posición de la etiqueta en la primera celda de la cuadrícula (fila 0, columna 0).

        gbc.insets = new Insets(20, 0, 10, 0);
        // Agrega un margen (insets) de 20 píxeles arriba, 10 píxeles abajo, y 0 píxeles a los lados.

        panelMenu.add(etiqueta, gbc);
        // Agrega la etiqueta al panel con las restricciones definidas.

        JPanel panelBotones = new JPanel(new GridLayout(2, 1, 10, 10));
        // Crea un panel para los botones con un diseño de cuadrícula de 2 filas y 1 columna, y un espaciado de 10 píxeles.

        panelBotones.setBackground(Color.BLACK);
        // Establece el fondo del panel de botones en negro.

        JButton botonBasico = crearBoton("Básico", marcoMenu);
        // Crea un botón para el nivel "Básico" usando el método `crearBoton`.

        JButton botonMedio = crearBoton("Medio", marcoMenu);
        // Crea un botón para el nivel "Medio" usando el método `crearBoton`.

        panelBotones.add(botonBasico);
        // Agrega el botón "Básico" al panel de botones.

        panelBotones.add(botonMedio);
        // Agrega el botón "Medio" al panel de botones.

        gbc.gridy = 1;
        // Cambia la posición vertical (fila) a la segunda fila para colocar el panel de botones.

        panelMenu.add(panelBotones, gbc);
        // Agrega el panel de botones al panel principal con las restricciones definidas.

        marcoMenu.add(panelMenu);
        // Agrega el panel principal a la ventana del menú.

        marcoMenu.setVisible(true);
        // Hace visible la ventana del menú.
    }

    private static JButton crearBoton(String nivel, JFrame marcoMenu) {
        // Método auxiliar para crear botones del menú principal.

        JButton boton = new JButton(nivel);
        // Crea un botón con el texto del nivel pasado como argumento.

        boton.setPreferredSize(new Dimension(200, 50));
        // Establece un tamaño preferido de 200x50 píxeles para el botón.

        boton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Agrega un ActionListener al botón para manejar el clic.

                marcoMenu.setVisible(false);
                // Oculta la ventana del menú principal.

                mostrarMenuJuego(nivel);
                // Llama al método `mostrarMenuJuego` pasando el nivel seleccionado.
            }
        });
        return boton;
        // Devuelve el botón creado.
    }

    private static void mostrarMenuJuego(String nivel) {
        // Método auxiliar para mostrar el menú del juego según el nivel seleccionado.

        JFrame marcoMenuJuego = new JFrame("Menú de Juego");
        // Crea un nuevo JFrame titulado "Menú de Juego".

        marcoMenuJuego.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Establece la operación por defecto al cerrar la ventana.

        marcoMenuJuego.setSize(400, 400);
        // Establece el tamaño de la ventana a 400x400 píxeles.

        marcoMenuJuego.setLocationRelativeTo(null);
        // Centra la ventana en la pantalla.

        JPanel panelMenuJuego = new JPanel(new BorderLayout());
        // Crea un panel con un diseño BorderLayout para organizar los componentes.

        panelMenuJuego.setBackground(Color.BLACK);
        // Establece el fondo del panel en negro.

        JButton botonIniciar = new JButton("Iniciar Partida");
        // Crea un botón para iniciar la partida.

        botonIniciar.setPreferredSize(new Dimension(200, 50));
        // Establece un tamaño preferido de 200x50 píxeles para el botón.

        botonIniciar.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
        // Establece la fuente del botón a "Comic Sans MS", en negrita y de tamaño 18.

        botonIniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Agrega un ActionListener al botón para manejar el clic.

                marcoMenuJuego.dispose();
                // Cierra la ventana del menú del juego.

                JFrame marcoJuego = new JFrame();
                // Crea un nuevo JFrame para la ventana del juego.

                marcoJuego.setBounds(10, 10, 700, 600);
                // Establece las dimensiones y posición inicial de la ventana del juego.

                marcoJuego.setTitle("Brick Breaker - " + nivel);
                // Establece el título de la ventana según el nivel seleccionado.

                marcoJuego.setResizable(false);
                // Evita que la ventana sea redimensionable.

                marcoJuego.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                // Establece la operación por defecto al cerrar la ventana.

                marcoJuego.add(nivel.equals("Básico") ? new Basico() : new Medio());
                // Agrega el panel del juego correspondiente al nivel seleccionado.

                marcoJuego.setVisible(true);
                // Hace visible la ventana del juego.
            }
        });

        JButton botonVolver = new JButton("Volver");
        // Crea un botón para volver al menú principal.

        botonVolver.setPreferredSize(new Dimension(100, 50));
        // Establece un tamaño preferido de 100x50 píxeles para el botón.

        botonVolver.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        // Establece la fuente del botón a "Comic Sans MS", en negrita y de tamaño 14.

        botonVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Agrega un ActionListener al botón para manejar el clic.

                marcoMenuJuego.dispose();
                // Cierra la ventana del menú del juego.

                main(null);
                // Llama al método `main` para volver al menú principal.
            }
        });

        JPanel panelBotones = new JPanel(new GridLayout(2, 1, 10, 10));
        // Crea un panel para los botones con un diseño de cuadrícula de 2 filas y 1 columna, y un espaciado de 10 píxeles.

        panelBotones.setBackground(Color.BLACK);
        // Establece el fondo del panel de botones en negro.

        panelBotones.add(botonIniciar);
        // Agrega el botón de iniciar partida al panel de botones.

        panelBotones.add(botonVolver);
        // Agrega el botón de volver al panel de botones.

        panelMenuJuego.add(panelBotones, BorderLayout.CENTER);
        // Agrega el panel de botones al centro del panel principal del menú de juego.

        marcoMenuJuego.add(panelMenuJuego);
        // Agrega el panel principal del menú de juego a la ventana del menú de juego.

        marcoMenuJuego.setVisible(true);
        // Hace visible la ventana del menú de juego.
    }
}
