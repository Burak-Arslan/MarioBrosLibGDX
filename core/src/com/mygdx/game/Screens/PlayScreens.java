package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MarioBros;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.Sprites.Mario;

public class PlayScreens implements Screen {

    private MarioBros game;
    private OrthographicCamera gameCame;
    private Viewport gamePort;
    Hud hud;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;

    Mario mario;


    public void Update(float dt) {
        try {
            HandleInput(dt);
            mario.world.step(0.25f, 2, 2);
            gameCame.update();
            renderer.setView(gameCame);
        } catch (Exception ex) {

        }
    }

    public void HandleInput(float dt) {
        if (Gdx.input.isTouched()) {
            gameCame.position.x += 100 * dt; // ekrana dokundukça 100er 100er  sağa kayması scrolling olmasını sağlayan yer
        }
    }

    public PlayScreens(MarioBros game) {
        try {
            this.game = game;
            hud = new Hud(game.batch);
            gameCame = new OrthographicCamera();
            gamePort = new FitViewport(MarioBros.V_WIDTH / MarioBros.PPM, MarioBros.V_HEIGHT / MarioBros.PPM, gameCame); // Oyunun başlangıç yerinin ayarlandığı yer


            mapLoader = new TmxMapLoader();
            map = mapLoader.load("level1.tmx");
            renderer = new OrthogonalTiledMapRenderer(map, 1 / MarioBros.PPM);
            gameCame.position.set(Gdx.graphics.getWidth() / 22, Gdx.graphics.getHeight() / 22, 0); // Ekrana oturmasını sağladığım yer

            world = new World(new Vector2(0, 0), true);
            mario = new Mario(world);
            box2DDebugRenderer = new Box2DDebugRenderer();

            BodyDef bodyDef = new BodyDef();
            PolygonShape polygonShape = new PolygonShape();
            FixtureDef fixtureDef = new FixtureDef();
            Body body;

            /*
            * map.getLayers().get(2).getObjects()
            * yukarıdaki kod parçası harita üzerinden istediğimiz layer içerisindeki bütün objeleri almamızı sağladı
            * map editör sayesinde katman katman yapılabiliyor.
            * */
            for (MapObject mapObject : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();

                bodyDef.type = BodyDef.BodyType.StaticBody;
                bodyDef.position.set(rectangle.getX() + rectangle.getWidth() / 14, rectangle.getY() + rectangle.getHeight() / 14);

                body = world.createBody(bodyDef);

                polygonShape.setAsBox(rectangle.getWidth() / 2, rectangle.getHeight() / 2);
                fixtureDef.shape = polygonShape;
                body.createFixture(fixtureDef);

            }
//            //Bütün katmanlar için yapıyoruz
            for (MapObject mapObject : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();

                bodyDef.type = BodyDef.BodyType.StaticBody;
                bodyDef.position.set(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 3);

                body = world.createBody(bodyDef);

                polygonShape.setAsBox(rectangle.getWidth() / 4, rectangle.getHeight() / 2);
                fixtureDef.shape = polygonShape;
                body.createFixture(fixtureDef);

            }
//
            for (MapObject mapObject : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();

                bodyDef.type = BodyDef.BodyType.StaticBody;
                bodyDef.position.set(rectangle.getX() + rectangle.getWidth() - 200, rectangle.getY() + rectangle.getHeight() - 42);

                body = world.createBody(bodyDef);

                polygonShape.setAsBox(rectangle.getWidth() / 2, rectangle.getHeight() / 3);
                fixtureDef.shape = polygonShape;
                body.createFixture(fixtureDef);

            }
            for (MapObject mapObject : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();

                bodyDef.type = BodyDef.BodyType.StaticBody;
                bodyDef.position.set(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2);

                body = world.createBody(bodyDef);

                polygonShape.setAsBox(rectangle.getWidth() / 4, rectangle.getHeight() / 4);
                fixtureDef.shape = polygonShape;
                body.createFixture(fixtureDef);

            }

        } catch (Exception ex) {
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();
        box2DDebugRenderer.render(world, gameCame.combined);
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
