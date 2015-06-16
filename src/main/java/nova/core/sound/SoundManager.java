package nova.core.sound;

import nova.core.game.GameStatusEventBus;
import nova.core.util.Factory;
import nova.core.util.Manager;
import nova.core.util.Registry;

public class SoundManager extends Manager<Sound> {

	public SoundManager(Registry<Factory<Sound>> registry, GameStatusEventBus gseb) {
		//TODO: Sounds are to be reworked.
		super(registry, gseb, Sound.class);
	}

}
