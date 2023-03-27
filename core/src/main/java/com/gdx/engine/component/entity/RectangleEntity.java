package com.gdx.engine.component.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.gdx.engine.component.graphics.AnimationComponent;
import com.gdx.engine.component.graphics.SpriteComponent;
import com.gdx.engine.component.graphics.TextureComponent;
import com.gdx.engine.component.physics.B2BodyComponent;
import com.gdx.engine.model.map.SpriteAnimation;
import com.gdx.engine.model.map.TiledMapLayerData;
import com.gdx.engine.service.ServiceFactoryImpl;
import com.gdx.engine.util.AnimationUtils;
import com.gdx.engine.util.OperationUtil;
import org.apache.commons.lang3.StringUtils;

public class RectangleEntity extends AbstractEntity {

    private TextureComponent textureComponent;
    private B2BodyComponent b2body;
    private final TiledMapLayerData layerData;
    private final Rectangle rect;

    public RectangleEntity(RectangleMapObject entity, TiledMapLayerData layerData) {
        super();

        this.layerData = layerData;
        this.rect = entity.getRectangle();

        if (layerData.getSpriteAnimation() != null) {
            createSpriteAnimationRectangle();
        } else if (layerData.getTextureAtlasName().equals(StringUtils.EMPTY) && layerData.getTextureName().equals(StringUtils.EMPTY)) {
            createSimpleRectangle();
        } else {
            createSpriteRectangle();
        }
    }

    private void createSimpleRectangle() {
        Vector2 centerPos = new Vector2(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2);
        bodyBuilder.type(OperationUtil.getBodyTypeDef(layerData.isStaticBody()))
                .position(centerPos, ppm)
                .buildBody();

        bodyBuilder.newRectangleFixture(centerPos, rect.getWidth() / 2, rect.getHeight() / 2, ppm)
                .categoryBits(Short.parseShort(layerData.getCategoryBits()))
                .friction(layerData.getFriction())
                .setSensor(!layerData.isSensor())
                .buildFixture();
    }

    private void createSpriteRectangle() {
        textureComponent = new TextureComponent(ServiceFactoryImpl.getAssetService()
                .getTexture(layerData.getTextureAtlasName(), layerData.getTextureName()),
                rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2);
        b2body = new B2BodyComponent(ServiceFactoryImpl.getBox2DWorldService().getWorld());

        add(textureComponent);
        add(b2body);

        SpriteComponent sprite = new SpriteComponent();
        add(sprite);
        sprite.setBounds(0, 0, 105f / ppm, 105f / ppm);

        defineBody();

        textureComponent.setBounds(0, 0, rect.getWidth() / ppm, rect.getHeight() / ppm);
    }

    private void createSpriteAnimationRectangle() {
        Texture texture = ServiceFactoryImpl.getAssetService().getTexture(
                layerData.getTextureAtlasName(), layerData.getTextureName());

        SpriteAnimation mapEntityAnimation = layerData.getSpriteAnimation();
        AnimationComponent<TextureRegion> animation = AnimationUtils.createAnimation(texture,
                mapEntityAnimation.getFrameDuration() / ppm,
                mapEntityAnimation.getFirstFrameCount(),
                mapEntityAnimation.getLastFrameCount(),
                mapEntityAnimation.getOffsetX(),
                mapEntityAnimation.getOffsetY(),
                mapEntityAnimation.getWidth(),
                mapEntityAnimation.getHeight());

        SpriteComponent sprite = new SpriteComponent(
                (rect.getX() + rect.getWidth() / 2) / ppm,
                (rect.getY() + rect.getHeight() / 2) / ppm);

        add(animation);
        add(sprite);

        sprite.setBounds(sprite.getX(), sprite.getY(), rect.getWidth() / ppm, rect.getHeight() / ppm);
    }

    private void defineBody() {
        Body body = b2body.getBodyBuilder().type(OperationUtil.getBodyTypeDef(layerData.isStaticBody()))
                .position(textureComponent.getX(), textureComponent.getY(), ppm)
                .buildBody();

        b2body.setBody(body);
        createBodyFixture();
    }

    private void createBodyFixture() {
        Fixture bodyFixture = b2body.getBodyBuilder().newRectangleFixture(b2body.getBody().getPosition(),
                        rect.getWidth() / 2, rect.getHeight() / 2, ppm)
                .categoryBits(Short.parseShort(layerData.getCategoryBits()))
                .maskBits(Short.parseShort(layerData.getMaskBits()))
                .setSensor(layerData.isSensor())
                .setUserData(this)
                .buildFixture();

        b2body.setBodyFixture(bodyFixture);

        b2body.getBodyBuilder().newRectangleFixture(b2body.getBody().getPosition(),
                        rect.getWidth() / 2, rect.getHeight() / 2, ppm)
                .categoryBits(Short.parseShort(layerData.getCategoryBits()))
                .maskBits(Short.parseShort(layerData.getMaskBits()))
                .setUserData(this)
                .buildFixture();
    }

}
