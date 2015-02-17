package nova.core.gui.render;

import nova.core.util.transform.Vector3d;

/**
 * Arbitrary immutable color object. Holds a color in argb space.
 * 
 * @author Vic Nightfall
 */
public class Color {

	// TODO Extend Operator?
	// TODO Document me!
	// TODO Test me!

	private final int value;
	
    public static final Color white     = rgb(255, 255, 255);
    public static final Color lightGray = rgb(192, 192, 192);
    public static final Color gray      = rgb(128, 128, 128);
    public static final Color darkGray  = rgb( 64,  64,  64);
    public static final Color black     = rgb(  0,   0,   0);
    public static final Color red       = rgb(255,   0,   0);
    public static final Color pink      = rgb(255, 175, 175);
    public static final Color orange    = rgb(255, 200,   0);
    public static final Color yellow    = rgb(255, 255,   0);
    public static final Color green     = rgb(  0, 255,   0);
    public static final Color magenta   = rgb(255,   0, 255);
    public static final Color cyan      = rgb(  0, 255, 255);
    public static final Color blue      = rgb(  0,   0, 255);


	private Color(int argb) {
		this.value = argb;
	}

	public static Color rgbc(int red, int green, int blue) {
		return rgbac(red, green, blue, 255);
	}

	public static Color rgb(int red, int green, int blue) {
		return rgba(red, green, blue, 255);
	}

	public static Color rgbfc(float red, float green, float blue) {
		return rgbafc(red, green, blue, 1F);
	}

	public static Color rgbf(float red, float green, float blue) {
		return rgbaf(red, green, blue, 1F);
	}

	public static Color rgb(int rgb) {
		return rgba(rgb, 255);
	}

	public static Color rgbac(int red, int green, int blue, int alpha) {
		int argb = (red & 0xFF) | (green & 0xFF) << 8 | (blue & 0xFF) << 16 | (alpha & 0xFF) << 24;
		return argb(argb);
	}

	public static Color rgba(int red, int green, int blue, int alpha) {
		if (red > 255 || red < 0 || green > 255 || green < 0 || blue > 255 || blue < 0 || alpha > 255 || blue < 0)
			throw new ColorRangeException(red, green, blue, alpha);
		return rgbac(red, green, blue, alpha);
	}

	public static Color rgbafc(float red, float green, float blue, float alpha) {
		return rgbac((int) (red * 255), (int) (green * 255), (int) (blue * 255), (int) (alpha * 255));
	}

	public static Color rgbaf(float red, float green, float blue, float alpha) {
		return rgba((int) (red * 255), (int) (green * 255), (int) (blue * 255), (int) (alpha * 255));
	}

	public static Color rgbac(int rgb, int alpha) {
		int value = rgb & 0xFFFFFF | (alpha & 0xFF) << 24;
		return argb(value);
	}

	public static Color rgba(int rgb, int alpha) {
		if (alpha > 255 || alpha < 0)
			throw new ColorRangeException(alpha);
		return rgbac(rgb, alpha);
	}

	public static Color rgbafc(int rgb, float alpha) {
		return rgbac(rgb, (int) (alpha * 255));
	}

	public static Color rgbaf(int rgb, float alpha) {
		return rgba(rgb, (int) (alpha * 255));
	}

	public static Color argb(int argb) {
		return new Color(argb);
	}

	public static Color rgba(int rgba) {
		int alpha = rgba & 0xFF;
		int argb = rgba << 8;
		argb |= alpha << 24;
		return argb(argb);
	}

	public static Color hsl(float h, float s, float l) {

		if (s == 0) {
			int c = (int)(l * 255);
			return rgbc(c, c, c);
		}
		
		float t1 = 0;
		if(l < 0.5F) {
			t1 = l * (1 + s);
		} else {
			t1 = l + s - l * s;
		}
		float t2 = 2 * l - t1;

		h = (h % 360F) / 360F;

		float tr = h + 1 / 3F;
		float tg = h;
		float tb = h - 1 / 3F;

		tr = tr > 1 ? tr - 1 : tr < 0 ? tr + 1 : tr;
		tg = tg > 1 ? tg - 1 : tg < 0 ? tg + 1 : tg;
		tb = tb > 1 ? tb - 1 : tb < 0 ? tb + 1 : tb;

		float r = constructColor(tr, t1, t2);
		float g = constructColor(tg, t1, t2);
		float b = constructColor(tb, t1, t2);

		return rgbfc(r, g, b);
	}

