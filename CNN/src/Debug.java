
public class Debug {
	public static void sngl() {
		System.out.println("test");
	}
	public static void sngl(String s) {
		System.out.println(s);
	}
	public static void sngl(int i) {
		System.out.println(i);
	}
	public static void arr() {
		System.out.println("");
	}
	public static void arr(int i) {
		System.out.print(i + " ");
	}
	public static void list(int[] i) {
		for (int x = 0; x < i.length; x++){
			System.out.print(i[x]);
		}
		System.out.println("");
	}
	public static void list(int[][] arr) {
		for (int i = 0; i < arr.length; i++) {
			for (int x = 0; x < arr[i].length; x++) {
				System.out.print(arr[i][x] + " ");
			}
			System.out.println("");
		}
		System.out.println("");
	}
	
}
