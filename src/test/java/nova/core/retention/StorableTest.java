package nova.core.retention;

import org.junit.Ignore;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by magik6k on 4/18/15.
 */
public class StorableTest {
    @Test
    public void testSingleStorable() {
        class TestCase implements Storable {
            @Stored
            private int value = 0;
        }
        TestCase test = new TestCase();
        test.value = 256;

        Data data = new Data();
        test.save(data);

        test = new TestCase();
        test.load(data);

        assertThat(test.value).isEqualTo(256);
    }

    @Test
    public void testKeyStorable() {
        class TestCase implements Storable {
            @Stored(key = "int")
            private int integer = 0;

            @Stored(key = "str")
            private String string = "";
        }
        TestCase test = new TestCase();
        test.integer = 256;
        test.string = "test";

        Data data = new Data();
        test.save(data);

        test = new TestCase();
        test.load(data);

        assertThat(test.integer).isEqualTo(256);
        assertThat(test.string).isEqualTo("test");
    }

    public static class SubTestCase implements Storable {
        @Stored
        private int value = 0;

        SubTestCase() {}
    }

    @Test
    public void testRecursiveStorable() {

        class TestCase implements Storable {
            @Stored
            private SubTestCase sub;
        }

        TestCase test = new TestCase();
        test.sub = new SubTestCase();
        test.sub.value = 256;

        Data data = new Data();
        test.save(data);

        test = new TestCase();
        test.load(data);

        assertThat(test.sub.value).isEqualTo(256);
    }

    @Test
    public void testMultipleStorable() {
        class TestCase implements Storable {
            @Stored
            private int integer = 0;

            @Stored
            private String string = "";
        }
        TestCase test = new TestCase();
        test.integer = 256;
        test.string = "test";

        Data data = new Data();
        test.save(data);

        test = new TestCase();
        test.load(data);

        assertThat(test.integer).isEqualTo(256);
        assertThat(test.string).isEqualTo("test");
    }
}
