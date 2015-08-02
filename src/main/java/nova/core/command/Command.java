package nova.core.command;

import nova.core.entity.component.Player;
import nova.core.util.Identifiable;
import nova.core.util.NovaException;

import java.util.Collections;
import java.util.Set;

/**
 * The command type.
 *
 * The ID of the command is the command name.
 *
 * For example, if the command starts with /doSomething ... ...
 * then the ID of the command is "doSomething"
 *
 * @author Calclavia
 */
public abstract class Command implements Identifiable {

	/**
	 * Handles the command.
	 *
	 * Throw a CommandException if the command handling failed.
	 * The message of the exception will be printed out for the user to see.
	 * This is useful for cases where the user uses the command incorrectly.
	 *
	 * @param player The player who sent the command.
	 * @param params The command parameters separated by spaces
	 */
	//TODO: Should we make it throw exceptions, or have a String return value for the message to give the player?
	public abstract void handle(Player player, String... params) throws CommandException;

	/**
	 * @return A set of alias command names that will also activate the command.
	 */
	public Set<String> getAlias() {
		return Collections.emptySet();
	}

	/**
	 * @param player The player trying to use the command
	 * @return True if the player can use this command
	 */
	public boolean canUse(Player player) {
		return true;
	}

	public static class CommandException extends NovaException {
		public CommandException(String message, Object... parameters) {
			super(message, parameters);
		}

		public CommandException(String message) {
			super(message);
		}
	}
}
