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

package nova.core.render;

import org.junit.Test;

import static nova.core.render.Color.ColorRangeException;
import static nova.core.render.Color.black;
import static nova.core.render.Color.darkGray;
import static nova.core.render.Color.rgb;
import static nova.core.render.Color.rgba;
import static nova.core.render.Color.rgbac;
import static nova.core.render.Color.rgbaf;
import static nova.core.render.Color.rgbafc;
import static nova.core.render.Color.rgbc;
import static nova.core.render.Color.rgbf;
import static nova.core.render.Color.rgbfc;
import static nova.core.render.Color.white;
import static nova.testutils.NovaAssertions.assertThat;

public class ColorTest {
	
	@Test
	public void testRGBC() {
		assertThat(rgbc(300, 300, 300)).isEqualTo(white);
		assertThat(rgbc(-300, -300, -300)).isEqualTo(black);
	}

	@Test
	public void testRGB() {
		assertThat(rgb(255, 255, 255)).isEqualTo(white);
		assertThat(rgb(0, 0, 0)).isEqualTo(black);
	}

	@Test(expected = ColorRangeException.class)
	public void testRGBError() {
		rgb(255, 255, 300);
	}

	@Test
	public void testRGBfC() {
		assertThat(rgbfc(300, 300, 300)).isEqualTo(white);
		assertThat(rgbfc(-300, -300, -300)).isEqualTo(black);
		assertThat(rgbfc(.25, .25, .25)).isEqualTo(darkGray);
	}

	@Test
	public void testRGBf() {
		assertThat(rgbf(1, 1, 1)).isEqualTo(white);
		assertThat(rgbf(0, 0, 0)).isEqualTo(black);
		assertThat(rgbf(.25, .25, .25)).isEqualTo(darkGray);
	}

	@Test(expected = ColorRangeException.class)
	public void testRGBfError() {
		rgbf(-1, 255, 300);
	}

	@Test
	public void testRGBPacked() {
		assertThat(rgb(0xFFFFFF)).isEqualTo(white);
		assertThat(rgb(0x000000)).isEqualTo(black);
	}

	@Test
	public void testRGBAC() {
		assertThat(rgbac(255, 255, 255, 300)).isEqualTo(white);
		assertThat(rgbac(0, 0, 0, -100)).isEqualTo(black.alpha(0));
	}

	@Test
	public void testRGBA() {
		assertThat(rgba(255, 255, 255, 255)).isEqualTo(white);
		assertThat(rgba(0, 0, 0, 255)).isEqualTo(black);
	}

	@Test(expected = ColorRangeException.class)
	public void testRGBAError() {
		rgba(256, 255, 300, -100);
	}

	public void testRGBAfC() {
		assertThat(rgbafc(300, 300, 300, 200)).isEqualTo(white);
		assertThat(rgbafc(-300, -300, -300, 100)).isEqualTo(black);
		assertThat(rgbafc(.25, .25, .25, 1)).isEqualTo(darkGray);
		assertThat(rgbafc(.25, .25, .25, .25).alpha()).isEqualTo(64);
	}

	@Test
	public void testRGBAf() {
		assertThat(rgbaf(1, 1, 1, 1)).isEqualTo(white);
		assertThat(rgbaf(0, 0, 0, 1)).isEqualTo(black);
	}

	@Test(expected = ColorRangeException.class)
	public void testRGBAfError() {
		rgbaf(255, -0.01, 300, -100);
	}

}