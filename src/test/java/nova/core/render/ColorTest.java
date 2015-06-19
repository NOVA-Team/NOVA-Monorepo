package nova.core.render;

import org.junit.Test;

import static nova.testutils.NovaAssertions.*;
import static nova.core.render.Color.*;

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