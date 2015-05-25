package nova.core.component.misc;

import nova.core.component.Component;
import nova.core.util.Identifiable;

/**
 * Applied to objects that can take damage.
 * @author Calclavia
 */
public abstract class Damageable extends Component {

	public void damage(double amount) {
		damage(amount, DamageType.generic);
	}

	public abstract void damage(double amount, DamageType type);

	public static class DamageType implements Identifiable {

		public static final DamageType generic = new DamageType("generic");

		public final String name;

		public DamageType(String name) {
			this.name = name;
		}

		@Override
		public String getID() {
			return name;
		}
	}
}
