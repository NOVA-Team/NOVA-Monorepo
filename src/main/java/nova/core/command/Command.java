package nova.core.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nova.core.entity.component.Player;
import nova.core.util.Identifiable;
import nova.core.util.NovaException;

/**
 * The command type.
 *
 * The ID of the command is the command name.
 *
 * For example, if the command starts with /doSomething ... ... then the ID of
 * the command is "doSomething"
 *
 * @author Calclavia
 */
public abstract class Command implements Identifiable {

	/**
	 * Handles the command.
	 *
	 * Throw a CommandException if the command handling failed. The message of
	 * the exception will be printed out for the user to see. This is useful for
	 * cases where the user uses the command incorrectly.
	 *
	 * @param player The player who sent the command.
	 * @param params The command parameters as {@link Args} object
	 * @throws CommandException
	 */
	// TODO: Should we make it throw exceptions, or have a String return value
	// for the message to give the player?
	public abstract void handle(Player player, Args params) throws CommandException;

	public final void handle(Player player, String[] params) throws CommandException {
		handle(player, new Args(params));
	}

	public final void handle(Player player, String params) throws CommandException {
		handle(player, new Args(splitParams(params)));
	}

	private static final Pattern patternSplitParams = Pattern.compile("([^\"]\\S*|\".+?\")\\s*");

	/**
	 * Returns an array of parameters split on space, considering quotes.
	 * 
	 * @param params
	 * @return
	 */
	private static String[] splitParams(String params) {
		List<String> list = new ArrayList<String>();
		Matcher m = patternSplitParams.matcher(params);
		while (m.find())
			list.add(m.group(1));
		return list.toArray(new String[list.size()]);
	}

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
