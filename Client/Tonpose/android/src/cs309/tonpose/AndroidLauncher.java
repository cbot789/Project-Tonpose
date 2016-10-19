package cs309.tonpose;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import cs309.tonpose.TonposeGame;

public class AndroidLauncher extends AndroidApplication {
	AndroidMethod androidMethod;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		androidMethod=new AndroidMethod(this);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new TonposeGame(androidMethod), config);
	}
}