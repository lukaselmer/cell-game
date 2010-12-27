package ch.elmermx.cell_game;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

/**
 * @author Lukas Elmer
 * @version 1.0
 * @created 26-Dez-2010 18:31:24
 */
public class Cell extends JComponent {

	private static final long serialVersionUID = 4170187546715590730L;
	/**
	 * Bacterias created per tick, e.g. 0.2
	 */
	private double creationRate, creationStorage = 0;
	private int maxBacterias;
	private Point position;
	private List<Bacteria> bacterias;
	private Game game;
	private boolean dragging;
	private GameDrawer gameDrawer;

	public Cell(final Game game, Point position, int bacteriaAmount, double creationRate, int maxBacterias) {
		this.game = game;
		this.position = position;
		this.creationRate = creationRate;
		this.maxBacterias = maxBacterias;
		bacterias = new ArrayList<Bacteria>(bacteriaAmount);
		for (int i = 0; bacteriaAmount > i; ++i) {
			addBacteria();
		}

		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("Clicked on Cell with " + bacterias.size() + " bacterias!");
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (dragging) {
					dragging = false;
					Component destinationComp = gameDrawer.getComponentAt(e.getPoint());
					if (destinationComp instanceof Cell) {
						Cell destination = (Cell) destinationComp;
						int size = bacterias.size();
						List<Bacteria> leavingBacterias = new ArrayList(Math.max((size / 2) - 1, 0));
						for (int i = 0; i < (size / 2) - 1; i++) {
							Bacteria b = bacterias.remove(i);
							leavingBacterias.add(b);
						}
						game.addBacteriaMovement(leaveCell(leavingBacterias, destination));
						// System.out.println("from " + ((Cell)
						// e.getComponent()).bacterias.size() + " to " +
						// destinationCell.getBacterias().size());
					}
				}
			}
		});

		this.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				dragging = true;
			}
		});
	}

	public BacteriaMovement leaveCell(List<Bacteria> bacterias, Cell destination) {
		for (Bacteria bacteria : bacterias) {
			if (bacteria.hasLeft()) {
				throw new RuntimeException("Bacteria already left the cell!");
			}
		}
		BacteriaMovement bacteriaMovement = new BacteriaMovement(bacterias, this, destination, game.getTicks());
		for (Bacteria bacteria : bacterias) {
			bacteria.leaveCell(bacteriaMovement);
		}
		return bacteriaMovement;
	}

	public List<Bacteria> getBacterias() {
		return bacterias;
	}

	private void addBacteria() {
		if (bacterias.size() <= maxBacterias) {
			bacterias.add(new Bacteria(game, this));
		}
	}

	public void tick() {
		creationStorage += creationRate;
		while (creationStorage > 1) {
			creationStorage -= 1;
			addBacteria();
		}
	}

	public int calculateRadius() {
		return Math.max(15, bacterias.size());
	}

	public void paint(Graphics gOld) {
		Graphics2D g2 = (Graphics2D) gOld;
		// Point center = position;
		int r = calculateRadius();
		Point topLeft = new Point(0, 0);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		GradientPaint gradient = new GradientPaint(topLeft.x, topLeft.y, Color.BLUE, topLeft.x + r * 2, topLeft.y + r * 2, Color.BLACK);
		g2.setPaint(gradient);
		g2.fill(new Ellipse2D.Double(topLeft.x, topLeft.y, r * 2, r * 2));
		g2.setColor(Color.RED);

		String s = "" + bacterias.size();
		g2.setFont(new Font(g2.getFont().getName(), Font.BOLD, 20));

		FontMetrics fm = g2.getFontMetrics();
		int msg_width = fm.stringWidth(s);
		int ascent = fm.getMaxAscent();
		int descent = fm.getMaxDescent();
		int sX = topLeft.x + r - msg_width / 2;
		int sY = topLeft.y + r - descent / 2 + ascent / 2;

		g2.drawString(s, sX, sY);
	}

	public Point getPosition() {
		return position;
	}

	// --- Component methods -----------------------------------------

	@Override
	public int getX() {
		int r = calculateRadius();
		return position.x - r;
	}

	@Override
	public int getY() {
		int r = calculateRadius();
		return position.y - r;
	}

	@Override
	public boolean contains(int x, int y) {
		int r = calculateRadius();
		int deltax = x - position.x;
		int deltay = y - position.y;
		double distance = Math.sqrt(Math.pow(deltax, 2) + Math.pow(deltay, 2));
		return (distance <= r);
	}

	public void setGameDrawer(GameDrawer gameDrawer) {
		this.gameDrawer = gameDrawer;
	}

	public void joinBacteria(Bacteria bacteria) {
		bacterias.add(bacteria);
	}

}