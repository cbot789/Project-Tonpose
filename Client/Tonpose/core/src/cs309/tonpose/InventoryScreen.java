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
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import static cs309.tonpose.TonposeScreen.player;

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

    private TextButton inv;
    private TextButton actionButton;
    private Table table;
    private TextButton.TextButtonStyle textButtonStyle;

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
        font.draw(batch, "Inventory", 300, 450);

        font.getData().setScale(2f);
        if(player.equiped!= null){
            font.draw(batch, "equipped: " + player.equiped.name, 100, 350);
        }else{
            font.draw(batch, "equipped: Nothing", 100, 350);
        }
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setBounds(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table.center();
        stage.addActor(table);
        font = new BitmapFont();
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        for (final Item item : player.getInventory()) {
            inv = new TextButton(item.name + " " + item.getCount(), textButtonStyle);
            inv.getLabel().setFontScale(5,5);
            inv.addListener(new EventListener()
            {
                @Override
                public boolean handle(Event event)
                {
                    if(item != player.equiped){
                        player.equipItem(item);
                    }else{
                        player.equipItem(null);
                    }
                    return true;
                }
            });
            table.add(inv);
            table.row();
        }

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
