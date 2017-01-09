package nova.core.retention;

import org.junit.Test;

import static nova.testutils.NovaAssertions.assertThat;

/**
 * @author soniex2
 */
public class DataConvertibleTest {

	@DataConvertible(AnImmutableObject.Converter.class)
	public static class AnImmutableObject {
		private final String data;

		public AnImmutableObject(String data) {
			this.data = data;
		}

		public String getData() {
			return data;
		}

		public static class Converter implements DataConverter {
			@Override
			public Object fromData(Data d) {
				return new AnImmutableObject(d.get("data"));
			}

			@Override
			public void toData(Object o, Data data) {
				data.put("data", ((AnImmutableObject) o).getData());
			}
		}
	}

	@Test
	public void testDataConvertible() {
		AnImmutableObject o = new AnImmutableObject("hello");
		Data d = new Data();
		d.put("anImmutableObject", o);
		AnImmutableObject other = d.getDataConvertible("anImmutableObject");
		assertThat(o).isEqualToComparingFieldByField(other);
		assertThat(o).isNotSameAs(other);
	}
}
