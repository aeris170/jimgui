package org.ice1000.jimgui;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;

import static org.ice1000.jimgui.util.JImGuiUtil.getBytes;

/**
 * @author ice1000
 * @since v0.1
 */
@SuppressWarnings("WeakerAccess")
public class JImGui extends JImGuiGen implements AutoCloseable, Closeable {
	/** package-private by design， */
	long nativeObjectPtr;
	private @NotNull JImVec4 background;
	private @Nullable JImGuiIO io;

	public JImGui() {
		nativeObjectPtr = allocateNativeObjects();
		io = new JImGuiIO();
		background = new JImVec4(1.0f, 0.55f, 0.60f, 1.00f);
	}

	@Override
	public void close() {
		background.close();
		deallocateNativeObjects(nativeObjectPtr);
		io = null;
	}

	public void text(@NotNull JImVec4 color, @NotNull String text) {
		textColored(color.nativeObjectPtr, getBytes(text));
	}

	public boolean arrowButton(@NotNull String text, @NotNull JImDir direction) {
		return arrowButton(getBytes(text), direction.intValue);
	}

	public @Nullable JImGuiIO tryGetIO() {
		return io;
	}

	public @NotNull JImGuiIO getIO() {
		if (null == io) alreadyDisposed();
		return io;
	}

	@Contract(pure = true)
	public boolean isDisposed() {
		return io == null;
	}

	/** @return shouldn't be closed, will close automatically */
	public @NotNull JImVec4 getBackground() {
		return background;
	}

	/** @param background shouldn't be closed, will close automatically */
	public void setBackground(@NotNull JImVec4 background) {
		this.background.close();
		this.background = background;
	}

	public boolean windowShouldClose() {
		return windowShouldClose(nativeObjectPtr);
	}

	@Contract(" -> fail")
	private void alreadyDisposed() {
		throw new IllegalStateException("Window already disposed.");
	}

	public void render() {
		render(nativeObjectPtr, background.nativeObjectPtr);
	}

	public native void initNewFrame();

	//region Private native interfaces
	private static native long allocateNativeObjects();
	private static native void deallocateNativeObjects(long nativeObjectPtr);
	private static native boolean windowShouldClose(long nativeObjectPtr);
	private static native void render(long nativeObjectPtr, long colorPtr);
	private static native void textColored(long colorPtr, byte[] text);
	//endregion
}