	private static float constructColor(float c, float t1, float t2) {
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

	public static Color hsl(Vector3d hsl) {
		return hsl(hsl.xf(), hsl.yf(), hsl.zf());
	}

	public int argb() {
		return value;
	}

	public int rgba() {
		int alpha = value & 0xFF000000;
		int rgba = value << 8;
		rgba |= alpha >> 24;
		return rgba;
	}

	public int red() {
		return value >> 16 & 0xFF;
	}

	public int green() {
		return value >> 8 & 0xFF;
	}

	public int blue() {
		return value & 0xFF;
	}

	public int alpha() {
		return value >> 24 & 0xFF;
	}

	public Color red(int red) {
		return rgba(red, green(), blue(), alpha());
	}

	public Color green(int green) {
		return rgba(red(), green, blue(), alpha());
	}

	public Color blue(int blue) {
		return rgba(red(), green(), blue, alpha());
	}

	public Color alpha(int alpha) {
		return rgba(red(), green(), blue(), alpha);
	}

	public float redf() {
		return red() / 255F;
	}

	public float greenf() {
		return green() / 255F;
	}

	public float bluef() {
		return blue() / 255F;
	}

	public float alphaf() {
		return alpha() / 255F;
	}

	public Color redf(float red) {
		return rgba((int) (red * 255), green(), blue(), alpha());
	}

	public Color greenf(float green) {
		return rgba(red(), (int) (green * 255), blue(), alpha());
	}

	public Color bluef(float blue) {
		return rgba(red(), green(), (int) (blue * 255), alpha());
	}

	public Color alphaf(float alpha) {
		return rgba(red(), green(), blue(), (int) (alpha * 255));
	}

	public Color blend(Color color) {
		int aA = alpha();
		int aB = color.alpha();
		
		int r = (int) ((red()   * aA / 255F) + (color.red()   * aB * (255F - aA) / (255F * 255F)));
		int g = (int) ((green() * aA / 255F) + (color.green() * aB * (255F - aA) / (255F * 255F)));
		int b = (int) ((blue()  * aA / 255F) + (color.blue()  * aB * (255F - aA) / (255F * 255F)));
		int a = (int) (aA + (aB * (255F - aA) / 255F));
		return rgbac(r, g, b, a);
	}

	public Color add(Color color) {
		return rgbac(red() + color.red(), green() + color.green(), blue() + color.blue(), alpha());
	}

	public Color substract(Color color) {
		return rgbac(red() - color.red(), green() - color.green(), blue() - color.blue(), alpha());
	}

	public Color multiply(Color color) {
		return rgbac(red() * color.red(), green() * color.green(), blue() * color.blue(), alpha());
	}

	public Color divide(Color color) {
		return rgbac(red() / color.red(), green() / color.green(), blue() / color.blue(), alpha());
	}

	public Color average(Color color) {
		return rgbac((red() + color.red()) / 2, (green() + color.green()) / 2, (blue() + color.blue()) / 2, (alpha() + color.alpha()) / 2);
	}

	public Color negate() {
		return rgbac(255 - red(), 255 - green(), 255 - blue(), alpha());
	}

	public Vector3d hsl() {

		float r = redf();
		float g = greenf();
		float b = bluef();
		
		float min = Math.min(Math.min(r, g), b);
		float max = Math.max(Math.max(r, g), b);

		float l = lighting(min, max);
		float s = saturation(min, max, l);
		float h = hue(r, g, b, min, max);

		return new Vector3d(h, s, l);
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
		if (h < 0)
			h += 360;

		return h;
	}

	public float lighting() {

		float r = redf();
		float g = greenf();
		float b = bluef();

		float min = Math.min(Math.min(r, g), b);
		float max = Math.max(Math.max(r, g), b);

		return lighting(min, max);
	}

	public float saturation() {

		float r = redf();
		float g = greenf();
		float b = bluef();

		float min = Math.min(Math.min(r, g), b);
		float max = Math.max(Math.max(r, g), b);

		float l = lighting(min, max);
		return saturation(min, max, l);
	}

	public float hue() {

		float r = redf();
		float g = greenf();
		float b = bluef();

		float min = Math.min(Math.min(r, g), b);
		float max = Math.max(Math.max(r, g), b);

		return hue(r, g, b, min, max);
	}
	
	public Color lighting(float l) {
		Vector3d hsl = hsl();
		return hsl(hsl.xf(), hsl.yf(), l).alpha(alpha());
	}
	
	public Color saturation(float s) {
		Vector3d hsl = hsl();
		return hsl(hsl.xf(), s, hsl.zf()).alpha(alpha());
	}
	
	public Color hue(float h) {
		Vector3d hsl = hsl();
		return hsl(h, hsl.yf(), hsl.zf()).alpha(alpha());
	}

	public Color complementary() {
		Vector3d hsl = hsl();
		float h = hsl.yf() + 180F;
		return hsl(h, hsl.yf(), hsl.zf()).alpha(alpha());
	}

	public static class ColorRangeException extends RuntimeException {

		private static final long serialVersionUID = 2757542930752922957L;

		public ColorRangeException(int r, int g, int b, int a) {
			super(String.format("Color out of range [0, 255]: red = %s green = %s blue = %s alpha = %s", r, g, b, a));
		}

		public ColorRangeException(int a) {
			super("Alpha out of range [0, 255]: " + a);
		}
	}
}
