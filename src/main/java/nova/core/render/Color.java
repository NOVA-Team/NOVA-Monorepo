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
 */package nova.core.render;

import nova.core.util.exception.NovaException;
import nova.core.util.math.MathUtil;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Arbitrary immutable color object. Holds a color in argb space.
 */
public final class Color {
	// TODO Test me!
	/**
	 * White color.
	 */
	public static final Color white = rgb(255, 255, 255);
	/**
	 * Light gray color.
	 */
	public static final Color lightGray = rgb(192, 192, 192);
	/**
	 * Gray color.
	 */
	public static final Color gray = rgb(128, 128, 128);
	/**
	 * Dark gray color.
	 */
	public static final Color darkGray = rgb(64, 64, 64);
	/**
	 * Black color.
	 */
	public static final Color black = rgb(0, 0, 0);
	/**
	 * Red color.
	 */
	public static final Color red = rgb(255, 0, 0);
	/**
	 * Pink color.
	 */
	public static final Color pink = rgb(255, 175, 175);
	/**
	 * Orange color.
	 */
	public static final Color orange = rgb(255, 200, 0);
	/**
	 * Yellow color.
	 */
	public static final Color yellow = rgb(255, 255, 0);
	/**
	 * Green color.
	 */
	public static final Color green = rgb(0, 255, 0);
	/**
	 * Magenta color.
	 */
	public static final Color magenta = rgb(255, 0, 255);
	/**
	 * Cyan color.
	 */
	public static final Color cyan = rgb(0, 255, 255);
	/**
	 * Blue color.
	 */
	public static final Color blue = rgb(0, 0, 255);

	private final int value;

	private Color(int argb) {
		this.value = argb;
	}

	/**
	 * Creates Color instance out of integer components.
	 * Clamps input variables into 0-255 range.
	 * Uses the most opaque alpha value.
	 *
	 * @param red component.
	 * @param green component.
	 * @param blue component.
	 * @return a color instance.
	 */
	public static Color rgbc(int red, int green, int blue) {
		return rgbac(red, green, blue, 255);
	}

	/**
	 * Creates Color instance out of integer components.
	 * Uses the most opaque alpha value.
	 *
	 * @param red component.
	 * @param green component.
	 * @param blue component.
	 * @return a color instance.
	 * @throws ColorRangeException if component variables are out of 0-255 range.
	 */
	public static Color rgb(int red, int green, int blue) {
		return rgba(red, green, blue, 255);
	}

	/**
	 * Creates Color instance out of double components.
	 * Uses the most opaque alpha value.
	 * Clamps input variables into 0-1 range.
	 *
	 * @param red component.
	 * @param green component.
	 * @param blue component.
	 * @return a color instance.
	 */
	public static Color rgbfc(double red, double green, double blue) {
		return rgbafc(red, green, blue, 1F);
	}

	/**
	 * Creates Color instance out of double components.
	 * Uses the most opaque alpha value.
	 * Clamps input variables into 0-1 range.
	 *
	 * @param red component.
	 * @param green component.
	 * @param blue component.
	 * @return a color instance.
	 * @throws ColorRangeException if component variables are out of 0-1 range.
	 */
	public static Color rgbf(double red, double green, double blue) {
		return rgbaf(red, green, blue, 1F);
	}

	/**
	 * Creates Color instance out of packed RGB data.
	 *
	 * @param rgb data occupying lower 24bits.
	 * @return a color instance.
	 */
	public static Color rgb(int rgb) {
		return rgba(rgb, 255);
	}

	/**
	 * Creates Color instance out of integer components.
	 * Uses the most opaque alpha value.
	 *
	 * @param red component.
	 * @param green component.
	 * @param blue component.
	 * @param alpha component.
	 * @return a color instance.
	 * @throws ColorRangeException if component variables are out of 0-255 range.
	 */
	public static Color rgbac(int red, int green, int blue, int alpha) {
		red = MathUtil.clamp(red, 0, 255);
		green = MathUtil.clamp(green, 0, 255);
		blue = MathUtil.clamp(blue, 0, 255);
		alpha = MathUtil.clamp(alpha, 0, 255);
		int argb = alpha << 24 | red << 16 | green << 8 | blue;
		return argb(argb);
	}

	/**
	 * Creates Color instance out of integer components.
	 * Uses the most opaque alpha value.
	 *
	 * @param red component.
	 * @param green component.
	 * @param blue component.
	 * @param alpha component.
	 * @return a color instance.
	 * @throws ColorRangeException if component variables are out of 0-255 range.
	 */
	public static Color rgba(int red, int green, int blue, int alpha) {
		if (red > 255 || red < 0 || green > 255 || green < 0 || blue > 255 || blue < 0 || alpha > 255 || blue < 0) {
			throw new ColorRangeException(red, green, blue, alpha);
		}
		return rgbac(red, green, blue, alpha);
	}

