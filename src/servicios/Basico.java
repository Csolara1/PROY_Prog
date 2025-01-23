package servicios;

// Importación de clases necesarias para la interfaz gráfica y eventos
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// Clase principal que extiende JPanel e implementa KeyListener y ActionListener
public class Basico extends JPanel implements KeyListener, ActionListener {
    private static final long serialVersionUID = 1L; // ID de serialización para la clase

    // Variables de estado del juego
    private boolean jugar = false; // Indica si el juego está en curso
    private int puntuacion = 0; // Puntuación del jugador
    private int ladrillosTotales; // Número total de ladrillos
    private int vidas = 3; // Número de vidas del jugador
    private int limiteTiempo = 10 * 60 * 1000; // Límite de tiempo en milisegundos (10 minutos)

    // Temporizador para el juego
    private Timer temporizador;
    private int retraso = 10; // Intervalo de tiempo del temporizador (10 ms)

    // Posición y dirección del jugador y la bola
    private int jugadorX = 310; // Posición horizontal del jugador
    private int bolaPosX = 120; // Posición horizontal de la bola
    private int bolaPosY = 370; // Posición vertical de la bola
    private int bolaDirX = -1; // Dirección horizontal de la bola
    private int bolaDirY = -1; // Dirección vertical de la bola

    // Generador de mapa para los ladrillos
    private GeneradorMapa mapa;
    private int tiempoJuego = 0; // Tiempo transcurrido en el juego

    // Constructor de la clase Basico
    public Basico() {
        mapa = new GeneradorMapa(4, 5); // Inicializa el mapa con 4 filas y 5 columnas de ladrillos
        ladrillosTotales = mapa.obtenerLadrillosTotales(); // Obtiene el número total de ladrillos
        addKeyListener(this); // Añade el KeyListener a este panel
        setFocusable(true); // Permite que el panel reciba eventos de teclado
        setFocusTraversalKeysEnabled(false); // Desactiva el uso de teclas de navegación
        temporizador = new Timer(retraso, this); // Inicializa el temporizador con un retraso y ActionListener
        temporizador.start(); // Comienza el temporizador
    }

    // Método para dibujar la interfaz gráfica
    @Override
    public void paint(Graphics g) {
        super.paint(g); // Llama al método de la superclase para limpiar la pantalla

        // Dibuja el fondo blanco
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Dibuja los ladrillos
        mapa.dibujar((Graphics2D) g);

        // Muestra la puntuación, vidas y tiempo restante
        g.setColor(Color.BLACK);
        g.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        g.drawString("Puntuación: " + puntuacion, 20, 30);
        g.drawString("Vidas: " + vidas, 20, 60);
        g.drawString("Tiempo: " + (limiteTiempo - tiempoJuego) / 1000 + "s", 300, 30);

        // Dibuja la pala del jugador
        g.fillRect(jugadorX, getHeight() - 40, 100, 10);

        // Dibuja la bola
        g.setColor(Color.RED);
        g.fillOval(bolaPosX, bolaPosY, 20, 20);

        // Muestra mensajes de fin de juego si corresponde
        if (ladrillosTotales == 0 || vidas <= 0 || tiempoJuego >= limiteTiempo) {
            jugar = false; // Detiene el juego
            bolaDirX = bolaDirY = 0; // Detiene la bola
            String mensaje = (ladrillosTotales == 0) ? "¡Ganaste!" : "Fin del Juego"; // Mensaje de victoria o derrota
            g.setColor(ladrillosTotales == 0 ? Color.GREEN : Color.RED); // Color del mensaje
            g.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
            g.drawString(mensaje, 260, 300); // Dibuja el mensaje
            g.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
            g.drawString("Presiona Enter para reiniciar", 230, 350); // Instrucción para reiniciar
        }

        g.dispose(); // Libera recursos gráficos
    }

    // Método que se ejecuta cada vez que el temporizador genera un evento
    @Override
    public void actionPerformed(ActionEvent e) {
        if (jugar) { // Si el juego está en curso
            // Detecta colisión entre la bola y la pala del jugador
            if (new Rectangle(bolaPosX, bolaPosY, 20, 20)
                    .intersects(new Rectangle(jugadorX, getHeight() - 40, 100, 10))) {
                bolaDirY = -bolaDirY; // Cambia la dirección vertical de la bola
            }

            verificarColisionLadrillo(); // Verifica colisiones con los ladrillos

            // Actualiza la posición de la bola
            bolaPosX += bolaDirX;
            bolaPosY += bolaDirY;

            // Rebotes en las paredes laterales
            if (bolaPosX < 0 || bolaPosX > getWidth() - 20) bolaDirX = -bolaDirX;
            if (bolaPosY < 0) bolaDirY = -bolaDirY;

            // Verifica si la bola cae por debajo de la pantalla
            if (bolaPosY > getHeight() - 30) {
                vidas--; // Reduce una vida
                puntuacion = Math.max(0, puntuacion - 5); // Reduce la puntuación
                reiniciarPosicionBola(); // Reinicia la posición de la bola
            }
        }

        // Incrementa el tiempo de juego
        if (tiempoJuego < limiteTiempo) {
            tiempoJuego += retraso;
        } else {
            jugar = false; // Detiene el juego si se alcanza el límite de tiempo
            temporizador.stop(); // Detiene el temporizador
        }

        repaint(); // Redibuja el panel
    }

