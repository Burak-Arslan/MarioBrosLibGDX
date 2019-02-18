package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Mario extends Sprite {
    public World world;
    public Body b2body;

    public Mario(World world) {
        this.world = world;
        defineMario();
    }

    private void defineMario() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(12, 12);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bodyDef);


        FixtureDef fDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(3);

        fDef.shape = shape;
        b2body.createFixture(fDef);
    }
}
