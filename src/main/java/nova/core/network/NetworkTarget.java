package nova.core.network;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import nova.core.entity.Entity;
import nova.core.event.SidedEventBus;
import nova.core.game.Game;
import nova.core.gui.Gui;
import nova.core.util.exception.NovaException;
import nova.core.world.World;

/**
 * A NetworkTarget specifies the target of a {@link Packet} or event.
 * 
 * @author Vic Nightfall
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface NetworkTarget {

	/**
	 * Side on which to process the object.
	 * 
	 * @return side
	 */
	public Side value();

	/**
	 * <p>
	 * A side specifies the current scope of the execution environment. Use
	 * {@link #get()} to check your current side in order to react differently
	 * on the server or client side. Some methods may only be run on a specific
	 * side, see {@link #assertSide(Side)}.
	 * </p>
	 * 
	 * <p>
	 * <b>Design your NOVA mod with two different sides in mind, or they might
	 * only run client side and crash your game when attempting to run the mod
	 * on the server side!</b>
	 * </p>
	 * 
	 * <p>
	 * By convention, {@link Sided} is used to mark a sided type. Any reference
	 * to it from the wrong side, be it in form of a variable, a method call, or
	 * anything else that might cause the desired type to load might inevidably
	 * crash your game, causing an {@link IllegalSideException},
	 * {@link ReflectiveOperationException} or <i>worse</i>.
	 * </p>
	 * 
	 * @author Vic Nightfall
	 */
	public static enum Side {
		/**
		 * The client side. The client usually handles rendering and has no
		 * access to the remote {@link World} or {@link Entity Entities}. Any
		 * changes on the client side have to be synchronized with the
		 * {@link #SERVER} side using {@link Packet Packets} or internal
		 * synchronizing methods as provided by {@link Gui}, {@link Entity} and
		 * others.
		 * 
		 * @see NetworkTarget
		 * @see NetworkManager
		 * @see #SERVER
		 */
		CLIENT,

		/**
		 * The server side. The server usually handles saving, entity logic,
		 * world data, etc. The server has to send packets to communicate with
		 * the {@link #CLIENT}, every interaction with the game world has to be
		 * synchronized in order to see the effects on the client. The server
		 * doesn't handle rendering usually.
		 * 
		 * @see NetworkTarget
		 * @see NetworkManager
		 * @see #CLIENT
		 */
		SERVER,

		/**
		 * BOTH is used by {@link NetworkTarget} to specify that an object has
		 * to be processed by both sides, {@link #SERVER} and {@link #CLIENT}.
		 */
		BOTH,

		/**
		 * NONE is used by {@link NetworkTarget} to specify that an object has
		 * been processed, and finished its queue.
		 */
		NONE;

		/**
		 * Check if the given side is a valid target for this side. This is
		 * given if the provided side is either the opposing side or of type
		 * {@link #BOTH}.
		 * 
		 * @param otherSide side to check for
		 * @return {@code true} if otherSide is a valid target.
		 */
		public boolean targets(Side otherSide) {
			return otherSide != NONE && this != NONE && (otherSide == BOTH || this != otherSide);
		}

		/**
		 * Check if the provided side is the server side, shorthand for
		 * {@code side == Side.SERVER}.
		 * 
		 * @return {@code true} if the current side is SERVER.
		 */
		public boolean isServer() {
			return this == SERVER;
		}

		/**
		 * Check if the provided side is the client side, shorthand for
		 * {@code side == Side.CLIENT}.
		 * 
		 * @return {@code true} if the current side is CLIENT.
		 */
		public boolean isClient() {
			return this == CLIENT;
		}

		/**
		 * Returns the opposite side, {@link #SERVER} for {@link #CLIENT} and
		 * vice-visa. {@link #BOTH} and {@link #NONE} remain unchanged.
		 * 
		 * @return Opposite side
		 */
		public Side opposite() {
			if (this == BOTH || this == NONE)
				return this;
			return this == CLIENT ? SERVER : CLIENT;
		}

		/**
		 * Reduce is used to mark an object that was sent over the network as
		 * already processed from the opposing side. Especially used for
		 * {@link #BOTH}.
		 * 
		 * @return reduced scope, without the opposite side
		 * @see SidedEventBus
		 */
		public Side reduce() {
			Side current = get();
			return this == BOTH ? current : this == current ? NONE : this;
		}

		/**
		 * Shorthand for {@code Side.assertSide(this)}
		 * 
		 * @see #assertSide(Side)
		 */
		public void assertSide() {
			Side current = get();
			if (this != current)
				throw new IllegalSideException(this, Thread.currentThread().getStackTrace()[2]);
		}

		/**
		 * Checks if the current execution environment is of the desired side.
		 * 
		 * @param side desired {@link Side}
		 * @throws IllegalSideException if the side doesn't match
		 */
		public static void assertSide(Side side) {
			Side current = get();
			if (side != current)
				throw new IllegalSideException(current, Thread.currentThread().getStackTrace()[2]);
		}

		/**
		 * Returns the {@link Side} of the current execution environment.
		 * 
		 * @return current side
		 * @see NetworkManager#getSide()
		 */
		@SuppressWarnings("deprecation")
		public static Side get() {
			return Game.getInstance().networkManager().getSide();
		}
	}

	/**
	 * An IllegalSideException indicates that a piece of code was called from
	 * the wrong {@link Side}.
	 * 
	 * @author Vic Nightfall
	 * @see Sided
	 * @see Side
	 */
	public static class IllegalSideException extends NovaException {

		private static final long serialVersionUID = -2732640693411842097L;

		public IllegalSideException(Side target) {
			this(target, Thread.currentThread().getStackTrace()[2]);
		}

		public IllegalSideException(Side target, StackTraceElement method) {
			super("Attempted to call method " + method + " for invalid side " + target);
		}
	}
}