	/**
	 * Creates Color instance out of double components.
	 * Uses the most opaque alpha value.
	 * Clamps input variables into 0-1 range.
	 *
	 * @param red component.
	 * @param green component.
	 * @param blue component.
	 * @param alpha component.
	 * @return a color instance.
	 */
	public static Color rgbafc(double red, double green, double blue, double alpha) {
		return rgbac(colorConvert(red), colorConvert(green), colorConvert(blue), colorConvert(alpha));
	}

	/**
	 * Creates Color instance out of double components.
	 * Uses the most opaque alpha value.
	 * Clamps input variables into 0-1 range.
	 *
	 * @param red component.
	 * @param green component.
	 * @param blue component.
	 * @param alpha component.
	 * @return a color instance.
	 * @throws ColorRangeException if component variables are out of 0-1 range.
	 */
	public static Color rgbaf(double red, double green, double blue, double alpha) {
		return rgba(colorConvert(red), colorConvert(green), colorConvert(blue), colorConvert(alpha));
	}

	private static int colorConvert(double component) {
		return (int) Math.floor(component == 1.0 ? 255d : component * 256.0);
	}
	private static int colorConvert(float component) {
		return (int) Math.floor(component == 1.0 ? 255d : component * 256.0);
	}

	/**
	 * Creates Color instance out of packed RGB data and byte alpha component.
	 *
	 * @param rgb packed into lower 24bits.
	 * @param alpha will be clamped into 0-255 range.
	 * @return a color instance.
	 */
	public static Color rgbac(int rgb, int alpha) {
		alpha = MathUtil.clamp(alpha, 0, 255);
		int value = rgb & 0xFFFFFF | alpha << 24;
		return argb(value);
	}

	/**
	 * Creates Color instance out of packed RGB data and byte alpha component.
	 *
	 * @param rgb packed into lower 24bits.
	 * @param alpha component.
	 * @return a color instance.
	 * @throws ColorRangeException if alpha component is out of 0-255 range.
	 */
	public static Color rgba(int rgb, int alpha) {
		if (alpha > 255 || alpha < 0) {
			throw new ColorRangeException(alpha);
		}
		return rgbac(rgb, alpha);
	}

	/**
	 * Creates Color instance out of packed RGB data and double alpha component.
	 *
	 * @param rgb packed into lower 24bits.
	 * @param alpha will be clamped into 0-1 range.
	 * @return a color instance.
	 */
	public static Color rgbafc(int rgb, float alpha) {
		return rgbac(rgb, colorConvert(alpha));
	}

	/**
	 * Creates Color instance out of packed RGB data and double alpha component.
	 *
	 * @param rgb packed into lower 24bits.
	 * @param alpha component.
	 * @return a color instance.
	 * @throws ColorRangeException if alpha component is out of 0-1 range.
	 */
	public static Color rgbaf(int rgb, float alpha) {
		return rgba(rgb, colorConvert(alpha));
	}

	/**
	 * Creates Color instance out of packed ARGB data.
	 *
	 * @param argb packed into 32bits.
	 * @return a color instance.
	 */
	public static Color argb(int argb) {
		return new Color(argb);
	}

	/**
	 * Creates Color instance out of packed RGBA data.
	 *
	 * @param rgba packed into 32bits.
	 * @return a color instance.
	 */
	public static Color rgba(int rgba) {
		int alpha = rgba & 0xFF;
		int argb = rgba << 8;
		argb |= alpha << 24;
		return argb(argb);
	}

	/**
	 * Creates Color instance from HSL color format.
	 *
	 * @param h component.
	 * @param s component.
	 * @param l component.
	 * @return a color instance.
	 */
	public static Color hsl(double h, double s, double l) {

		if (s == 0) {
			int c = (int) (l * 255);
			return rgbc(c, c, c);
		}

		double t1;
		if (l < 0.5F) {
			t1 = l * (1 + s);
		} else {
			t1 = l + s - l * s;
		}
		double t2 = 2 * l - t1;

		h = (h % 360F) / 360F;

		double tr = h + 1 / 3F;
		double tg = h;
		double tb = h - 1 / 3F;

		tr = tr > 1 ? tr - 1 : tr < 0 ? tr + 1 : tr;
		tg = tg > 1 ? tg - 1 : tg < 0 ? tg + 1 : tg;
		tb = tb > 1 ? tb - 1 : tb < 0 ? tb + 1 : tb;

		double r = constructColor(tr, t1, t2);
		double g = constructColor(tg, t1, t2);
		double b = constructColor(tb, t1, t2);

		return rgbfc(r, g, b);
	}

