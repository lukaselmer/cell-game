package ch.elmermx.cell_game;

public class CellVector {

	private Point destination;
	private Point source;
	private double x, y;
	private double distance;

	public CellVector(Cell source, Cell destination) {
		this.source = source.getPosition();
		this.destination = destination.getPosition();
		calculateVector();
	}

	private void calculateVector() {
		x = source.x - destination.x;
		y = source.y - destination.y;
		distance = Math.sqrt((x * x) + (y * y)); // Pythagoras
	}

	public double getDistance() {
		return distance;
	}

	public Point calculateCurrentPosition(double distancePassed) {
		double completed = distancePassed / distance * -1;
		double currentX = (this.x * completed) + source.x;
		double currentY = (this.y * completed) + source.y;
		return new Point((int) currentX, (int) currentY);
	}
}
