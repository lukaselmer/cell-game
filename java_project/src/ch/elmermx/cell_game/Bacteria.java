package ch.elmermx.cell_game;

public class Bacteria {

	private double movementSpeed = 0.7;
	private BacteriaMovement bacteriaMovement;
	private Cell cell;
	private Game game;
	private boolean hasLeft = false;

	public Bacteria(Game game, Cell cell) {
		this.game = game;
		this.cell = cell;
	}

	public void draw() {

	}

	public BacteriaMovement leaveCell(BacteriaMovement bacteriaMovement) {
		this.bacteriaMovement = bacteriaMovement;
		cell = null;
		hasLeft = true;
		return bacteriaMovement;
	}

	public void joinCell(Cell cell) {
		hasLeft = false;
		this.cell = cell;
		cell.joinBacteria(this);
	}

	public boolean hasLeft() {
		return hasLeft;
	}

	public double getMovementSpeed() {
		return movementSpeed;
	}

}