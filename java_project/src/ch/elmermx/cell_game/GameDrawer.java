package ch.elmermx.cell_game;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;

/**
 * @author Lukas Elmer
 * @version 1.0
 * @created 26-Dez-2010 18:31:24
 */
public class GameDrawer extends JPanel implements Runnable, Observer {

	private static final long serialVersionUID = -960623208547171654L;
	private Game game;

	public GameDrawer(Game game) {
		this.game = game;
		game.addObserver(this);
		this.setLayout(new OverlayLayout(this));
		for (Cell c : game.getCells()) {
			c.setGameDrawer(this);
			this.add(c);
		}
		// this.add(new BacteriaMovement(new Bacteria(game,
		// game.getCells().get(0)), game.getCells().get(0),
		// game.getCells().get(1), game.getTicks()));
	}

	public void run() {
		while (true) {
			repaint();
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		// g2.drawImage(background, 0, 0, this);
		g2.drawImage(new ImageIcon("data/images/background.jpg").getImage(), 0, 0, this);
		/*
		 * for (Cell c : game.getCells()) { c.paint(g); }
		 */
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof Game && arg instanceof BacteriaMovement) {
			BacteriaMovement bm = (BacteriaMovement) arg;
			this.add(bm);
			this.updateUI();
		}
	}
}