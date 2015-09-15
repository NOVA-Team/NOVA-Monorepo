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

package nova.core.wrapper.mc.forge.v17.wrapper.command;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import nova.core.command.Command;
import nova.core.entity.Entity;
import nova.core.entity.component.Player;
import nova.internal.core.Game;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Calclavia
 */
public class FWCommand implements ICommand {

	public final Command command;

	public FWCommand(Command command) {
		this.command = command;
	}

	@Override
	public String getCommandName() {
		return command.getID();
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		Entity entity = Game.natives().toNova(p_71518_1_);

		try {
			command.handle(entity.components.get(Player.class), "help");
			return null;
		} catch (Command.CommandException e) {
			return e.getLocalizedMessage();
		}
	}

	@Override
	public List getCommandAliases() {
		return Arrays.asList(command.getAlias().toArray());
	}

	@Override
	public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
		Entity entity = Game.natives().toNova(p_71515_1_);
		try {
			command.handle(entity.components.get(Player.class), String.join(" ", p_71515_2_));
		} catch (Command.CommandException e) {
			throw new WrongUsageException(e.getMessage());
		}
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
		Entity entity = Game.natives().toNova(p_71519_1_);
		return command.canUse(entity.components.get(Player.class));
	}

	@Override
	public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_) {
		return Collections.emptyList();
	}

	@Override
	public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
		return false;
	}

	@Override
	public int compareTo(Object o) {
		return 0;
	}
}
