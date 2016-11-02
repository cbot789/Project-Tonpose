package cs309.tonpose;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
	AndroidMethod androidMethod;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		androidMethod=new AndroidMethod(this);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		Intent intent = getIntent();
		String name = intent.getExtras().getString("name");
		initialize(new Tonpose(androidMethod, name), config);
	}
}