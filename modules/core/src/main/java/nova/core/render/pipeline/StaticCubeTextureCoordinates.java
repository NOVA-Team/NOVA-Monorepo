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

	@Override
	public double getTopMinU() {
		return 0;
	}

	@Override
	public double getTopMinV() {
		return 0;
	}

	@Override
	public double getTopMaxU() {
		return 1;
	}

	@Override
	public double getTopMaxV() {
		return 1;
	}

	@Override
	public double getBottomMinU() {
		return 0;
	}

	@Override
	public double getBottomMinV() {
		return 0;
	}

	@Override
	public double getBottomMaxU() {
		return 1;
	}

	@Override
	public double getBottomMaxV() {
		return 1;
	}

	@Override
	public double getWestMinU() {
		return 0;
	}

	@Override
	public double getWestMinV() {
		return 0;
	}

	@Override
	public double getWestMaxU() {
		return 1;
	}

	@Override
	public double getWestMaxV() {
		return 1;
	}

	@Override
	public double getEastMinU() {
		return 0;
	}

	@Override
	public double getEastMinV() {
		return 0;
	}

	@Override
	public double getEastMaxU() {
		return 1;
	}

	@Override
	public double getEastMaxV() {
		return 1;
	}

	@Override
	public double getNorthMinU() {
		return 0;
	}

	@Override
	public double getNorthMinV() {
		return 0;
	}

	@Override
	public double getNorthMaxU() {
		return 1;
	}

	@Override
	public double getNorthMaxV() {
		return 1;
	}

	@Override
	public double getSouthMinU() {
		return 0;
	}

	@Override
	public double getSouthMinV() {
		return 0;
	}

	@Override
	public double getSouthMaxU() {
		return 1;
	}

	@Override
	public double getSouthMaxV() {
		return 1;
	}
}
