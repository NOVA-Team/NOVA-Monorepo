package nova.core.game;

import nova.core.event.Event;
import nova.core.event.EventBus;

/**
 * @author Kubuxu
 */
public final class GameStatusEventBus extends EventBus<GameStatusEventBus.GameStatusEvent>{



	public static class GameStatusEvent extends Event {}

	public static class PreInit extends GameStatusEvent {}
	public static class Init extends GameStatusEvent {}
	public static class PostInit extends GameStatusEvent {}
}
