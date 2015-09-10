/*
 * Copyright (c) 2015 NOVA, All rights reserved.
 * This library is free software, licensed under GNU Lesser General Public License version 3
 *
 * This file is part of NOVA.
 *
 * NOVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NOVA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NOVA.  If not, see <http://www.gnu.org/licenses/>.
 */

package nova.core.render.pipeline;

/**
 * @author Stan
 */
public final class StaticCubeTextureCoordinates implements CubeTextureCoordinates {
	public static final StaticCubeTextureCoordinates instance = new StaticCubeTextureCoordinates();

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
