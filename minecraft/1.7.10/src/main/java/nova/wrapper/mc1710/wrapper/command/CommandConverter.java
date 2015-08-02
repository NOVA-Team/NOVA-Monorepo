package nova.wrapper.mc1710.wrapper.command;

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
