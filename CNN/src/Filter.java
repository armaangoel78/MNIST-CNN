import java.util.Scanner;

public class Filter {
	private static int size = 3;
	private double[][] filter = new double[size][size];
	
	public void adjust_filter(int a, int b, double adjustment) {
		filter[a][b] += adjustment;
	}
	
	public double getWeight(int a, int b) {
		return filter[a][b];
	}
	
	public static int getSize() {
		return size;
	}
	
	public void rand () {
		for (int i = 0; i < filter.length; i++) {
			for (int x = 0; x < filter[i].length; x++) {
				filter[i][x] = Math.random() * 1/9;
			}
		}
	}
	
	public void inputFilter() {
		Scanner s = new Scanner(System.in);
		Debug.sngl("Enter filter weights: ");
		for (int i = 0; i < filter.length; i++) {
			for (int x = 0; x < filter[i].length; x++){
				filter[i][x] = s.nextInt();
			}
 		}
	}
	
	public double applyFilter(double[][] image_section) {
		double sum = 0;
		for (int i = 0; i < image_section.length; i++) {
			for (int x = 0; x < image_section[i].length; x++) {
				sum += (image_section[i][x] * filter[i][x]);
			}
		}
		
		double output = sum/(image_section.length * image_section[0].length);
		return (output > 0 ? output : 0);
	}
	
	public double[][] getFilter() {
		return filter;
	}
	
	public void display(int filterNum, int layer) {
		System.out.println("Layer: " + layer + " filter#: " + filterNum);
		for (int i = 0; i < filter.length; i++) {
			for (int x = 0; x < filter[0].length; x++) {
				System.out.print(filter[i][x] + " ");
			}
			System.out.println("");
		}
	}
}