	private static double constructColor(double c, double t1, double t2) {
		if (6 * c < 1) {
			return t2 + (t1 - t2) * 6 * c;
		} else if (2 * c < 1) {
			return t1;
		} else if (3 * c < 2) {
			return t2 + (t1 - t2) * (2 / 3F - c) * 6;
		} else {
			return t2;
		}
	}

	/**
	 * Creates Color instance from HSL color format using vector as source of components.
	 *
	 * @param hsl vector which components will be used as HSL color components.
	 * X -&gt; H,
	 * Y -&gt; S,
	 * Z -&gt; L
	 * @return a color instance.
	 */
	public static Color hsl(Vector3D hsl) {
		return hsl(hsl.getX(), hsl.getY(), hsl.getZ());
	}

	/**
	 * @return packed color in ARGB format.
	 */
	public int argb() {
		return value;
	}

	/**
	 * @return packed color in RGBA format.
	 */
	public int rgba() {
		return (red() << 24) | (green() << 16) | (blue() << 8) | (alpha());
	}

	/**
	 * @return red component of the color in lowest 8bit of the integer.
	 */
	public int red() {
		return value >>> 16 & 0xFF;
	}

	/**
	 * @return green component of the color in lowest 8bit of the integer.
	 */
	public int green() {
		return value >>> 8 & 0xFF;
	}

	/**
	 * @return blue component of the color in lowest 8bit of the integer.
	 */
	public int blue() {
		return value & 0xFF;
	}

	/**
	 * @return alpha component of the color in lowest 8bit of the integer.
	 */
	public int alpha() {
		return value >>> 24 & 0xFF;
	}

	/**
	 * Creates Color instance with replaced red component.
	 *
	 * @param red a new red component.
	 * @return a new Color instance.
	 */
	public Color red(int red) {
		return rgba(red, green(), blue(), alpha());
	}

	/**
	 * Creates Color instance with replaced green component.
	 *
	 * @param green a new green component.
	 * @return a new Color instance.
	 */
	public Color green(int green) {
		return rgba(red(), green, blue(), alpha());
	}

	/**
	 * Creates Color instance with replaced blue component.
	 *
	 * @param blue a new blue component.
	 * @return a new Color instance.
	 */
	public Color blue(int blue) {
		return rgba(red(), green(), blue, alpha());
	}

	/**
	 * Creates Color instance with replaced alpha component.
	 *
	 * @param alpha a new alpha component.
	 * @return a new Color instance.
	 */
	public Color alpha(int alpha) {
		return rgba(red(), green(), blue(), alpha);
	}

	/**
	 * @return red component of the color as a normalized float.
	 */
	public float redf() {
		return red() / 255F;
	}

	/**
	 * @return green component of the color as a normalized float.
	 */
	public float greenf() {
		return green() / 255F;
	}

	/**
	 * @return blue component of the color as a normalized float.
	 */
	public float bluef() {
		return blue() / 255F;
	}

	/**
	 * @return alpha component of the color as a normalized float.
	 */
	public float alphaf() {
		return alpha() / 255F;
	}

	/**
	 * Creates Color instance with replaced red component.
	 *
	 * @param red a new red component.
	 * @return a new Color instance.
	 */
	public Color redf(float red) {
		return rgba(colorConvert(red), green(), blue(), alpha());
	}

	/**
	 * Creates Color instance with replaced green component.
	 *
	 * @param green a new green component.
	 * @return a new Color instance.
	 */
	public Color greenf(float green) {
		return rgba(red(), colorConvert(green), blue(), alpha());
	}

	/**
	 * Creates Color instance with replaced blue component.
	 *
	 * @param blue a new blue component.
	 * @return a new Color instance.
	 */
	public Color bluef(float blue) {
		return rgba(red(), green(), colorConvert(blue), alpha());
	}

	/**
	 * Creates Color instance with replaced alpha component.
	 *
	 * @param alpha a new alpha component.
	 * @return a new Color instance.
	 */
	public Color alphaf(float alpha) {
		return rgba(red(), green(), blue(), colorConvert(alpha));
	}

	/**
	 * Performs alpha color blending.
	 *
	 * @param color color to blend with.
	 * @return a new, bended, Color instance.
	 */
	public Color blend(Color color) {
		float aA = alphaf();
		float aB = color.alphaf();

		float r = (redf() * aA) + (color.redf() * aB * (1 - aA));
		float g = (greenf() * aA) + (color.greenf() * aB * (1 - aA));
		float b = (bluef() * aA ) + (color.bluef() * aB * (1 - aA));
		float a = aA + (aB * (1 - aA));
		return rgbafc(r, g, b, a);
	}

