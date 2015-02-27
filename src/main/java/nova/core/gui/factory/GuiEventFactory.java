package nova.core.gui.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Function;

import nova.core.gui.ComponentEvent;
import nova.core.gui.Gui;
import nova.core.gui.GuiComponent;
import nova.core.network.Packet;
import nova.core.util.exception.NovaException;

public class GuiEventFactory {

	public static GuiEventFactory instance = new GuiEventFactory();

	private final ArrayList<Function<GuiComponent<?, ?>, ?>> networkEvents = new ArrayList<>();
	private final HashMap<Class<?>, Integer> networkEventsReverse = new HashMap<>();

	private GuiEventFactory() {
		registerNetworkEvents();
	}
	
	public void registerNetworkEvents() {
		registerNetworkEvent((component) -> new ComponentEvent.ActionEvent<>(component));
	}

	public <E extends ComponentEvent<?>> void registerNetworkEvent(Function<GuiComponent<?, ?>, E> supplier) {
		networkEvents.add(supplier);
		networkEventsReverse.put(supplier.apply(null).getClass(), networkEvents.size() - 1);
	}

	@SuppressWarnings("unchecked")
	public <E extends ComponentEvent<?>> E constructEvent(Packet packet, Gui parentGui) {
		int eventID = packet.readInt();
		String qualifiedName = packet.readString();
		int eventSubID = packet.readInt();

		if (eventID < 0 || eventID >= networkEvents.size())
			throw new NovaException(String.format("Illegal event type %s at GUI %s", eventID, parentGui));

		Optional<GuiComponent<?, ?>> component = parentGui.getChildElement(qualifiedName);
		if (!component.isPresent())
			throw new NovaException(String.format("Recieved an event for a non-existent component \"%s\" at GUI %s", qualifiedName, parentGui));

		E event = (E) networkEvents.get(eventID).apply(component.get());
		packet.setID(eventSubID);
		event.read(packet);
		return event;
	}

	public void constructPacket(ComponentEvent<?> event, Gui parentGui, Packet packet, int subID) {
		if (!networkEventsReverse.containsKey(event.getClass()))
			throw new NovaException(String.format("Unknown event %s at GUI %s. Register with registerNetworkEvent!", event.getClass(), packet));
		packet.writeInt(networkEventsReverse.get(event.getClass()));
		packet.writeString(event.component.getQualifiedName());
		packet.writeInt(subID);
		packet.setID(subID);
		event.write(packet);
	}
}
