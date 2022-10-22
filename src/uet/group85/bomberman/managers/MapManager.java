package uet.group85.bomberman.managers;

import uet.group85.bomberman.auxilities.Coordinate;
import uet.group85.bomberman.entities.blocks.*;
import uet.group85.bomberman.entities.blocks.BombItem;
import uet.group85.bomberman.entities.blocks.FlameItem;
import uet.group85.bomberman.entities.blocks.SpeedItem;
import uet.group85.bomberman.entities.characters.Balloon;
import uet.group85.bomberman.entities.tiles.Grass;
import uet.group85.bomberman.entities.tiles.Wall;
import uet.group85.bomberman.graphics.Sprite;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MapManager {
    public MapManager() throws FileNotFoundException {
        loadMap(GameManager.level);
    }

    private void initWall(Coordinate mapPos) {
        GameManager.tiles.add(new Wall(mapPos, new Coordinate(mapPos)));
    }

    private void initGrass(Coordinate mapPos) {
        GameManager.tiles.add(new Grass(mapPos, new Coordinate(mapPos)));
    }

    private void initGrass(Coordinate mapPos, Coordinate screenPos, Block[] layers) {
        Grass belowBlock = new Grass(mapPos, screenPos);
        for (Block layer : layers) {
            belowBlock.addLayer(layer);
        }
        GameManager.tiles.add(belowBlock);
    }

    private void initBrick(Coordinate mapPos) {
        Coordinate screenPos = new Coordinate(mapPos);
        initGrass(mapPos, screenPos, new Block[]{new Brick(mapPos, screenPos)});
    }

    private void initPortal(Coordinate mapPos) {
        Coordinate screenPos = new Coordinate(mapPos);
        initGrass(mapPos, screenPos, new Block[]{
                new Portal(mapPos, screenPos),
                new Brick(mapPos, screenPos)
        });
    }

    private void initBomber(Coordinate mapPos) {
        initGrass(mapPos);
        GameManager.bomber.setMapPos(new Coordinate(mapPos));
        GameManager.bomber.setScreenPos(new Coordinate(mapPos));
    }

    private void initBalloon(Coordinate mapPos) {
        initGrass(mapPos);
        GameManager.enemies.add(new Balloon(new Coordinate(mapPos), new Coordinate(mapPos)));
    }

    private void initOneal(Coordinate mapPos) {
        initGrass(mapPos);
    }

    private void initBombItem(Coordinate mapPos) {
        Coordinate screenPos = new Coordinate(mapPos);
        initGrass(mapPos, screenPos, new Block[]{
                new BombItem(mapPos, screenPos),
                new Brick(mapPos, screenPos)
        });
    }

    private void initFlameItem(Coordinate mapPos) {
        Coordinate screenPos = new Coordinate(mapPos);
        initGrass(mapPos, screenPos, new Block[]{
                new FlameItem(mapPos, screenPos),
                new Brick(mapPos, screenPos)
        });
    }

    private void initSpeedItem(Coordinate mapPos) {
        Coordinate screenPos = new Coordinate(mapPos);
        initGrass(mapPos, screenPos, new Block[]{
                new SpeedItem(mapPos, screenPos),
                new Brick(mapPos, screenPos)
        });
    }

    private void initWallPassItem(Coordinate mapPos) {
        Coordinate screenPos = new Coordinate(mapPos);
        initGrass(mapPos, screenPos, new Block[]{
                new WallPassItem(mapPos, screenPos),
                new Brick(mapPos, screenPos)
        });
    }

    private void initBombPassItem(Coordinate mapPos) {
        Coordinate screenPos = new Coordinate(mapPos);
        initGrass(mapPos, screenPos, new Block[]{
                new BombPassItem(mapPos, screenPos),
                new Brick(mapPos, screenPos)
        });
    }

    public void loadMap(int level) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(String.format("res/levels/Level%d.txt", level)));
        GameManager.mapRows = sc.nextInt();
        GameManager.mapCols = sc.nextInt();
        sc.nextLine();
        for (int j = 0; j < GameManager.mapRows; j++) {
            String line = sc.nextLine();
            for (int i = 0; i < GameManager.mapCols; i++) {
                switch (line.charAt(i)) {
                    // Blocks & Tiles
                    case '#' -> initWall(new Coordinate(i, j).multiply(Sprite.SCALED_SIZE));
                    case '*' -> initBrick(new Coordinate(i, j).multiply(Sprite.SCALED_SIZE));
                    case 'x' -> initPortal(new Coordinate(i, j).multiply(Sprite.SCALED_SIZE));
                    // Characters
                    case 'p' -> initBomber(new Coordinate(i, j).multiply(Sprite.SCALED_SIZE));
                    case '1' -> initBalloon(new Coordinate(i, j).multiply(Sprite.SCALED_SIZE));
                    case '2' -> initOneal(new Coordinate(i, j).multiply(Sprite.SCALED_SIZE));
                    // Items
                    case 'b' -> initBombItem(new Coordinate(i, j).multiply(Sprite.SCALED_SIZE));
                    case 'f' -> initFlameItem(new Coordinate(i, j).multiply(Sprite.SCALED_SIZE));
                    case 's' -> initSpeedItem(new Coordinate(i, j).multiply(Sprite.SCALED_SIZE));
                    case 'w' -> initWallPassItem(new Coordinate(i, j).multiply(Sprite.SCALED_SIZE));
                    case 'q' -> initBombPassItem(new Coordinate(i, j).multiply(Sprite.SCALED_SIZE));
                    // Grass as default
                    default -> initGrass(new Coordinate(i, j).multiply(Sprite.SCALED_SIZE));
                }
            }
        }
        sc.close();
    }
}
