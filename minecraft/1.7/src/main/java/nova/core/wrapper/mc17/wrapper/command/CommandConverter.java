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

package nova.core.wrapper.mc17.wrapper.command;

import net.minecraft.command.ICommand;
import nova.core.command.Command;
import nova.core.nativewrapper.NativeConverter;

/**
 * @author Calclavia
 */
public class CommandConverter implements NativeConverter<Command, ICommand> {
	@Override
	public Class<Command> getNovaSide() {
		return Command.class;
	}

	@Override
	public Class<ICommand> getNativeSide() {
		return ICommand.class;
	}

	@Override
	public Command toNova(ICommand nativeObj) {
		if (nativeObj instanceof FWCommand) {
			return ((FWCommand) nativeObj).command;
		}

		throw new RuntimeException("BW of command is not implemented.");
	}

	@Override
	public ICommand toNative(Command novaObj) {
		return new FWCommand(novaObj);
	}
}
