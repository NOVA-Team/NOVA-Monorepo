package nova.core.render.model;

/**
 * Created by Stan on 14/02/2015.
 */
public final class StaticCubeTextureCoordinates implements CubeTextureCoordinates {
	public static final StaticCubeTextureCoordinates INSTANCE = new StaticCubeTextureCoordinates();

	private StaticCubeTextureCoordinates() {}

	public double getTopMinU() {
		return 0;
	}

	public double getTopMinV() {
		return 0;
	}

	public double getTopMaxU() {
		return 1;
	}

	public double getTopMaxV() {
		return 1;
	}

	public double getBottomMinU() {
		return 0;
	}

	public double getBottomMinV() {
		return 0;
	}

	public double getBottomMaxU() {
		return 1;
	}

	public double getBottomMaxV() {
		return 1;
	}

	public double getWestMinU() {
		return 0;
	}

	public double getWestMinV() {
		return 0;
	}

	public double getWestMaxU() {
		return 1;
	}

	public double getWestMaxV() {
		return 1;
	}

	public double getEastMinU() {
		return 0;
	}

	public double getEastMinV() {
		return 0;
	}

	public double getEastMaxU() {
		return 1;
	}

	public double getEastMaxV() {
		return 1;
	}

	public double getNorthMinU() {
		return 0;
	}

	public double getNorthMinV() {
		return 0;
	}

	public double getNorthMaxU() {
		return 1;
	}

	public double getNorthMaxV() {
		return 1;
	}

	public double getSouthMinU() {
		return 0;
	}

	public double getSouthMinV() {
		return 0;
	}

	public double getSouthMaxU() {
		return 1;
	}

	public double getSouthMaxV() {
		return 1;
	}
}
