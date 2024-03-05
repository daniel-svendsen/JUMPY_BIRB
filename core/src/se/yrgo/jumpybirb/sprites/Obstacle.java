package se.yrgo.jumpybirb.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

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
    private Random rand; // to get random top and bottom positions on Y axis
    public Rectangle boundsTop;
    public Rectangle boundsBot;
    private Rectangle boundSpace;
    private boolean isPassed; // Variable to track whether the obstacle is passed

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

    public void reposition(float x) {
        posTopObstacle.set(x, rand.nextFloat(FLUCTUATION) + OBSTACLE_GAP + LOWEST_OPENING);
        posBottomObstacle.set(x, posTopObstacle.y - OBSTACLE_GAP - bottomObstacle.getHeight());
        boundsTop.setPosition(posTopObstacle.x, posTopObstacle.y);
        boundsBot.setPosition(posBottomObstacle.x, posBottomObstacle.y);
        boundSpace.setPosition(posTopObstacle.x, posTopObstacle.y - OBSTACLE_GAP);
        isPassed = false;
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
