package ru.geerbrains.dungeondich.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import ru.geerbrains.dungeondich.GameController;
import ru.geerbrains.dungeondich.GameMap;

public class Hero extends Unit {
    float movementTime;
    float movementMaxTime;
    int targetX, targetY;
    private int movesCount;
    private int maxMovesCount;
    private BitmapFont font;

    public Hero(TextureAtlas atlas, GameController gc) {
        super(gc, 1, 1, 10, 0, 10);
        this.texture = atlas.findRegion("knight");
        this.textureHp = atlas.findRegion("hp");
        this.movementMaxTime = 0.2f;
        this.maxMovesCount = 5;
        this.movesCount = maxMovesCount;
        this.font = new BitmapFont();
        this.targetX = cellX;
        this.targetY = cellY;
    }

    public void update(float dt) {
        checkMovement(dt);
    }

    public boolean isStayStill() {
        return cellY == targetY && cellX == targetX;
    }

    public void checkMovement(float dt) {
        if (Gdx.input.justTouched() && isStayStill()) {
            if (Math.abs(gc.getCursorX() - cellX) + Math.abs(gc.getCursorY() - cellY) == 1) {
                targetX = gc.getCursorX();
                targetY = gc.getCursorY();
            }
        }

        Monster m = gc.getMonsterController().getMonsterInCell(targetX, targetY);
        if (m != null) {
            targetX = cellX;
            targetY = cellY;
            m.takeDamage(1);
            if (!m.isActive()) {
                super.expAdd(1);
            }
        }

        if (!gc.getGameMap().isCellPassable(targetX, targetY)) {
            targetX = cellX;
            targetY = cellY;
        }

        if (!isStayStill()) {
            movementTime += dt;
            if (movementTime > movementMaxTime) {
                movementTime = 0;
                cellX = targetX;
                cellY = targetY;
                lowerMovesCounter();
            }
        }
    }

    public void lowerMovesCounter() {
        if (movesCount > 0) {
            movesCount--;
        } else {
            movesCount = maxMovesCount;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        float px = cellX * GameMap.CELL_SIZE;
        float py = cellY * GameMap.CELL_SIZE;
        if (!isStayStill()) {
            px = cellX * GameMap.CELL_SIZE + (targetX - cellX) * (movementTime / movementMaxTime) * GameMap.CELL_SIZE;
            py = cellY * GameMap.CELL_SIZE + (targetY - cellY) * (movementTime / movementMaxTime) * GameMap.CELL_SIZE;
        }
        batch.draw(texture, px, py);
        // hp bar
        batch.setColor(0.0f, 0.0f, 0.0f, 1.0f);
        batch.draw(texture, px + 1, py + 51, 58, 10);
        batch.setColor(0.7f, 0.0f, 0.0f, 1.0f);
        batch.draw(texture, px + 2, py + 52, 56, 8);
        batch.setColor(0.0f, 1.0f, 0.0f, 1.0f);
        batch.draw(texture, px + 2, py + 52, (float) hp / hpMax * 56, 8);
        //exp bar
        batch.setColor(0.0f, 0.0f, 0.0f, 1.0f);
        batch.draw(textureHp, px + 1, py + 63, 58, 10);
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        batch.draw(textureHp, px + 2, py + 64, 56, 8);
        batch.setColor(0.25f, 0.66f, 1.0f, 1.0f);
        batch.draw(textureHp, px + 2, py + 64, (float) exp / expMax * 56, 8);

        font.draw(batch, String.valueOf(movesCount), px +2, py + 88);

        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }
}
