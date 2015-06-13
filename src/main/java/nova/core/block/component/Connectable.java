package nova.core.block.component;

import nova.core.component.Component;
import nova.core.event.Event;
import nova.core.event.EventBus;

import java.util.Collections;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A component that defines a connection withPriority another.  C is the connector type
 * @author Calclavia
 */
public class Connectable<C> extends Component {

	public final EventBus<Event> connectEvent = new EventBus<>();

	/**
	 * Can this connectable component connect to another component?
	 */
	public Function<C, Boolean> canConnect = C -> true;

	public Supplier<Set<C>> connections = Collections::emptySet;

	public Connectable setCanConnect(Function<C, Boolean> canConnect) {
		this.canConnect = canConnect;
		return this;
	}

	public Connectable setConnections(Supplier<Set<C>> connections) {
		this.connections = connections;
		return this;
	}
}
