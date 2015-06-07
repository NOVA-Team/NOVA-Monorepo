package nova.core.util;

import nova.core.util.transform.vector.Vector3d;
import nova.internal.Game;
import nova.internal.launch.NovaLauncher;
import nova.testutils.FakeWorld;
import nova.wrappertests.NovaLauncherTestFactory;

import java.util.Random;
import java.util.stream.IntStream;

public class RayTraceEfficiencyTest {
    public static void main(String[] args) {
        int maxTestSize = 10000;

        NovaLauncher launcher = new NovaLauncherTestFactory(RayTraceTest.RayTraceMod.class).createLauncher();
        FakeWorld fakeWorld = new FakeWorld();


        Game.logger().info("Generating random world with block count: " + maxTestSize);

        /**
         * Generate a random world
         */
        Random random = new Random();

        IntStream.range(0, 500)
                .mapToObj(value -> Vector3d.random().multiply(random.nextInt(maxTestSize)).toInt())
                .forEach(pos -> fakeWorld.setBlock(pos, RayTraceTest.RayTraceMod.solid));

        Game.logger().info("World Generated");

        for (int size = 0; size <= maxTestSize; size += 200) {
            /**
             * Do random ray trace
             */
            RayTracer rayTracer = new RayTracer(new Ray(new Vector3d(0, 5, 5), new Vector3d(1, 0, 0))).setDistance(size);
            Game.logger().info("Ray tracing with threading: " + rayTracer.doParallel());
            Profiler start = new Profiler("Ray Trace " + size).start();
            rayTracer.rayTraceBlocks(fakeWorld).findFirst();
            start.end();
        }
    }
}
