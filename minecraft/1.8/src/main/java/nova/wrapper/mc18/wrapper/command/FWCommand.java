package nova.wrapper.mc18.wrapper.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import nova.core.command.Command;
import nova.core.entity.Entity;
import nova.core.entity.component.Player;
import nova.internal.core.Game;

import java.util.Arrays;
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
			command.handle(entity.get(Player.class), "help");
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
	public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) throws CommandException {
		Entity entity = Game.natives().toNova(p_71515_1_);
		try {
			command.handle(entity.get(Player.class), p_71515_2_);
		} catch (Command.CommandException e) {
			throw new CommandException(e.getMessage());
		}
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
		Entity entity = Game.natives().toNova(p_71519_1_);
		return command.canUse(entity.get(Player.class));
	}

	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
		return null;
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
