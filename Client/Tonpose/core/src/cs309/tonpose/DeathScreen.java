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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import static cs309.tonpose.TonposeScreen.player;

/**
 * Created by Quade on 11/1/16.
 */
public class DeathScreen implements Screen {

    final Tonpose tonpose;
    private Texture buttonImage,Scared,Background;
    private Stage stage;
    private TextureRegion buttonRegion;
    private TextureRegionDrawable buttonRegionDrawable;
    private ImageButton backButton;
    private BitmapFont font;

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private TextButton inv;
    private Table table = new Table();
    private TextButton.TextButtonStyle textButtonStyle;

    public DeathScreen(Tonpose t){
        this.tonpose = t;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();
        font = new BitmapFont();

        //add stage and players button
        buttonImage = new Texture(Gdx.files.internal("back.png"));
        Scared=new Texture(Gdx.files.internal("mainScared.png"));
        Background=new Texture(Gdx.files.internal("deadbackground.png"));
        buttonRegion = new TextureRegion(buttonImage);
        buttonRegionDrawable = new TextureRegionDrawable(buttonRegion);
        backButton = new ImageButton(buttonRegionDrawable);
        backButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                tonpose.setScreen(tonpose.tonposeScreen);
            }
        });
        stage = new Stage();
        stage.addActor(backButton);
    }


    @Override
    public void render(float delta) {
        // clear screen to light blue color
        Gdx.gl.glClearColor(130/255f, 200/255f, 230/255f, 1); //choose color by dividing rgb choices by 255f
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();



        batch.setProjectionMatrix(camera.combined); // tells spriteBatch to use camera coordinate system
        batch.begin();
        batch.draw(Background,0,0);
        // show all players
        font.setColor(Color.FIREBRICK);
        font.getData().setScale(4f);
        font.draw(batch, TonposeScreen.player.userName, 300, 450);
        font.draw(batch, "has been slain", 225, 400);
        batch.draw(Scared,215,200);
        batch.end();

        //render stage
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        table.setBounds(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table.center();
        stage.addActor(table);
        font = new BitmapFont();
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        table.clear();
        inv = new TextButton("Quit to menu", textButtonStyle);
        inv.getLabel().setFontScale(5,5);
        inv.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                tonpose.menu();
            }
        });
        table.add(inv);
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
