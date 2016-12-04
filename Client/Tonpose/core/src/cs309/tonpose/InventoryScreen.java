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
public class InventoryScreen implements Screen {

    final Tonpose tonpose;
    private Texture buttonImage,Cabbage,Log;
    private Stage stage;
    private TextureRegion buttonRegion;
    private TextureRegionDrawable buttonRegionDrawable;
    private ImageButton backButton;
    private BitmapFont font;

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private int currentInvSize = 0;
    private TextButton inv[];
    private TextButton base;
    private TextButton mod;
    private TextButton craft;
    private Table table = new Table();
    private Table craftTable = new Table();
    private TextButton.TextButtonStyle textButtonStyle;
    private enum invMode{
        equipMode, baseMode, modMode
    }
    private invMode currentMode;

    public InventoryScreen(Tonpose t){
        this.tonpose = t;
        inv = new TextButton[player.invSize];
        currentMode = invMode.equipMode;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();
        font = new BitmapFont();

        //add stage and players button
        buttonImage = new Texture(Gdx.files.internal("back.png"));
        Log=new Texture(Gdx.files.internal("shlog.png"));
        Cabbage= new Texture(Gdx.files.internal("cabbage.png"));
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

        //render stage
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        batch.setProjectionMatrix(camera.combined); // tells spriteBatch to use camera coordinate system
        batch.begin();
        // show all players
        font.setColor(Color.FIREBRICK);
        font.getData().setScale(4f);
        font.draw(batch, "Inventory", 300, 450);
        batch.draw(Cabbage, 100, 100);
        batch.draw(Log, 25, 300);
        batch.draw(Cabbage, 600,350);
        batch.draw(Log, 575, 80);
        font.getData().setScale(2f);
        if(player.equiped!= null){
            font.draw(batch, "equipped: " + player.equiped.name, 100, 350);
        }else{
            font.draw(batch, "equipped: Nothing", 100, 350);
        }
        batch.end();

        if( player.base != null){
            base.setText("Base: "+ player.base.name);
        }else{
            base.setText("Base: Nothing");
        }

        if( player.mod != null) {
            mod.setText("Mod: " + player.mod.name);
        }else{
            mod.setText("Mod: Nothing");
        }
        int i = 0;
        for (final cs309.tonpose.map.Item item : player.getInventory()) {
            inv[i].setText(item.name + " " + item.getCount());
            i++;
        }

    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() { //TODO needs comments
        Gdx.input.setInputProcessor(stage);

        table.setBounds(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table.center();
        stage.addActor(table);
        font = new BitmapFont();
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        updateTable();
        /*
        table.clear();
        currentInvSize = 0;

        for (final cs309.tonpose.map.Item item : player.getInventory()) {
            //inv = new TextButton[player.invSize];
            inv[currentInvSize] = new TextButton(item.name + " " + item.getCount(), textButtonStyle);
            inv[currentInvSize].getLabel().setFontScale(5,5);
            inv[currentInvSize].addListener(new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    if(currentMode == invMode.equipMode){
                        if(item != player.equiped){
                            if(item.hasAction){
                                player.equipItem(item);
                            }
                        }else{
                            player.equipItem(null);
                        }
                    }else if(currentMode == invMode.baseMode){
                        player.base = item;
                        currentMode = invMode.equipMode;
                    }else{
                        player.mod = item;
                        currentMode = invMode.equipMode;
                    }

                }
            });
            table.add(inv[currentInvSize]);
            table.row();
            currentInvSize++;
        }*/

        craftTable.setBounds(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        craftTable.right();
        stage.addActor(craftTable);
        craftTable.clear();
        if( player.base != null){
            base = new TextButton("Base: "+ player.base.name, textButtonStyle);
        }else{
            base = new TextButton("Base: Nothing", textButtonStyle);
        }
        base.getLabel().setFontScale(5,5);
        base.addListener(new ClickListener()
        {
            @Override                                                   //FIXME buttons for crafting updating without refreshing
            public void clicked(InputEvent event, float x, float y)
            {
                currentMode = invMode.baseMode;
            }
        });
        craftTable.add(base);
        craftTable.row();
        if( player.mod != null) {
            mod = new TextButton("Mod: " + player.mod.name, textButtonStyle);
        }else{
            mod = new TextButton("Mod: Nothing", textButtonStyle);
        }
        mod.getLabel().setFontScale(5,5);
        mod.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                currentMode = invMode.modMode;
            }
        });
        craftTable.add(mod);
        craftTable.row();
        craftTable.row();

        craft = new TextButton("Craft!", textButtonStyle);

        craft.getLabel().setFontScale(5,5);
        craft.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                player.craft();
                updateTable();
            }
        });
        craftTable.add(craft);
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

    private void updateTable(){
        table.clear();
        currentInvSize = 0;
        for (final cs309.tonpose.map.Item item : player.getInventory()) {
            //inv = new TextButton[player.invSize];
            inv[currentInvSize] = new TextButton(item.name + " " + item.getCount(), textButtonStyle);
            inv[currentInvSize].getLabel().setFontScale(5,5);
            inv[currentInvSize].addListener(new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    if(currentMode == invMode.equipMode){
                        if(item != player.equiped){
                            if(item.hasAction){
                                player.equipItem(item);
                            }
                        }else{
                            player.equipItem(null);
                        }
                    }else if(currentMode == invMode.baseMode){
                        player.base = item;
                        currentMode = invMode.equipMode;
                    }else{
                        player.mod = item;
                        currentMode = invMode.equipMode;
                    }

                }
            });
            table.add(inv[currentInvSize]);
            table.row();
            currentInvSize++;
        }
    }

}
