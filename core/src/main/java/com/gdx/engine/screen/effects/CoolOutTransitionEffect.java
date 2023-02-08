package com.gdx.engine.screen.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.gdx.engine.screen.transition.ImmediateModeRendererUtils;
import com.gdx.engine.screen.transition.TransitionEffect;

public class CoolOutTransitionEffect extends TransitionEffect {

    private final Color color = new Color();

    public CoolOutTransitionEffect(float duration) {
        super(duration);
    }

    @Override
    public void render(Screen current, Screen next) {
        //current.show();
        current.render(Gdx.graphics.getDeltaTime());
        color.set(20f, 20f, 20f, 0.5f);

        Gdx.gl20.glEnable(GL20.GL_BLEND);

        ImmediateModeRendererUtils.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        ImmediateModeRendererUtils.drawFillArcCircle(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2,
                getAlpha() * 500, color);

        ShapeRenderer shapeRenderer = new ShapeRenderer();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(color.r, color.g, color.b, color.a);

//        for (int i = 0; i <= segNum + 1; i++, angle = i * arcNum) {
        shapeRenderer.arc(400, 300, 50, 0, 1);
//        }

        shapeRenderer.end();

        Gdx.gl20.glDisable(GL20.GL_BLEND);
    }

}
