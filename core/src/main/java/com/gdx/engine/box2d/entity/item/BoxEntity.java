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
import com.gdx.game.map.CategoryBits;

public class BoxEntity extends Entity implements Disposable {

    private static final int itemWidth = 20;
    private static final int itemHeight = 16;

    private final TextureComponent textureComponent;
    private final B2BodyComponent b2body;

    public BoxEntity(World world, Texture texture, Float x, Float y) {
        textureComponent = new TextureComponent(texture, x * 100, y * 100);
        b2body = new B2BodyComponent(world);

        add(textureComponent);
        add(b2body);

        // Sprite data
        SpriteComponent sprite = new SpriteComponent();
        add(sprite);
        sprite.setBounds(0, 0, 105f / 100, 105f / 100);

        constructIconBody();

        textureComponent.setBounds(0, 0, itemWidth / 100f, itemHeight / 100f);
    }

    public void constructIconBody() {
        short bodyCategoryBits = CategoryBits.ITEM;
        short bodyMaskBits = CategoryBits.GROUND;// | CategoryBits.PLATFORM;
        defineBody(bodyCategoryBits, bodyMaskBits);
    }

    private void defineBody(short bodyCategoryBits, short bodyMaskBits) {
        Body body = b2body.getBodyBuilder().type(BodyDef.BodyType.DynamicBody)
                .position(textureComponent.getX(), textureComponent.getY(), 100)
                .buildBody();

        b2body.setBody(body);
        createBodyFixture(bodyCategoryBits, bodyMaskBits);
    }

    private void createBodyFixture(short categoryBits, short maskBits) {
        Fixture bodyFixture = b2body.getBodyBuilder().newRectangleFixture(b2body.getBody().getPosition(), itemWidth / 2, itemHeight / 2, 100)
                .categoryBits(categoryBits)
                .maskBits((short) (maskBits | CategoryBits.PLAYER))
                .setSensor(true)
                .setUserData(this)
                .buildFixture();

        b2body.setBodyFixture(bodyFixture);

        Fixture collisionFixture = b2body.getBodyBuilder().newRectangleFixture(b2body.getBody().getPosition(), itemWidth / 2, itemHeight / 2, 100)
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