	/**
	 * Performs addition operation.
	 *
	 * @param color color to be added.
	 * @return a new Color instance.
	 */
	public Color add(Color color) {
		return rgbac(red() + color.red(), green() + color.green(), blue() + color.blue(), alpha());
	}

	/**
	 * Performs subtraction operation.
	 *
	 * @param color color to be subtracted.
	 * @return a new Color instance.
	 */
	public Color subtract(Color color) {
		return rgbac(red() - color.red(), green() - color.green(), blue() - color.blue(), alpha());
	}

	/**
	 * Performs multiplication operation.
	 *
	 * @param color color to be multiplied.
	 * @return a new Color instance.
	 */
	public Color multiply(Color color) {
		return rgbac(red() * color.red(), green() * color.green(), blue() * color.blue(), alpha());
	}

	/**
	 * Performs division operation.
	 *
	 * @param color color to be divisor.
	 * @return a new Color instance.
	 */
	public Color divide(Color color) {
		return rgbac(red() / color.red(), green() / color.green(), blue() / color.blue(), alpha());
	}

	/**
	 * Performs average operation.
	 *
	 * @param color color to second part of the average.
	 * @return a new Color instance.
	 */
	public Color average(Color color) {
		return rgbac((red() + color.red()) / 2, (green() + color.green()) / 2, (blue() + color.blue()) / 2, (alpha() + color.alpha()) / 2);
	}

	/**
	 * Performs negation operation.
	 *
	 * @return a new Color instance.
	 */
	public Color negate() {
		return rgbac(255 - red(), 255 - green(), 255 - blue(), alpha());
	}

	/**
	 * Getter for HSL components.
	 *
	 * @return a vector with HSL components.
	 */
	public Vector3D hsl() {

		float r = redf();
		float g = greenf();
		float b = bluef();

		float min = Math.min(Math.min(r, g), b);
		float max = Math.max(Math.max(r, g), b);

		float l = lighting(min, max);
		float s = saturation(min, max, l);
		float h = hue(r, g, b, min, max);

		return new Vector3D(h, s, l);
	}

	private float lighting(float min, float max) {
		return (min + max) / 2F;
	}

	private float saturation(float min, float max, float lighting) {
		float s = 0;
		if (min != max) {
			if (lighting > 0.5F) {
				s = (max - min) / (2F - max - min);
			} else {
				s = (max - min) / (max + min);
			}
		}
		return s;
	}

	private float hue(float r, float g, float b, float min, float max) {
		float h = 0;
		if (r == max) {
			h = (g - b) / (max - min);
		} else if (g == max) {
			h = (b - r) / (max - min);
		} else if (b == max) {
			h = (r - g) / (max - min);
		}

		h *= 60;
		if (h < 0) {
			h += 360;
		}

		return h;
	}

	public float lighting() {

		float r = redf();
		float g = greenf();
		float b = bluef();

		float min = MathUtil.min(r, g, b);
		float max = MathUtil.max(r, g, b);

		return lighting(min, max);
	}

	public float saturation() {

		float r = redf();
		float g = greenf();
		float b = bluef();

		float min = MathUtil.min(r, g, b);
		float max = MathUtil.max(r, g, b);

		float l = lighting(min, max);
		return saturation(min, max, l);
	}

	public float hue() {

		float r = redf();
		float g = greenf();
		float b = bluef();

		float min = MathUtil.min(r, g, b);
		float max = MathUtil.max(r, g, b);

		return hue(r, g, b, min, max);
	}

	public Color lighting(float l) {
		Vector3D hsl = hsl();
		return hsl(hsl.getX(), hsl.getY(), l).alpha(alpha());
	}

	public Color saturation(float s) {
		Vector3D hsl = hsl();
		return hsl(hsl.getX(), s, hsl.getZ()).alpha(alpha());
	}

	public Color hue(float h) {
		Vector3D hsl = hsl();
		return hsl(h, hsl.getY(), hsl.getZ()).alpha(alpha());
	}

	/**
	 * Calculates complementary color to this color instance.
	 *
	 * @return a new Color which is complementary to current.
	 */
	public Color complementary() {
		Vector3D hsl = hsl();
		double h = hsl.getY() + 180F;
		return hsl(h, hsl.getY(), hsl.getZ()).alpha(alpha());
	}

	@Override
	public int hashCode() {
		return value;
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj || (obj != null && obj.getClass() == Color.class) && value == ((Color) obj).value;
	}

	public static class ColorRangeException extends NovaException {

		private static final long serialVersionUID = 2757542930752922957L;

		public ColorRangeException(int r, int g, int b, int a) {
			super(String.format("Color out of range [0, 255]: red = %s green = %s blue = %s alpha = %s", r, g, b, a));
		}

		public ColorRangeException(int a) {
			super("Alpha out of range [0, 255]: " + a);
		}
	}
}
