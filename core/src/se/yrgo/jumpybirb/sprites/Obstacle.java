package se.yrgo.jumpybirb.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class Obstacle {
    private Texture topObstacle;
    private Texture bottomObstacle;
    private Vector2 posTopObstacle, posBottomObstacle; // Position of top and bottom tubes on X axis.

    public static final int OBSTACLE_WIDTH = 40; // pixel width of the tube from the image
    private static final int FLUCTUATION = 340; // so it can move randomly between 0 and 140
    private static final int OBSTACLE_GAP = 200; //  this will be the gap between 2 tubes
    private static final int FLUCTUATION = 140; // so it can move randomly between 0 and 140
    private static final int OBSTACLE_GAP = 140; //  this will be the gap between 2 tubes
    private static final int LOWEST_OPENING = 130; // where from the bottom of the screen can we have top tube
    private Random rand; // to get random top and bottom positions on Y axis
    public Rectangle boundsTop, boundsBot;
    private Rectangle boundSpace;

    public Obstacle(float x) {
        topObstacle = new Texture("UpperObstacle1.png");
        bottomObstacle = new Texture("LowerObstacle1.png");
        rand = new Random();
        posTopObstacle = new Vector2(x, rand.nextInt(FLUCTUATION) + OBSTACLE_GAP + LOWEST_OPENING);
        posBottomObstacle = new Vector2(x, posTopObstacle.y - OBSTACLE_GAP - bottomObstacle.getHeight());
        boundsTop = new Rectangle(posTopObstacle.x, posTopObstacle.y, topObstacle.getWidth() - 5, topObstacle.getHeight() + 5); //Set position of invisible rectangle for top tube
        boundsBot = new Rectangle(posBottomObstacle.x, posTopObstacle.y, bottomObstacle.getWidth() - 5, bottomObstacle.getHeight() - 5); //Set position of invisible rectangle for bottom tube
        boundSpace = new Rectangle(posTopObstacle.x, posTopObstacle.y - OBSTACLE_GAP, topObstacle.getWidth(), topObstacle.getHeight());

    }

    public boolean collides (Rectangle player) {
        return player.overlaps(boundsBot) || player.overlaps(boundsTop);
    }

    public void dispose() {
        topObstacle.dispose();
        bottomObstacle.dispose();
    }

    public void reposition(float x) {
        posTopObstacle.set(x, rand.nextInt(FLUCTUATION) + OBSTACLE_GAP + LOWEST_OPENING);
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