/**
 The MIT License (MIT)

 Copyright (c) 2014 ChickenBones

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE. 
 */

package nova.core.util.transform;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * @author ChickenBones
 */
public class Quaternion {

	public double x;
	public double y;
	public double z;
	public double s;

	public Quaternion() {
		s = 1;
		x = 0;
		y = 0;
		z = 0;
	}

	public Quaternion(Quaternion Quaternion) {
		x = Quaternion.x;
		y = Quaternion.y;
		z = Quaternion.z;
		s = Quaternion.s;
	}

	public Quaternion(double d, double d1, double d2, double d3) {
		x = d1;
		y = d2;
		z = d3;
		s = d;
	}

	/**
	 * Retrieves a quaternion based on Euler Angles.
	 * @param yaw - Radians
	 * @param pitch - Radians
	 * @param roll - Radians
	 * @return {@link Quaternion} based on Euler Angles.
	 */
	public static Quaternion fromEuler(double yaw, double pitch, double roll) {
		// Assuming the angles are in radians.
		double c1 = Math.cos(yaw / 2);
		double s1 = Math.sin(yaw / 2);
		double c2 = Math.cos(pitch / 2);
		double s2 = Math.sin(pitch / 2);
		double c3 = Math.cos(roll / 2);
		double s3 = Math.sin(roll / 2);
		double c1c2 = c1 * c2;
		double s1s2 = s1 * s2;
		double w = c1c2 * c3 - s1s2 * s3;
		double x = c1c2 * s3 + s1s2 * c3;
		double y = s1 * c2 * c3 + c1 * s2 * s3;
		double z = c1 * s2 * c3 - s1 * c2 * s3;
		return new Quaternion(w, x, y, z);
	}

	public static Quaternion aroundAxis(double ax, double ay, double az, double angle) {
		return new Quaternion().setAroundAxis(ax, ay, az, angle);
	}

	public static Quaternion aroundAxis(Vector3<?> axis, double angle) {
		return aroundAxis(axis.xd(), axis.yd(), axis.zd(), angle);
	}

	public Quaternion set(Quaternion Quaternion) {
		x = Quaternion.x;
		y = Quaternion.y;
		z = Quaternion.z;
		s = Quaternion.s;

		return this;
	}

	public Quaternion set(double d, double d1, double d2, double d3) {
		x = d1;
		y = d2;
		z = d3;
		s = d;

		return this;
	}

	public Quaternion setAroundAxis(double ax, double ay, double az, double angle) {
		angle *= 0.5;
		double d4 = Math.sin(angle);
		return set(Math.cos(angle), ax * d4, ay * d4, az * d4);
	}

	public Quaternion setAroundAxis(Vector3d axis, double angle) {
		return setAroundAxis(axis.x, axis.y, axis.z, angle);
	}

	public Quaternion multiply(Quaternion Quaternion) {
		double d = s * Quaternion.s - x * Quaternion.x - y * Quaternion.y - z * Quaternion.z;
		double d1 = s * Quaternion.x + x * Quaternion.s - y * Quaternion.z + z * Quaternion.y;
		double d2 = s * Quaternion.y + x * Quaternion.z + y * Quaternion.s - z * Quaternion.x;
		double d3 = s * Quaternion.z - x * Quaternion.y + y * Quaternion.x + z * Quaternion.s;
		s = d;
		x = d1;
		y = d2;
		z = d3;

		return this;
	}

	public Quaternion rightMultiply(Quaternion Quaternion) {
		double d = s * Quaternion.s - x * Quaternion.x - y * Quaternion.y - z * Quaternion.z;
		double d1 = s * Quaternion.x + x * Quaternion.s + y * Quaternion.z - z * Quaternion.y;
		double d2 = s * Quaternion.y - x * Quaternion.z + y * Quaternion.s + z * Quaternion.x;
		double d3 = s * Quaternion.z + x * Quaternion.y - y * Quaternion.x + z * Quaternion.s;
		s = d;
		x = d1;
		y = d2;
		z = d3;

		return this;
	}

	public double magnitude() {
		return Math.sqrt(x * x + y * y + z * z + s * s);
	}

	public Quaternion normalize() {
		double d = magnitude();
		if (d != 0) {
			d = 1 / d;
			x *= d;
			y *= d;
			z *= d;
			s *= d;
		}

		return this;
	}

	public Quaternion copy() {
		return new Quaternion(this);
	}

	public Vector3d rotate(Vector3d vec) {
		double d = -x * vec.x - y * vec.y - z * vec.z;
		double d1 = s * vec.x + y * vec.z - z * vec.y;
		double d2 = s * vec.y - x * vec.z + z * vec.x;
		double d3 = s * vec.z + x * vec.y - y * vec.x;
		return new Vector3d(d1 * s - d * x - d2 * z + d3 * y, d2 * s - d * y + d1 * z - d3 * x, d3 * s - d * z - d1 * y + d2 * x);
	}

	public String toString() {
		MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
		return "Quaternion[" + new BigDecimal(s, cont) + ", " + new BigDecimal(x, cont) + ", " + new BigDecimal(y, cont) + ", " + new BigDecimal(z, cont) + "]";
	}
}
