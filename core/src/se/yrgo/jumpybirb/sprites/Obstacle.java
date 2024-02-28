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
    private static final float FLUCTUATION = 140f; // so it can move randomly between 0 and 140
    private static final float OBSTACLE_GAP = 140f; //  this will be the gap between 2 tubes
    private static final float LOWEST_OPENING = 130f; // where from the bottom of the screen can we have top tube
    private Random rand; // to get random top and bottom positions on Y axis
    public Rectangle boundsTop;
    public Rectangle boundsBot;
    private Rectangle boundSpace;


    public Obstacle(float x) {
        topObstacle = new Texture("UpperObstacle1.png");
        bottomObstacle = new Texture("LowerObstacle1.png");
        rand = new Random();

        // Generate random positions for top and bottom obstacles
        float topY = rand.nextFloat(FLUCTUATION) + OBSTACLE_GAP + LOWEST_OPENING;
        float bottomY = topY - OBSTACLE_GAP - bottomObstacle.getHeight();

        // Set positions for top and bottom obstacles
        posTopObstacle = new Vector2(x, topY);
        posBottomObstacle = new Vector2(x, bottomY);

        // Update bounds for top and bottom obstacles
        // Adjust bounding box dimensions to match the visual representation of obstacles
        boundsTop = new Rectangle(posTopObstacle.x, posTopObstacle.y, 100, 300); // Adjusted width and height
        boundsBot = new Rectangle(posBottomObstacle.x, posBottomObstacle.y, 100, 300); // Adjusted width and height

        // Update boundSpace if necessary
        boundSpace = new Rectangle(posTopObstacle.x, posTopObstacle.y - OBSTACLE_GAP, topObstacle.getWidth(), topObstacle.getHeight());
    }

    public boolean collidesWith(Rectangle playerBounds) {
        // Check if the player's bounds intersect with the obstacle's bounds
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
