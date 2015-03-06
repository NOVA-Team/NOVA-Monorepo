package nova.core.gui;

import nova.core.gui.ComponentEvent.ActionEvent;
import nova.core.gui.components.Button;
import nova.core.gui.components.Container;
import nova.core.gui.layout.Anchor;
import nova.core.gui.layout.BorderLayout;
import nova.core.gui.layout.Constraints.BorderConstraints;
import nova.core.network.NetworkTarget.Side;

public class GuiTest {

	// TODO Make this a test case, for now it's just to test generics and stuffs
	// until the core is functional.
	public void testGui() {

		// Safe way of getting the fitting constraints for the layout, could also create the object yourself and spare another line.
		BorderLayout layout = new BorderLayout();
		BorderConstraints constraints = layout.constraints();
		Container container = new Container("test").setLayout(layout)
			.add(new Button("testButton1", "Test Button 1")
					.onEvent((e, button) -> {
						// TODO Still needs a cast unfortunately
						button.setActive(false);
					}, ActionEvent.class, Side.SERVER),
				constraints.of(e -> e.region = Anchor.WEST))
			.add(new Button("testButton2", "Test Button 2"), constraints.of(e -> e.region = Anchor.CENTER))
			.add(new Button("testButton3", "Test Button 3"), constraints.of(e -> e.region = Anchor.EAST));

		// Container 2 is the exact equivalent of container 1, without using any constraints. It's the more error prone way.
		Container container2 = new Container("test")
			.add(new Button("testButton1", "Test Button 1")
				.onEvent(this::onButton1Pressed, ActionEvent.class, Side.SERVER), Anchor.WEST)
			.add(new Button("testButton2", "Test Button 2"))
			.add(new Button("testButton3", "Test Button 3"), Anchor.EAST);
	}

	private void onButton1Pressed(ActionEvent event, Button button) {

	}
}
