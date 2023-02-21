package com.gdx.engine.box2d.entity.item;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.gdx.engine.box2d.component.graphics.SpriteComponent;
import com.gdx.engine.box2d.component.graphics.TextureComponent;
import com.gdx.engine.box2d.component.physics.B2BodyComponent;
import com.gdx.engine.model.config.WindowConfig;
import com.gdx.engine.service.ConfigServiceImpl;
import com.gdx.game.map.CategoryBits;

public class BoxEntity extends Entity implements Disposable {
    private static ConfigServiceImpl configService;

    private static int itemWidth;
    private static int itemHeight;
    private static float ppm;
    private final TextureComponent textureComponent;
    private final B2BodyComponent b2body;

    public BoxEntity(World world, Texture texture, Float x, Float y) {
        configService = ConfigServiceImpl.getInstance();
        WindowConfig windowConfig = configService.getWindowConfig();
        ppm = windowConfig.getCameraConfig().getPpm();

        itemWidth = 22;
        itemHeight = 16;

        textureComponent = new TextureComponent(texture, x * ppm, y * ppm);
        b2body = new B2BodyComponent(world);

        add(textureComponent);
        add(b2body);

        // Sprite data
        SpriteComponent sprite = new SpriteComponent();
        add(sprite);
        sprite.setBounds(0, 0, 105f / ppm, 105f / ppm);

        constructIconBody();

        textureComponent.setBounds(0, 0, itemWidth / ppm, itemHeight / ppm);
    }

    public void constructIconBody() {
        short bodyCategoryBits = CategoryBits.BOX;
        short bodyMaskBits = CategoryBits.GROUND;
        defineBody(bodyCategoryBits, bodyMaskBits);
    }

    private void defineBody(short bodyCategoryBits, short bodyMaskBits) {
        Body body = b2body.getBodyBuilder().type(BodyDef.BodyType.DynamicBody)
                .position(textureComponent.getX(), textureComponent.getY(), ppm)
                .buildBody();

        b2body.setBody(body);
        createBodyFixture(bodyCategoryBits, bodyMaskBits);
    }

    private void createBodyFixture(short categoryBits, short maskBits) {
        Fixture bodyFixture = b2body.getBodyBuilder().newRectangleFixture(b2body.getBody().getPosition(),
                        itemWidth / 2, itemHeight / 2, ppm)
                .categoryBits(categoryBits)
                .maskBits((short) (maskBits | CategoryBits.DATA))
                .setSensor(true)
                .setUserData(this)
                .buildFixture();

        b2body.setBodyFixture(bodyFixture);

        Fixture collisionFixture = b2body.getBodyBuilder().newRectangleFixture(b2body.getBody().getPosition(),
                        itemWidth / 2, itemHeight / 2, ppm)
                .categoryBits(categoryBits)
                .maskBits(maskBits)
                .setUserData(this)
                .buildFixture();
    }

    @Override
    public void dispose() {
        //sprite.dispose();
    }

}