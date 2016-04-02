package waveReader;

public class test {
	public static void main(String[] args) {
		WaveReader wave = new WaveReader("sound/th_na4.wav");
		double test[] = wave.getData();
		PitchTracker pitch = new PitchTracker(test, wave);
	}
}