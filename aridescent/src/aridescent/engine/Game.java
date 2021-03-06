package aridescent.engine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.Renderable;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

/** Base class for making a game. Comes with its own loop. */
public abstract class Game {
    protected final float DISPLAY_WIDTH;
    protected final float DISPLAY_HEIGHT;
    protected final int DISPLAY_WIDTH_INT;
    protected final int DISPLAY_HEIGHT_INT;
    protected long fps = 0;

    private int fpsTarget = 100;
    private int glClearBits = GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT;

    /** Constructor for Game inheritors
     *
     * @param width horizontal size of window created
     * @param height vertical size of window created
     *               @throws LWJGLException Propogates LWJGLException born from failure to set up Display
     */
    protected Game(int width, int height) throws LWJGLException {
        Display.setDisplayMode(new DisplayMode(width, height));
        Display.create();
        DISPLAY_WIDTH = width;
        DISPLAY_HEIGHT = height;
        DISPLAY_WIDTH_INT = width;
        DISPLAY_HEIGHT_INT = height;
    }

    /** Method for starting the game */
    public void run() {
        init();
        loop();
    }

    /** Method for exiting the game */
    protected void exit() {
        Display.destroy();
        System.exit(0);
    }

    /** Main game loop, runs forever until either {@link #exit} is called
     * or Display.isCloseRequested() returns true
     */
    private void loop() {
        long tick = 0;
        long now = System.currentTimeMillis();
        long old_tick = 0;

        while (!Display.isCloseRequested()) {
            glClear(glClearBits);

            /* Calculates FPS every second */
            if ((System.currentTimeMillis() - now) > 1000) {
                fps = (tick - old_tick);
                old_tick = tick;
                now = System.currentTimeMillis();
            }

            update();
            render();
            poll();

            Display.update();
            Display.sync(fpsTarget);
            util.debug(1, "tick=%d, fps=%d", tick, fps);
            tick++;
        }
    }

    /** Method for setting fps, effective from next tick on.
     *
     * @param target target fps
     */
    protected void setFPS(int target) {
        fpsTarget = target;
    }

    /** Method for setting glClear(bits), effective from next tick on.
     *
     * @param bits bits to use, use opengl enums
     */
    protected void setGlClear(int bits) {
        glClearBits = bits;
    }

    /** Method for returning height of display as a float */
    protected float getHeight() {
        return DISPLAY_HEIGHT;
    }

    /** Method for returning width of display as a float */
    protected float getWidth() {
        return DISPLAY_WIDTH;
    }

    /** Method that should include any initialization neccesary to render, update and poll */
    protected abstract void init();

    /** Method that should include anything related to logic updates */
    protected abstract void update();

    /** Method that should only include rendering of objects or calls to rendering */
    protected abstract void render();

    /** Method that should contain code for checking keyboard and mouse events/information */
    protected abstract void poll();

}
