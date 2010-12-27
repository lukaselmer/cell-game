package ch.elmermx.cell_game;

import javax.swing.JFrame;

public class GameFrame {

	private JFrame frame;
	private Game game;
	private GameDrawer gameDrawer;

	public GameFrame(Game game) {
		this.game = game;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 900, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		gameDrawer = new GameDrawer(game);
		Thread t = new Thread(gameDrawer);
		t.start();

		frame.getContentPane().add(gameDrawer);
		frame.setVisible(true);
	}

	public void remove(BacteriaMovement bm) {
		gameDrawer.remove(bm);
	}
}