    // Verifica colisiones entre la bola y los ladrillos
    private void verificarColisionLadrillo() {
        A: for (int i = 0; i < mapa.obtenerNumeroFilas(); i++) {
            for (int j = 0; j < mapa.obtenerNumeroColumnas(); j++) {
                if (mapa.obtenerValorLadrillo(i, j) > 0) { // Si el ladrillo no está roto
                    int ladrilloX = j * mapa.obtenerAnchoLadrillo() + 80;
                    int ladrilloY = i * mapa.obtenerAltoLadrillo() + 100;
                    Rectangle ladrilloRect = new Rectangle(ladrilloX, ladrilloY, mapa.obtenerAnchoLadrillo(), mapa.obtenerAltoLadrillo());
                    Rectangle bolaRect = new Rectangle(bolaPosX, bolaPosY, 20, 20);

                    if (bolaRect.intersects(ladrilloRect)) { // Si hay colisión
                        mapa.establecerValorLadrillo(0, i, j); // Rompe el ladrillo
                        ladrillosTotales--; // Reduce el número de ladrillos
                        puntuacion += 10; // Incrementa la puntuación

                        // Cambia la dirección de la bola según la colisión
                        if (bolaPosX + 19 <= ladrilloRect.x || bolaPosX + 1 >= ladrilloRect.x + ladrilloRect.width) {
                            bolaDirX = -bolaDirX;
                        } else {
                            bolaDirY = -bolaDirY;
                        }

                        break A; // Sale del bucle
                    }
                }
            }
        }
    }

    // Reinicia la posición de la bola y la pala
    private void reiniciarPosicionBola() {
        bolaPosX = 120;
        bolaPosY = 370;
        bolaDirX = -1;
        bolaDirY = -1;
        jugadorX = 310;
    }

    // Métodos del KeyListener (no utilizados)
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}

    // Detecta pulsaciones de teclas
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) { // Mueve la pala a la derecha
            jugadorX = Math.min(jugadorX + 20, getWidth() - 100);
            jugar = true; // Inicia el juego si no está en curso
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) { // Mueve la pala a la izquierda
            jugadorX = Math.max(jugadorX - 20, 10);
            jugar = true; // Inicia el juego si no está en curso
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) { // Reinicia el juego
            if (!jugar) {
                jugar = true;
                if (vidas <= 0) reiniciarJuego(); // Si no hay vidas, reinicia el juego
            }
        }
    }

    // Reinicia todos los parámetros del juego
    private void reiniciarJuego() {
        reiniciarPosicionBola();
        puntuacion = 0;
        vidas = 3;
        mapa = new GeneradorMapa(4, 5); // Reinicia el mapa
        ladrillosTotales = mapa.obtenerLadrillosTotales(); // Recalcula el total de ladrillos
        tiempoJuego = 0; // Reinicia el tiempo
        temporizador.start(); // Reinicia el temporizador
    }

    // Clase interna para generar y manejar el mapa de ladrillos
    public class GeneradorMapa {
        private int[][] mapa; // Matriz de ladrillos
        private int anchoLadrillo; // Ancho de cada ladrillo
        private int altoLadrillo; // Alto de cada ladrillo

        // Constructor del generador de mapas
        public GeneradorMapa(int filas, int columnas) {
            mapa = new int[filas][columnas]; // Crea la matriz con el número de filas y columnas
            for (int[] fila : mapa) {
                java.util.Arrays.fill(fila, 1); // Llena cada ladrillo con el valor 1 (no roto)
            }
            anchoLadrillo = 540 / columnas; // Calcula el ancho de cada ladrillo
            altoLadrillo = 150 / filas; // Calcula el alto de cada ladrillo
        }

        // Dibuja los ladrillos en la pantalla
        public void dibujar(Graphics2D g) {
            for (int i = 0; i < mapa.length; i++) {
                for (int j = 0; j < mapa[0].length; j++) {
                    if (mapa[i][j] > 0) { // Si el ladrillo no está roto
                        int ladrilloX = j * anchoLadrillo + 80;
                        int ladrilloY = i * altoLadrillo + 100;
                        g.setColor(Color.MAGENTA);
                        g.fillRect(ladrilloX, ladrilloY, anchoLadrillo, altoLadrillo); // Dibuja el ladrillo
                        g.setStroke(new BasicStroke(3)); // Define el grosor del borde
                        g.setColor(Color.BLACK);
                        g.drawRect(ladrilloX, ladrilloY, anchoLadrillo, altoLadrillo); // Dibuja el borde del ladrillo
                    }
                }
            }
        }

        // Cuenta el total de ladrillos no rotos
        public int obtenerLadrillosTotales() {
            return (int) java.util.Arrays.stream(mapa)
                    .flatMapToInt(java.util.Arrays::stream)
                    .filter(val -> val > 0)
                    .count();
        }

        // Devuelve el número de filas del mapa
        public int obtenerNumeroFilas() {
            return mapa.length;
        }

        // Devuelve el número de columnas del mapa
        public int obtenerNumeroColumnas() {
            return mapa[0].length;
        }

        // Devuelve el valor de un ladrillo en una posición específica
        public int obtenerValorLadrillo(int fila, int columna) {
            return mapa[fila][columna];
        }

        // Establece el valor de un ladrillo en una posición específica
        public void establecerValorLadrillo(int valor, int fila, int columna) {
            mapa[fila][columna] = valor;
        }

        // Devuelve el ancho de los ladrillos
        public int obtenerAnchoLadrillo() {
            return anchoLadrillo;
        }

        // Devuelve el alto de los ladrillos
        public int obtenerAltoLadrillo() {
            return altoLadrillo;
        }
    }
}
