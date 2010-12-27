package ch.elmermx.cell_game;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.util.List;
import java.util.Observable;

import javax.swing.JComponent;

/**
 * @author Lukas Elmer
 * @version 1.0
 * @created 26-Dez-2010 18:31:24
 */
public class BacteriaMovement extends JComponent {

	private int startTick;
	@SuppressWarnings("unused")
	private Cell source;
	private Cell destination;
	private CellVector movementVector;
	private List<Bacteria> bacterias;
	private boolean movementOver = false;
	private Point currentPosition;

	public BacteriaMovement(List<Bacteria> bacterias, Cell source, Cell destination, int startTick) {
		if (bacterias.size() == 0) {
			throw new RuntimeException();
		}
		this.bacterias = bacterias;
		this.source = source;
		this.destination = destination;
		this.startTick = startTick;
		this.setCurrentPosition(source.getPosition());
		movementVector = new CellVector(source, destination);
	}

	public void calculatePosition(int currentTick) {
		if (movementOver) {
			return;
		}
		double movementSpeed = bacterias.get(0).getMovementSpeed();
		int relativeTicks = currentTick - startTick;
		double distancePassed = movementSpeed * relativeTicks;
		if (movementVector.getDistance() < distancePassed) {
			if (bacterias.get(0).hasLeft()) {
				for (Bacteria b : bacterias) {
					b.joinCell(destination);
				}
				movementOver = true;
				setCurrentPosition(destination.getPosition());
			}
		} else {
			setCurrentPosition(movementVector.calculateCurrentPosition(distancePassed));
		}
	}

	public boolean isMovementOver() {
		return movementOver;
	}

	private void setCurrentPosition(Point currentPosition) {
		this.currentPosition = currentPosition;
	}

	public Point getCurrentPosition() {
		return currentPosition;
	}

	public int calculateRadius() {
		return Math.max(5, bacterias.size() / 2);
	}

	public void paint(Graphics gOld) {
		Graphics2D g2 = (Graphics2D) gOld;
		int r = calculateRadius();
		Point topLeft = new Point(0, 0);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		GradientPaint gradient = new GradientPaint(topLeft.x, topLeft.y, Color.RED, topLeft.x + r * 2, topLeft.y + r * 2, Color.PINK);
		g2.setPaint(gradient);
		g2.fill(new Ellipse2D.Double(topLeft.x, topLeft.y, r * 2, r * 2));
		g2.setColor(Color.RED);
	}

	// --- Component methods -----------------------------------------

	@Override
	public int getX() {
		return getCurrentPosition().x - calculateRadius();
	}

	@Override
	public int getY() {
		return getCurrentPosition().y - calculateRadius();
	}

	@Override
	public boolean contains(int x, int y) {
		return false;
	}

}