package nova.core.sound;

import nova.core.util.Factory;
import nova.core.util.Manager;
import nova.core.util.Registry;

public class SoundManager extends Manager<Sound> {

	public SoundManager(Registry<Factory<Sound>> registry) {
		super(registry);
	}

}
