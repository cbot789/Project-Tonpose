package cs309.tonpose;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by Quade on 11/1/16.
 */
public class InventoryScreen implements Screen {

    final Tonpose tonpose;
    private Texture buttonImage;
    private Stage stage;
    private TextureRegion buttonRegion;
    private TextureRegionDrawable buttonRegionDrawable;
    private ImageButton backButton;
    private BitmapFont font;

    private OrthographicCamera camera;
    private SpriteBatch batch;

    public InventoryScreen(Tonpose t){
        this.tonpose = t;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();
        font = new BitmapFont();

        //add stage and players button
        buttonImage = new Texture(Gdx.files.internal("back.png"));
        buttonRegion = new TextureRegion(buttonImage);
        buttonRegionDrawable = new TextureRegionDrawable(buttonRegion);
        backButton = new ImageButton(buttonRegionDrawable);
        backButton.addListener(new EventListener()
        {
            @Override
            public boolean handle(Event event)
            {
                tonpose.setScreen(tonpose.tonposeScreen);
                return true;
            }
        });
        stage = new Stage();
        stage.addActor(backButton);
    }


    @Override
    public void render(float delta) {
        // clear screen to dark blue color
        Gdx.gl.glClearColor(1f, 1f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        //render stage
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        batch.setProjectionMatrix(camera.combined); // tells spriteBatch to use camera coordinate system
        batch.begin();
        // show all players
        font.setColor(Color.BLACK);
        font.getData().setScale(4f);
        font.draw(batch, "Inventory", 200, 450);
        font.getData().setScale(2f);
        int x = 300;
        int y = 350;
        for (User value : tonpose.users.values()) {
            font.draw(batch, value.name, x, y);
            y -= 30;
        }

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {

    }

}