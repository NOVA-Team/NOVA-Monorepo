package nova.core.gui.factory;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import nova.core.entity.Entity;
import nova.core.gui.Gui;
import nova.core.gui.GuiEvent.BindEvent;
import nova.core.loader.NovaMod;
import nova.core.network.NetworkTarget.IllegalSideException;
import nova.core.network.NetworkTarget.Side;
import nova.core.network.Sided;
import nova.core.util.Registry;
import nova.core.util.exception.NovaException;
import nova.core.util.transform.Vector3i;

public abstract class GuiFactory {

	protected HashMap<String, Registry<Gui>> guiRegistry = new HashMap<>();
	protected EnumMap<GuiType, List<Gui>> overlayRegistry = new EnumMap<>(GuiType.class);

	public static enum GuiType {
		INGAME, TITLE, OPTIONS, INGAME_OPTIONS, CRAFTING, NATIVE, CUSTOM
	}

	/**
	 * Register a NOVA {@link Gui}.
	 * 
	 * @param gui The GUI you want to register
	 * @param modID Your mod's ID
	 */
	public void registerGui(Gui gui, String modID) {

		// TODO verify mod id? Register with the mod instance rather?
		gui.setModID(modID);
		if (!guiRegistry.containsKey(modID))
			guiRegistry.put(modID, new Registry<>());
		guiRegistry.get(modID).register(gui);
	}

	public void registerOverlay(Gui gui, GuiType guiType) {
		if (!overlayRegistry.containsKey(guiType))
			overlayRegistry.put(guiType, new ArrayList<>());
		overlayRegistry.get(guiType).add(gui);
	}

	/**
	 * <p>
	 * Shows the provided {@link Gui} previously registered over the factory
	 * instance. It will trigger the {@link BindEvent}, so any changes to the
	 * instance can be done there.
	 * </p>
	 * 
	 * <p>
	 * Calling this is <i>safe</i>, you will always get a valid GUI for both
	 * sides no matter on which side you call it.
	 * </p>
	 * 
	 * @param modID Id of the {@link NovaMod} that registered the GUI
	 * @param identifier Unique identifier for the GUI
	 * @param entity {@link Entity} which opened the GUI
	 * @param position The block coordinate on which to open the GUI
	 * 
	 * @see #showGui(Gui, Entity, Vector3i)
	 */
	public void showGui(String modID, String identifier, Entity entity, Vector3i position) {

		Registry<Gui> gr = guiRegistry.get(modID);
		if (gr == null)
			throw new NovaException(String.format("No GUI called %s registered for mod %s!", identifier, modID));
		Optional<Gui> optGui = gr.get(identifier);
		if (!optGui.isPresent())
			throw new NovaException(String.format("No GUI called %s registered for mod %s!", identifier, modID));
		Gui gui = optGui.get();
		showGui(gui, entity, position);
	}

	/**
	 * <p>
	 * Shows the provided {@link Gui}. It will trigger the {@link BindEvent}, so
	 * any changes to the instance can be done there.
	 * </p>
	 * 
	 * <p>
	 * Calling this is <i>unsafe</i>, if you need a server side backend you
	 * <b>have</b> to call this on <i>both</i> sides on order create a matching
	 * GUI on the server side if the provided GUI wasn't registered with
	 * {@link #registerGui(Gui, String)}. This should only be used for client
	 * side GUIs, the usage of {@link #showGui(String, Gui, Entity, Vector3i)}
	 * is recommended.
	 * </p>
	 * 
	 * @param modID Id of the {@link NovaMod} that wants to show the GUI
	 * @param gui GUI to to display
	 * @param entity {@link Entity} which opened the GUI
	 * @param position The block coordinate on which to open the GUI
	 * 
	 * @see #showGui(String, String, Entity, Vector3i)
	 */
	public void showGui(String modID, Gui gui, Entity entity, Vector3i position) {
		gui.setModID(modID);
		showGui(gui, entity, position);
	}

	protected void showGui(Gui gui, Entity entity, Vector3i position) {
		gui.bind(entity, position);
	}

	/**
	 * Closes the currently open NOVA {@link Gui}, if present, and returns to
	 * the in-game GUI. It will not affect any native GUIs that might exist
	 * along with NOVA.
	 */
	public void closeGui() {
		Optional<Gui> active = getActiveGui();
		if (active.isPresent()) {
			closeGui(active.get());
		}
	}

	protected void closeGui(Gui gui) {
		gui.unbind();
	}

	/**
	 * Returns the active NOVA {@link Gui} on the client side, if present.
	 * 
	 * @return NOVA {@link Gui}
	 * @throws IllegalSideException if called on the server side
	 */
	@Sided(Side.CLIENT)
	public Optional<Gui> getActiveGui() {
		Side.assertSide(Side.CLIENT);
		return getActiveGuiImpl();
	}

	protected abstract Optional<Gui> getActiveGuiImpl();

	/**
	 * Returns the active NOVA {@link Gui} of the supplied player on the client
	 * side, if present.
	 * 
	 * @param player Player to check for
	 * @return NOVA {@link Gui}
	 */
	public Optional<Gui> getActiveGui(Entity player) {
		if (Side.get().isClient())
			return getActiveGuiImpl();
		return getActiveGuiImpl(player);
	}

	protected abstract Optional<Gui> getActiveGuiImpl(Entity player);

	/**
	 * Returns the active {@link GuiType}.
	 * 
	 * @return active {@link GuiType}
	 */
	public GuiType getActiveGuiType() {
		return getActiveGui().isPresent() ? GuiType.CUSTOM : GuiType.NATIVE;
	}
}
