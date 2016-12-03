import java.awt.Graphics;
import java.util.Scanner;

import javax.swing.JApplet;

public class Main extends JApplet{
	private final boolean prompt = true;
	private final boolean display = false;
	private int data_size = 50000;
	
	private Image_Reader reader = new Image_Reader();
	private Displayer displayer;
	private MNIST_Parser parser = new MNIST_Parser();
	private CNN_Driver cnn = new CNN_Driver();
	
	public void init() {
		
		this.resize(20*28, 20*28);
	}
	
	public void paint(Graphics g) {
		if (prompt) {
			Scanner s = new Scanner(System.in);
			Debug.sngl("Data Size: ");
			data_size = s.nextInt();
		}
		
		displayer = new Displayer(this, g);
		
		parser.parse_training_data("C:\\Users\\sunja\\Downloads\\MNIST_Data_Set\\CSV_Version\\training.csv",
			data_size, displayer, cnn, display);
	}
}
