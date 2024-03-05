package se.yrgo.jumpybirb.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

public class Obstacle {
    public static final String TAG = Birb.class.getSimpleName();
    private Texture topObstacle;
    private Texture bottomObstacle;
    private Vector2 posTopObstacle;
    private Vector2 posBottomObstacle; // Position of top and bottom tubes on X axis.

    public static final float OBSTACLE_WIDTH = 40f; // pixel width of the tube from the image
    private static final float FLUCTUATION = 340f; // so it can move randomly between 0 and 140
    private static final float OBSTACLE_GAP = 200f; //  this will be the gap between 2 tubes
    private static final float LOWEST_OPENING = 130f; // where from the bottom of the screen can we have top tube
    private static final int OBSTACLE_COUNT = 4;
    private static final float OBSTACLE_SPACING = 300f;
    private Random rand; // to get random top and bottom positions on Y axis
    public Rectangle boundsTop;
    public Rectangle boundsBot;
    private Rectangle boundSpace;
    private boolean isPassed; // Variable to track whether the obstacle is passed
    private boolean isScored; // Variable to make sure an obstacle give 1 score.

    public Obstacle(float x) {
        topObstacle = new Texture("UpperObstacle1.png");
        bottomObstacle = new Texture("LowerObstacle1.png");
        rand = new Random();
        posTopObstacle = new Vector2(x, rand.nextFloat(FLUCTUATION) + OBSTACLE_GAP + LOWEST_OPENING);
        posBottomObstacle = new Vector2(x, posTopObstacle.y - OBSTACLE_GAP - bottomObstacle.getHeight());
        boundsTop = new Rectangle(posTopObstacle.x, posTopObstacle.y, topObstacle.getWidth() - 5f, topObstacle.getHeight() + 5f); //Set position of invisible rectangle for top tube
        boundsBot = new Rectangle(posBottomObstacle.x, posBottomObstacle.y, bottomObstacle.getWidth() - 5f, bottomObstacle.getHeight() - 5f); //Set position of invisible rectangle for bottom tube
        boundSpace = new Rectangle(posTopObstacle.x, posTopObstacle.y - OBSTACLE_GAP, topObstacle.getWidth(), topObstacle.getHeight());
        isPassed = false;
        isScored = false;
    }

    public static Array<Obstacle> createArray() {
        Array<Obstacle> obstacles = new Array<>();
        for (int i = 1; i <= OBSTACLE_COUNT; i++) {
            obstacles.add(new Obstacle(i * (OBSTACLE_SPACING + Obstacle.OBSTACLE_WIDTH)));
        }
        return obstacles;
    }

    public void update(OrthographicCamera camera) {
        if (camera.position.x - (camera.viewportWidth / 2) > posTopObstacle.x + topObstacle.getWidth()) {
            reposition(posTopObstacle.x + (OBSTACLE_WIDTH + OBSTACLE_SPACING) * OBSTACLE_COUNT);
        }
    }

    public void reposition(float x) {
        posTopObstacle.set(x, rand.nextFloat(FLUCTUATION) + OBSTACLE_GAP + LOWEST_OPENING);
        posBottomObstacle.set(x, posTopObstacle.y - OBSTACLE_GAP - bottomObstacle.getHeight());
        boundsTop.setPosition(posTopObstacle.x, posTopObstacle.y);
        boundsBot.setPosition(posBottomObstacle.x, posBottomObstacle.y);
        boundSpace.setPosition(posTopObstacle.x, posTopObstacle.y - OBSTACLE_GAP);
        isPassed = true;
    }

    // Add the draw method to draw the obstacle
    public void draw(SpriteBatch batch, OrthographicCamera camera) {
        if (posTopObstacle.x > 320) {
            batch.draw(topObstacle, posTopObstacle.x, posTopObstacle.y);
            batch.draw(bottomObstacle, posBottomObstacle.x, posBottomObstacle.y);
        }
    }

    // Method to check if the bird has passed the obstacle
    public boolean isPassed() {
        return isPassed;
    }

    public boolean collidesWith(Rectangle playerBounds) {
        if (Intersector.overlaps(playerBounds, boundsTop)) {
            Gdx.app.log(TAG, "collision detected with top obstacle at coordinates: (" + posTopObstacle.x + ", " + posTopObstacle.y + ")");
            return true;
        }
        if (Intersector.overlaps(playerBounds, boundsBot)) {
            Gdx.app.log(TAG, "collision detected with bottom obstacle at coordinates: (" + posBottomObstacle.x + ", " + posBottomObstacle.y + ")");
            return true;
        }
        return false;
    }

    public void dispose() {
        topObstacle.dispose();
        bottomObstacle.dispose();
    }

    public boolean isScored() {
        return isScored;
    }

    public void setScored(boolean scored) {
        isScored = scored;
    }

    public Texture getTopObstacle() {
        return topObstacle;
    }

    public Texture getBottomObstacle() {
        return bottomObstacle;
    }

    public Vector2 getPosTopObstacle() {
        return posTopObstacle;
    }

    public Vector2 getPosBottomObstacle() {
        return posBottomObstacle;
    }
}
