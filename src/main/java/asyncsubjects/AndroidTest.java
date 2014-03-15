package asyncsubjects;

import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

public class AndroidTest extends Activity {

	int raceOnMe;
	Particle particle;
	
	static class Particle {
		int x;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		class AT extends AsyncTask<Particle, Void, Void> {
			@Override
			protected Void doInBackground(Particle... arg0) {
				raceOnMe = 2;
				m(arg0[0]);
				return null;
			}
			@Override
			protected void onPostExecute(Void result) {
				
			}
		};
		
		particle = new Particle();
		raceOnMe = 1;
		m(particle);
		AT async = new AT();
		async.execute(particle);
	}
	
	private void m(Particle particle) {
		particle.x = 2;
	}
}