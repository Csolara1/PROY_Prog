package servicios;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Medio extends JPanel implements KeyListener, ActionListener {
    private static final long serialVersionUID = 1L;
    private boolean jugando = false; // Bandera para verificar si el juego está en curso
    private int puntuacion = 0; // Puntuación del jugador
    private int ladrillosTotales; // Número total de ladrillos en el mapa
    private int vidas = 3; // Número de vidas del jugador
    private int limiteTiempo = 5 * 60 * 1000; // Límite de tiempo en milisegundos (5 minutos)

    private Timer temporizador; // Temporizador para actualizar el juego
    private int retraso = 7; // Retraso en milisegundos entre actualizaciones

    private int jugadorX = 310; // Posición X del jugador
    private int jugadorY1 = 500; // Posición Y para el primer lado del jugador
    private int jugadorY2 = 70; // Posición Y para el segundo lado del jugador
    private int bolaPosX = 370; // Posición X de la bola
    private int bolaPosY = 370; // Posición Y de la bola
    private int bolaDirX = -1; // Dirección X de la bola
    private int bolaDirY = -2; // Dirección Y de la bola

    private GeneradorDeMapa mapa; // Mapa de ladrillos del juego
    private int tiempoJuego = 0; // Tiempo que ha transcurrido en el juego

    public Medio() {
        mapa = new GeneradorDeMapa(5, 6); // Genera un mapa con 5 filas y 6 columnas de ladrillos
        ladrillosTotales = mapa.obtenerLadrillosTotales(); // Obtiene el número total de ladrillos
        addKeyListener(this); // Agrega el KeyListener para detectar teclas
        setFocusable(true); // Habilita la detección de teclado
        setFocusTraversalKeysEnabled(false); // Desactiva las teclas de navegación
        temporizador = new Timer(retraso, this); // Crea un temporizador con el retraso especificado
        temporizador.start(); // Inicia el temporizador
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.WHITE); // Establece el color de fondo
        g.fillRect(0, 0, getWidth(), getHeight()); // Dibuja el fondo

        mapa.dibujar((Graphics2D) g); // Dibuja el mapa de ladrillos

        g.setColor(Color.BLACK); // Establece el color para la puntuación y el tiempo
        g.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        g.drawString("Puntuación: " + puntuacion, 20, 30);
        g.drawString("Vidas: " + vidas, 20, 60);
        g.drawString("Tiempo: " + (limiteTiempo - tiempoJuego) / 1000 + "s", 300, 30); // Muestra el tiempo restante

        g.setColor(Color.BLACK);
        g.fillRect(jugadorX, getHeight() - 30, 100, 10); // Dibuja la plataforma del jugador (parte inferior)
        g.fillRect(jugadorX, jugadorY2, 100, 10); // Dibuja la plataforma del jugador (parte superior)

        g.setColor(Color.RED); // Establece el color para la bola
        g.fillOval(bolaPosX, bolaPosY, 20, 20); // Dibuja la bola

        // Condición para terminar el juego
        if (ladrillosTotales <= 0 || vidas <= 0 || tiempoJuego >= limiteTiempo) {
            jugando = false; // Detiene el juego
            bolaDirX = 0; // Detiene la dirección de la bola
            bolaDirY = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
            g.drawString("Fin del juego", 260, 300); // Muestra el mensaje de fin del juego
            g.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
            g.drawString("Presiona Enter para reiniciar", 230, 350); // Muestra la opción para reiniciar el juego
        }

        g.dispose(); // Libera recursos gráficos
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (jugando) {
            // Verifica si la bola toca la plataforma inferior
            if (new Rectangle(bolaPosX, bolaPosY, 20, 20)
                    .intersects(new Rectangle(jugadorX, getHeight() - 30, 100, 10))) {
                bolaDirY = -bolaDirY; // Cambia la dirección de la bola
            }

            // Verifica si la bola toca la plataforma superior
            if (new Rectangle(bolaPosX, bolaPosY, 20, 20)
                    .intersects(new Rectangle(jugadorX, jugadorY2, 100, 10))) {
                bolaDirY = -bolaDirY; // Cambia la dirección de la bola
            }

            // Verifica si la bola toca un ladrillo
            for (int i = 0; i < mapa.obtenerCantidadDeFilas(); i++) {
                for (int j = 0; j < mapa.obtenerCantidadDeColumnas(); j++) {
                    if (mapa.obtenerValorDeLadrillo(i, j) > 0) {
                        int ladrilloX = j * mapa.obtenerAnchoDeLadrillo() + 80;
                        int ladrilloY = i * mapa.obtenerAltoDeLadrillo() + 210;
                        Rectangle rectLadrillo = new Rectangle(ladrilloX, ladrilloY, mapa.obtenerAnchoDeLadrillo(), mapa.obtenerAltoDeLadrillo());
                        Rectangle rectBola = new Rectangle(bolaPosX, bolaPosY, 20, 20);

                        if (rectBola.intersects(rectLadrillo)) {
                            mapa.establecerValorDeLadrillo(0, i, j); // Destruye el ladrillo
                            ladrillosTotales--; // Reduce el número de ladrillos restantes
                            puntuacion += 10; // Aumenta la puntuación

                            // Cambia la dirección de la bola según donde toque el ladrillo
                            if (bolaPosX + 19 <= rectLadrillo.x || bolaPosX + 1 >= rectLadrillo.x + rectLadrillo.width) {
                                bolaDirX = -bolaDirX;
                            } else {
                                bolaDirY = -bolaDirY;
                            }

                            break;
                        }
                    }
                }
            }

            bolaPosX += bolaDirX; // Actualiza la posición de la bola en X
            bolaPosY += bolaDirY; // Actualiza la posición de la bola en Y

            // Si la bola toca las paredes laterales, cambia su dirección
            if (bolaPosX < 0 || bolaPosX > getWidth() - 20) {
                bolaDirX = -bolaDirX;
            }

            // Si la bola toca la parte inferior, pierde una vida y reinicia su posición
            if (bolaPosY < 0) {
                vidas--; // Resta una vida
                puntuacion -= 5; // Resta puntos por perder la bola
                restablecerPosicionDeBola(); // Restablece la posición de la bola
                jugando = false; // Detiene el juego
            }

            // Si la bola cae fuera de la pantalla por la parte inferior, también pierde una vida
            if (bolaPosY > getHeight() - 30) {
                vidas--;
                puntuacion -= 5;
                restablecerPosicionDeBola();
                jugando = false;
            }
        }

        // Actualiza el tiempo de juego si no ha alcanzado el límite
        if (tiempoJuego < limiteTiempo * 1000) {
            tiempoJuego += retraso;
        }

        repaint(); // Redibuja el panel
    }

    private void restablecerPosicionDeBola() {
        bolaPosX = 370;
        bolaPosY = 370;
        bolaDirX = -1;
        bolaDirY = -2;
        jugadorX = 310;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        // Controla el movimiento del jugador con las teclas de flecha
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (jugadorX >= getWidth() - 100) {
                jugadorX = getWidth() - 100;
            } else {
                moverDerecha(); // Mueve al jugador a la derecha
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (jugadorX <= 10) {
                jugadorX = 10;
            } else {
                moverIzquierda(); // Mueve al jugador a la izquierda
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!jugando) {
                jugando = true;
                if (vidas <= 0) {
                    reiniciarJuego(); // Reinicia el juego si las vidas son 0
                }
            }
        }
    }

    private void reiniciarJuego() {
        bolaPosX = 370;
        bolaPosY = 370;
        bolaDirX = -1;
        bolaDirY = -2;
        jugadorX = 310;
        puntuacion = 0;
        vidas = 3;
        ladrillosTotales = 30;
        tiempoJuego = 0;
        mapa = new GeneradorDeMapa(5, 6); // Reinicia el mapa
    }

    public void moverDerecha() {
        jugando = true;
        jugadorX += 30; // Mueve al jugador 30 píxeles a la derecha
    }

    public void moverIzquierda() {
        jugando = true;
        jugadorX -= 30; // Mueve al jugador 30 píxeles a la izquierda
    }

    // Clase para generar y manejar el mapa de ladrillos
    public class GeneradorDeMapa {
        private int[][] mapa; // Matriz de valores de los ladrillos
        private int anchoLadrillo; // Ancho de cada ladrillo
        private int altoLadrillo; // Alto de cada ladrillo

        public GeneradorDeMapa(int fila, int col) {
            mapa = new int[fila][col]; // Inicializa el mapa con el número de filas y columnas especificadas
            for (int i = 0; i < fila; i++) {
                for (int j = 0; j < col; j++) {
                    mapa[i][j] = 1; // Establece que todos los ladrillos están activos (valor 1)
                }
            }
            anchoLadrillo = 540 / col; // Calcula el ancho de cada ladrillo
            altoLadrillo = 150 / fila; // Calcula el alto de cada ladrillo
        }

        public void dibujar(Graphics2D g) {
            for (int i = 0; i < mapa.length; i++) {
                for (int j = 0; j < mapa[i].length; j++) {
                    if (mapa[i][j] > 0) { // Si el ladrillo está activo (valor mayor a 0)
                        int ladrilloX = j * anchoLadrillo + 80;
                        int ladrilloY = i * altoLadrillo + 210;
                        g.setColor(Color.MAGENTA); // Establece el color del ladrillo
                        g.fillRect(ladrilloX, ladrilloY, anchoLadrillo, altoLadrillo); // Dibuja el ladrillo
                        g.setStroke(new BasicStroke(3)); // Establece el grosor del borde
                        g.setColor(Color.BLACK); // Establece el color del borde
                        g.drawRect(ladrilloX, ladrilloY, anchoLadrillo, altoLadrillo); // Dibuja el borde del ladrillo
                    }
                }
            }
        }

        // Métodos para obtener y modificar las propiedades del mapa y los ladrillos
        public int obtenerLadrillosTotales() {
            int cuenta = 0;
            for (int[] fila : mapa) {
                for (int valor : fila) {
                    if (valor > 0) { // Cuenta cuántos ladrillos están activos
                        cuenta++;
                    }
                }
            }
            return cuenta;
        }

        public int obtenerCantidadDeFilas() {
            return mapa.length; // Devuelve el número de filas
        }

        public int obtenerCantidadDeColumnas() {
            return mapa[0].length; // Devuelve el número de columnas
        }

        public int obtenerValorDeLadrillo(int fila, int col) {
            return mapa[fila][col]; // Devuelve el valor de un ladrillo en una posición específica
        }

        public void establecerValorDeLadrillo(int valor, int fila, int col) {
            mapa[fila][col] = valor; // Establece un valor para un ladrillo específico
        }

        public int obtenerAnchoDeLadrillo() {
            return anchoLadrillo; // Devuelve el ancho de cada ladrillo
        }

        public int obtenerAltoDeLadrillo() {
            return altoLadrillo; // Devuelve el alto de cada ladrillo
        }
    }
}
