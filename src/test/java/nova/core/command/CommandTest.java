/*
 * Copyright (c) 2015 NOVA, All rights reserved.
 * This library is free software, licensed under GNU Lesser General Public License version 3
 *
 * This file is part of NOVA.
 *
 * NOVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NOVA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NOVA.  If not, see <http://www.gnu.org/licenses/>.
 */

package nova.core.command;

import nova.core.entity.component.Player;
import nova.internal.core.Game;
import nova.internal.core.launch.NovaLauncher;
import nova.wrappertests.NovaLauncherTestFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static nova.testutils.NovaAssertions.assertThat;

/**
 * @author Calclavia
 */
public class CommandTest {

	@Before
	public void setUp() throws Exception {
		NovaLauncherTestFactory novaLauncherTestFactory = new NovaLauncherTestFactory();
		NovaLauncher launcher = novaLauncherTestFactory.createLauncher();

		Game.commands().register(new ExplodeCommand());
	}

	@Test
	public void testCommandValidation() throws Exception {
		Optional<Command> explode = Game.commands().get("explode");
		assertThat(explode).isPresent();

		Command command = explode.get();
		command.handle(null, new String[] { "radius:5" });
	}

	public static class ExplodeCommand extends Command {
		@Override
		public void handle(Player player, Args params) throws CommandException {
			if (params.checkString(0).equals("radius")) {
				
			}
		}

		@Override
		public String getID() {
			return "explode";
		}
	}
}
