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

package nova.testutils;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;

public abstract class NovaAssertions extends Assertions{

    public static Offset<Double> offsetD = Offset.offset(1e-11);
    public static Offset<Float> offsetF = Offset.offset(1e-7F);

    public static AlmostEqualAssertionVector3D assertThat(Vector3D actual) {
        return new AlmostEqualAssertionVector3D(actual);
    }





    public static abstract class Almost<K extends Almost<K, T>, T> extends AbstractAssert<K, T> {

        private final T zero;


        protected Almost(T actual, Class<?> selfType, T zero) {
            super(actual, selfType);
            this.zero = zero;
        }

        public Almost<K, T> isAlmostEqualTo(T other, double precision){
            isNotNull();
            if (Math.abs(difference(actual, other)) > precision) {
				failWithMessage("<%s> was expected to be equal to <%s> with tolerance <%s> but difference is <%s>", actual, other, precision, difference(actual, other));
			}
            return this;
        }
        public Almost<K, T> isAlmostZeroTo(double precision){
            isNotNull();
            if (Math.abs(difference(actual, zero)) > precision) {
				failWithMessage("<%s> was expected to be equal to <%s> with tolerance <%s> but difference is <%s>", actual, zero, precision, difference(actual, zero));
			}
            return this;
        }

        public Almost<K, T> isAlmostEqualTo(T other) {
            return isAlmostEqualTo(other, 1e-11);
        }
        public Almost<K, T> isAlmostZeroTo() {
            return isAlmostZeroTo(1e-11);
        }

        /**
         * Returns difference between objects.
         * @param actual object from which the difference is to be calculated.
         * @param other object to which the difference is to be calculated.
         * @return the difference
         */
        protected abstract double difference(T actual, T other);

    }


    public static class AlmostEqualAssertionVector3D extends Almost<AlmostEqualAssertionVector3D, Vector3D> {
        private AlmostEqualAssertionVector3D(Vector3D actual) {
            super(actual, AlmostEqualAssertionVector3D.class, Vector3D.ZERO);
        }

        @Override
        protected double difference(Vector3D actual, Vector3D other) {
            return  actual.distance1(other);
        }
    }

    public static class AlmostEqualAssertionDouble extends Almost<AlmostEqualAssertionDouble, Double> {
        private AlmostEqualAssertionDouble(Double actual) {
            super(actual, AlmostEqualAssertionDouble.class, 0D);
        }

        @Override
        protected double difference(Double actual, Double other) {
            return  actual - other;
        }
    }


}
