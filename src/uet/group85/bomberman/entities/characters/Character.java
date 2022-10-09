package uet.group85.bomberman.entities.characters;

import javafx.scene.image.Image;

import uet.group85.bomberman.BombermanGame;
import uet.group85.bomberman.auxilities.Bound;
import uet.group85.bomberman.auxilities.Coordinate;
import uet.group85.bomberman.auxilities.Rectangle;
import uet.group85.bomberman.entities.Entity;
import uet.group85.bomberman.entities.blocks.Block;
import uet.group85.bomberman.graphics.Sprite;

import java.util.List;

public abstract class Character extends Entity {
    public enum Direction {
        UP, DOWN, LEFT, RIGHT, TOTAL
    }

    // Character movement
    protected int stepLength;
    protected int stepDuration;
    protected int stepCounter;
    protected Direction stepDirection;
    protected boolean isLiving;
    protected double deadTime;
    protected final double deadDuration;
    // Character collision
    protected Block obstacle1;
    protected Block obstacle2;

    // Character animation
    protected Image[] defaultFrame;
    protected Image[][] movingFrame;
    protected Image[] dyingFrame;
    protected double[] frameDuration;

    // Constructor
    public Character(Coordinate pos, Rectangle solidArea, int stepLength, int stepDuration) {
        super(pos, solidArea);

        this.stepLength = stepLength;
        this.stepDuration = stepDuration;
        this.stepCounter = 0;
        this.stepDirection = Direction.DOWN;
        isLiving = true;
        deadDuration = 1.2;

        this.hitBox = new Bound(this);
        obstacle1 = null;
        obstacle2 = null;
    }

    protected Image getFrame(Image[] frame, double time, double frameDuration) {
        int index = (int) ((time % (frame.length * frameDuration)) / frameDuration);
        return frame[index];
    }

    public boolean isCollided(Entity other) {
        Bound otherHitBox = other.getHitBox();
        // Check top side
        if ((hitBox.topY < otherHitBox.bottomY && hitBox.topY > otherHitBox.topY)
                && !((hitBox.leftX >= otherHitBox.rightX) || (hitBox.rightX <= otherHitBox.leftX))) {
            return true;
        }
        // Check bottom side
        if ((hitBox.bottomY > otherHitBox.topY && hitBox.bottomY < otherHitBox.bottomY)
                && !((hitBox.leftX >= otherHitBox.rightX) || (hitBox.rightX <= otherHitBox.leftX))) {
            return true;
        }
        // Check left side
        if ((hitBox.leftX < otherHitBox.rightX && hitBox.leftX > otherHitBox.leftX)
                && !((hitBox.topY >= otherHitBox.bottomY) || (hitBox.bottomY <= otherHitBox.topY))) {
            return true;
        }
        // Check right side
        if ((hitBox.rightX > otherHitBox.leftX && hitBox.rightX < otherHitBox.rightX)
                && !((hitBox.topY >= otherHitBox.bottomY) || (hitBox.bottomY <= otherHitBox.topY))) {
            return true;
        }
        return false;
    }

    public boolean isCollided(List<Block> blocks) {
        this.hitBox.update(this);
        // Detect obstacles
        switch (stepDirection) {
            case UP -> {
                hitBox.topY -= stepLength;
                obstacle1 = blocks.get(BombermanGame.WIDTH * (hitBox.topY / Sprite.SCALED_SIZE) // Row size multiply Row index
                        + (hitBox.leftX / Sprite.SCALED_SIZE)); // add Column index -> One dimension index
                obstacle2 = blocks.get(BombermanGame.WIDTH * (hitBox.topY / Sprite.SCALED_SIZE)
                        + (hitBox.rightX / Sprite.SCALED_SIZE));
            }
            case DOWN -> {
                hitBox.bottomY += stepLength;
                obstacle1 = blocks.get(BombermanGame.WIDTH * (hitBox.bottomY / Sprite.SCALED_SIZE)
                        + (hitBox.leftX / Sprite.SCALED_SIZE));
                obstacle2 = blocks.get(BombermanGame.WIDTH * (hitBox.bottomY / Sprite.SCALED_SIZE)
                        + (hitBox.rightX / Sprite.SCALED_SIZE));
            }
            case LEFT -> {
                hitBox.leftX -= stepLength;
                obstacle1 = blocks.get(BombermanGame.WIDTH * (hitBox.topY / Sprite.SCALED_SIZE)
                        + (hitBox.leftX / Sprite.SCALED_SIZE));
                obstacle2 = blocks.get(BombermanGame.WIDTH * (hitBox.bottomY / Sprite.SCALED_SIZE)
                        + (hitBox.leftX / Sprite.SCALED_SIZE));
            }
            case RIGHT -> {
                hitBox.rightX += stepLength;
                obstacle1 = blocks.get(BombermanGame.WIDTH * (hitBox.topY / Sprite.SCALED_SIZE)
                        + (hitBox.rightX / Sprite.SCALED_SIZE));
                obstacle2 = blocks.get(BombermanGame.WIDTH * (hitBox.bottomY / Sprite.SCALED_SIZE)
                        + (hitBox.rightX / Sprite.SCALED_SIZE));
            }
        }
        assert obstacle1 != null;
        assert obstacle2 != null;
        return isCollided(obstacle1) && (!obstacle1.isPassable())
                || isCollided(obstacle2) && (!obstacle2.isPassable());
    }

    public void eliminateNow(double deadTime) {
        this.deadTime = deadTime;
        isLiving = false;
    }

    public void increaseSpeed() {
        this.stepLength += 2;
        this.stepDuration += 1;
    }

    @Override
    public void update() {
        this.hitBox.update(this);
    }
}
