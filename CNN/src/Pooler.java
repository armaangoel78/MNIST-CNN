
public class Pooler {

	public double pool(double[][] filtered_input, double[][] maxes, int a, int b) {
		double max = 0;
		for (int i = 0; i < filtered_input.length; i++) {
			for (int x = 0; x < filtered_input[i].length; x++) {
				if (filtered_input[i][x] > max) max = filtered_input[i][x];
			}
		}
		
		for (int i = 0; i < filtered_input.length; i++) {
			for (int x = 0; x < filtered_input.length; x++) {
				if (filtered_input[i][x] == max) maxes[a + i][b + x] = 1;
				else maxes[a + i][b + x] = 0;
 			}
		}
		
		return max;
	}
}
