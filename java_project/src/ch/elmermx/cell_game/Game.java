package ch.elmermx.cell_game;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

/**
 * @author Lukas Elmer
 * @version 1.0
 * @created 26-Dez-2010 18:31:24
 */
public class Game extends Observable implements Runnable {

	private int ticks;
	private List<BacteriaMovement> bacteriaMovements = new ArrayList<BacteriaMovement>();
	private List<Cell> cells = new ArrayList<Cell>();
	private GameFrame gameFrame;

	public static void main(String[] args) {
		Game g = new Game();
		Thread t = new Thread(g);
		t.start();
	}

	public Game() {
		cells.add(new Cell(this, new Point(500, 200), 30, 0.012, 200));
		cells.add(new Cell(this, new Point(270, 400), 20, 0.012, 800));
		cells.add(new Cell(this, new Point(320, 300), 30, 0, 200));
		cells.add(new Cell(this, new Point(120, 130), 55, 0.015, 100));
		gameFrame = new GameFrame(this);
	}

	public void tick() {
		// System.out.println("Tick " + ticks + "...");
		ticks++;
		for (Cell c : cells) {
			c.tick();
		}
		LinkedList<BacteriaMovement> toRemove = new LinkedList<BacteriaMovement>();
		try {
			for (BacteriaMovement bm : bacteriaMovements) {
				bm.calculatePosition(ticks);
				if (bm.isMovementOver()) {
					toRemove.add(bm);
				}
			}
		} catch (ConcurrentModificationException e) {
		}
		for (BacteriaMovement bm : toRemove) {
			bacteriaMovements.remove(bm);
			gameFrame.remove(bm);
			setChanged();
		}
		notifyObservers();
	}

	public void run() {
		while (true) {
			tick();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public List<Cell> getCells() {
		return cells;
	}

	public List<BacteriaMovement> getBacteriaMovements() {
		return bacteriaMovements;
	}

	public int getTicks() {
		return ticks;
	}

	public GameFrame getGameFrame() {
		return gameFrame;
	}

	public void addBacteriaMovement(BacteriaMovement bm) {
		bacteriaMovements.add(bm);
		setChanged();
		notifyObservers(bm);
	}

}