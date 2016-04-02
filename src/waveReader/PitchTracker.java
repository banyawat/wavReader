package waveReader;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class PitchTracker {
	static int sampleRate;
	static int windows = 256;
	public PitchTracker(double[] data, WaveReader wave){
		sampleRate = wave.sampleRate;
		ACF(data, wave);
		
	}
	
	public void ACF(double[] data, WaveReader wave){
		double[] f0 = new double[100];
		double[] t = new double[100];
		double windowSize = 0.05*sampleRate;
		double stepSize = windowSize/2; //(stepSize = windowSize - overlapSize) (overlapSize = windowSize/2)
		double max;
		double[] R = new double[(int) windowSize];
		int N = wave.subChunk2Size/wave.blockAlign;
		int i = 0,j = 0,k = 0,m = 0,n = 0,T = 0;
		int minT = sampleRate/500,maxT = sampleRate/50,indexT = 0;
		try {
			System.out.println("Writing Files... Pitch");
			PrintWriter writer = new PrintWriter("pitch.txt", "UTF-8");
			
			for (k = 0; k < (N-windowSize); k += stepSize) //move Window
			{
				//calculate autocorrelation function R(n)
				//shift data
				double[] sample = new double[(int) windowSize];
				for (j = 0; j < windowSize; j++)
				{
					sample[j] = data[j+k];
				}
				//find R[n]
				for (n = 0; n < windowSize; n++)
				{
					R[n] = 0;
					for (m = n; m < windowSize; m++)
					{
						R[n] += sample[m]*sample[m-n]; 	
					}
				}
				max = R[minT];
				indexT = minT;
				//find T that has maximum R(T)
				for (T = minT; T < maxT; T++)
				{
					 if(R[T] > max)
		            {
		                max = R[T];
		                indexT = T;
		            }
				}	
				f0[i] = sampleRate/indexT;
		        t[i] = (double)k/sampleRate;
		        writer.write(t[i]+" ");
				writer.println(f0[i]);
		        i++;
			}
			writer.close();
			System.out.println("Writing done");
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			System.out.println("Unsupported file format");
			e.printStackTrace();
		}
	}
}
