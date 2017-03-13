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

package nova.core.util;

import nova.core.util.math.Vector3DUtil;
import nova.internal.core.Game;
import nova.internal.core.launch.NovaLauncher;
import nova.testutils.FakeWorld;
import nova.wrappertests.NovaLauncherTestFactory;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.Random;
import java.util.stream.IntStream;

public class RayTraceEfficiencyTest {
    public static void main(String[] args) {
        int maxTestSize = 10000;

        NovaLauncher launcher = NovaLauncherTestFactory.createDummyLauncher(RayTraceTest.RayTraceMod.class);
        FakeWorld fakeWorld = new FakeWorld();

		Game.logger().info("Generating random world with block count: " + maxTestSize);

        /**
         * Generate a random world
         */
        Random random = new Random();

        IntStream.range(0, 500)
			.mapToObj(value -> Vector3DUtil.random().scalarMultiply(random.nextInt(maxTestSize)))
			.forEach(pos -> fakeWorld.setBlock(pos, RayTraceTest.RayTraceMod.solid));

        Game.logger().info("World Generated");

        for (int size = 0; size <= maxTestSize; size += 200) {
            /**
             * Do random ray trace
             */
            RayTracer rayTracer = new RayTracer(new Ray(new Vector3D(0, 5, 5), new Vector3D(1, 0, 0))).setDistance(size);
			Game.logger().info("Ray tracing with threading: " + rayTracer.doParallel());
			Profiler start = new Profiler("Ray Trace " + size).start();
            rayTracer.rayTraceBlocks(fakeWorld).findFirst();
            start.end();
	        Game.logger().info(start.toString());
        }


    }
}
