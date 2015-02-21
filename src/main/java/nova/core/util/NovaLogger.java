package nova.core.util;

/**
 * The logger class for NOVA.
 *
 * @author Calclavia
 */
//TODO: Use an actual logger. Right now it's all dummy methods
public class NovaLogger {

	public static void log(String msg) {
		System.out.println(msg);
	}

	public static void log(Exception exception) {
		exception.printStackTrace();
	}
}
