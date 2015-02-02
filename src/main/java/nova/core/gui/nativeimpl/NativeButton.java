package nova.core.gui.nativeimpl;

public interface NativeButton extends NativeGuiElement {

	public String getText();

	public void setText(String text);

	public boolean isPressed();

	public void setPressed(boolean isPressed);
}
