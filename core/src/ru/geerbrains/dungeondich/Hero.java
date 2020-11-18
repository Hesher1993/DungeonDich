package ru.geerbrains.dungeondich;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Hero {
    private ProjectileController projectileController;
    private Vector2 position;
    private Vector2 velocity;
    private Vector2 gameField;
    private TextureRegion texture;
    private boolean shotMultiple;
    private final float speed = 100.0f;

    public Hero(GameMap gameMap, TextureAtlas atlas, ProjectileController projectileController) {
        this.position = new Vector2(100, 100);
        this.velocity = new Vector2(speed,0);
        this.gameField = gameMap.getSize();
        this.texture = atlas.findRegion("tank");
        this.projectileController = projectileController;
    }

    public Hero(TextureAtlas atlas, ProjectileController projectileController) {

    }

    public void update(float dt) {
        move(dt);
        setShotMultiple();
        shot();
    }

    public void render(SpriteBatch batch) {
        float angle = 0f;
        if (velocity.x > 0) angle = 0f;
        if (velocity.x < 0) angle = -180f;
        if (velocity.y > 0) angle = 90f;
        if (velocity.y < 0) angle = -90f;
        batch.draw(texture,
                position.x - 20,
                position.y - 20,
                texture.getRegionWidth()/2f,
                texture.getRegionHeight()/2f,
                texture.getRegionWidth(),
                texture.getRegionHeight(),
                1,
                1,
                angle);
    }

    private void move(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            velocity.x = 0;
            velocity.y = speed;
            position.mulAdd(velocity, dt);
            position.y = Math.min(position.y, gameField.y - 20);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            velocity.x = 0;
            velocity.y = -speed;
            position.mulAdd(velocity, dt);
            position.y = Math.max(position.y, 20);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            velocity.x = -speed;
            velocity.y = 0;
            position.mulAdd(velocity, dt);
            position.x = Math.max(position.x, 20);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            velocity.x = speed;
            velocity.y = 0;
            position.mulAdd(velocity, dt);
            position.x = Math.min(position.x, gameField.x + 20);
        }
    }

    public void shot() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (isShotMultiple()) {
                projectileController.activate(position.x, position.y, velocity.x * 3, velocity.y * 3);
                if (velocity.x != 0) {
                    projectileController.activate(position.x, position.y, (velocity.x - 10) * 3, velocity.y * 3);
                } else {
                    projectileController.activate(position.x, position.y, velocity.x * 3, (velocity.y - 10) * 3);
                }
            } else {
                projectileController.activate(position.x, position.y, velocity.x * 3, velocity.y * 3);
            }
        }
    }

    public boolean isShotMultiple() {
        return shotMultiple;
    }

    public void setShotMultiple() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            shotMultiple = !shotMultiple;
        }
    }
}