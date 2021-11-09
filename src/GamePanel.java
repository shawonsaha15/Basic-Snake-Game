import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

	static final int screen_width = 600;
	static final int screen_height = 600;
	static final int unit_size = 25;
	static final int game_units = (screen_width * screen_height) / unit_size;
	static final int delay = 75;
	final int[] x = new int[game_units];
	final int[] y = new int[game_units];
	int bodyParts = 6;
	int foodEaten = 0;
	int foodX;
	int foodY;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;

	GamePanel() {
		random = new Random();
		this.setPreferredSize(new Dimension(screen_width, screen_height));
		this.setBackground(Color.BLACK);
		this.setFocusable(true);
		this.addKeyListener(new myKeyAdapter());
		startGame();
	}

	public void startGame() {
		newFood();
		running = true;
		timer = new Timer(delay, this);
		timer.start();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	public void draw(Graphics g) {
		if (running) {
//			for (int i = 0; i < screen_height / unit_size; i++) {
//				g.drawLine(i * unit_size, 0, i * unit_size, screen_height);
//				g.drawLine(0, i * unit_size, screen_width, i * unit_size);
//			}

			g.setColor(Color.YELLOW);
			g.fillOval(foodX, foodY, unit_size, unit_size);

			for (int i = 0; i < bodyParts; i++) {
				if (i == 0) {
					g.setColor(Color.GREEN);
					g.fillRect(x[i], y[i], unit_size, unit_size);
				} else {
					g.setColor(Color.BLUE);
					g.fillRect(x[i], y[i], unit_size, unit_size);
				}
			}

			// score text
			g.setColor(Color.RED);
			g.setFont(new Font("Open Sans", Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: " + foodEaten, (screen_width - metrics.stringWidth("Score: " + foodEaten)) / 2,
					g.getFont().getSize());

		} else {
			gameOver(g);
		}
	}

	public void newFood() {
		foodX = random.nextInt((int) (screen_width / unit_size)) * unit_size;
		foodY = random.nextInt((int) (screen_height / unit_size)) * unit_size;
	}

	public void move() {
		for (int i = bodyParts; i > 0; i--) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];
		}

		switch (direction) {
		case 'U':
			y[0] = y[0] - unit_size;
			break;
		case 'D':
			y[0] = y[0] + unit_size;
			break;
		case 'L':
			x[0] = x[0] - unit_size;
			break;
		case 'R':
			x[0] = x[0] + unit_size;
			break;
		}
	}

	public void checkFood() {
		if ((x[0] == foodX) && (y[0] == foodY)) {
			bodyParts++;
			foodEaten++;
			newFood();
		}
	}

	public void checkCollisions() {
		// checks if head collides with body
		for (int i = bodyParts; i > 0; i--) {
			if ((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}
		}

		// checks if head collides with left border
		if (x[0] < 0) {
			running = false;
		}

		// checks if head collides with right border
		if (x[0] == screen_width) {
			running = false;
		}

		// checks if head collides with top border
		if (y[0] < 0) {
			running = false;
		}

		// checks if head collides with down border
		if (y[0] == screen_height) {
			running = false;
		}

		if (!running) {
			timer.stop();
		}
	}

	public void gameOver(Graphics g) {
		// Score text
		g.setColor(Color.RED);
		g.setFont(new Font("Open Sans", Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: " + foodEaten, (screen_width - metrics1.stringWidth("Score: " + foodEaten)) / 2,
				g.getFont().getSize());
		// Game Over Text
		g.setColor(Color.RED);
		g.setFont(new Font("Open Sans", Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (screen_width - metrics2.stringWidth("Game Over")) / 2, screen_height / 2);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (running) {
			move();
			checkFood();
			checkCollisions();
		}
		repaint();

	}

	public class myKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if (direction != 'R') {
					direction = 'L';
				}
				break;

			case KeyEvent.VK_RIGHT:
				if (direction != 'L') {
					direction = 'R';
				}
				break;

			case KeyEvent.VK_UP:
				if (direction != 'D') {
					direction = 'U';
				}
				break;

			case KeyEvent.VK_DOWN:
				if (direction != 'U') {
					direction = 'D';
				}
				break;
			}
		}
	}

}
