package nova.wrappertests.depmodules;

import nova.core.entity.component.Player;
import nova.core.network.NetworkManager;
import nova.core.network.Packet;
import se.jbee.inject.bind.BinderModule;

/**
 * @author Calclavia
 */
public class FakeNetworkModule extends BinderModule {
	@Override
	protected void declare() {
		bind(NetworkManager.class).to(FakeNetworkManager.class);
	}

	public static class FakeNetworkManager extends NetworkManager {
		@Override
		public Packet newPacket() {
			return new Packet() {
				@Override
				public int getID() {
					return 0;
				}

				@Override
				public Packet setID(int id) {
					return this;
				}

				@Override
				public Player player() {
					return null;
				}

				@Override
				public Packet writeBoolean(boolean value) {
					return this;
				}

				@Override
				public Packet writeByte(int value) {
					return this;
				}

				@Override
				public Packet writeShort(int value) {
					return this;
				}

				@Override
				public Packet writeInt(int value) {
					return this;
				}

				@Override
				public Packet writeLong(long value) {
					return this;
				}

				@Override
				public Packet writeChar(int value) {
					return this;
				}

				@Override
				public Packet writeFloat(float value) {
					return this;
				}

				@Override
				public Packet writeDouble(double value) {
					return this;
				}

				@Override
				public Packet writeString(String value) {
					return this;
				}

				@Override
				public Packet writeBytes(byte[] array) {
					return this;
				}

				@Override
				public byte[] readBytes(int length) {
					return new byte[0];
				}

				@Override
				public boolean readBoolean() {
					return false;
				}

				@Override
				public byte readByte() {
					return 0;
				}

				@Override
				public short readUnsignedByte() {
					return 0;
				}

				@Override
				public short readShort() {
					return 0;
				}

				@Override
				public int readInt() {
					return 0;
				}

				@Override
				public long readUnsignedInt() {
					return 0;
				}

				@Override
				public long readLong() {
					return 0;
				}

				@Override
				public char readChar() {
					return 0;
				}

				@Override
				public float readFloat() {
					return 0;
				}

				@Override
				public double readDouble() {
					return 0;
				}

				@Override
				public String readString() {
					return null;
				}
			};
		}

		@Override
		public void sendPacket(Packet packet) {

		}

		@Override
		public void sendChat(Player player, String message) {

		}

		@Override
		public boolean isServer() {
			return true;
		}
	}
}

