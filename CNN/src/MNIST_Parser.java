import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MNIST_Parser {
	public void parse_training_data(String source, int length, Displayer displayer, CNN_Driver cnn, boolean display) {
		boolean first = true;
		int times = 1000000000;
		for (int f = 0; f < times; f++) {
			try {
				File file = new File(source);
				Scanner s = new Scanner(file);
				
				long total_runtime = 0;
				for (int i = 0; i < length; i++) {
						double[][] data = new double[28][28];
						int[][] disp_data = new int[28][28];
						
						int label;
						int labels[] = new int[10];	
						
						long startTime = System.currentTimeMillis();
					
						String data_set = s.nextLine();
						label = Integer.parseInt(data_set.substring(0,1));
						labels[label] = 1;
						
						String parsed_data_set = "";
						for (int y = 1; y < data_set.length(); y++) {
							if (data_set.substring(y, y+1).equals(",")) parsed_data_set += " ";
							else parsed_data_set += data_set.substring(y, y+1);
						}

						Scanner s2 = new Scanner(parsed_data_set);
						for (int x = 0; x < 28; x++) {
							for (int y = 0; y < 28; y++) {
								int next = s2.nextInt();
								data[x][y] = (double) next;
								disp_data[x][y] = next;
							}
						}
						s2.close();
						
						cnn.run(data, labels, first);
						
						if (display) displayer.paint(disp_data, label);
						
						long runtime = System.currentTimeMillis() - startTime;
						total_runtime += runtime;
						double error = cnn.net_error();
						int prediction = cnn.prediction();
						System.out.println("Dataset#: " + i  + " Works: " + (prediction == label) + " Label: " + label + " Error: " + error + " Prediction: " +  (prediction == -1 ? "???" : prediction) + " Avg_Runtime: " + (total_runtime / (i + 1)));
						
						first = false;
						Thread.sleep(0);
					}	
				s.close(); 
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	}
}
