import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class Image_Reader{
	BufferedImage draw_image;
	boolean image_exists = false;
	
	
	public BufferedImage read_image(String source) {
		File file = new File(source);
		try {
			BufferedImage image = ImageIO.read(file);
			return image;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int[][][] read_image_data(BufferedImage image) {
		int[][][] image_data = new int[image.getWidth()][image.getHeight()][3];
		for (int i = 0; i < image_data.length; i++) {
			for (int x = 0; x < image_data[i].length; x++) {
				int clr=  image.getRGB(i, x); 
				int red = (clr & 0x00ff0000) >> 16;
				int green = (clr & 0x0000ff00) >> 8;
				int blue =  clr & 0x000000ff;
				image_data[i][x] = new int[]{red, green, blue};
			}
		}
		return image_data;
	}
	public int[][] greyscale(int[][][] image_data) {
		int[][] greyscaled = new int[image_data.length][image_data[0].length];
		for (int i = 0; i < image_data.length; i++) {
			for (int x = 0; x < image_data[i].length; x++) {
				greyscaled[i][x] = (image_data[i][x][0] + image_data[i][x][1] + image_data[i][x][2])/3;
			}
		}
		return greyscaled;
	}
	
	public BufferedImage new_image(BufferedImage og_image) {
		int[][][] data = read_image_data(og_image);
		BufferedImage new_image = og_image;
		for (int i = 0; i < og_image.getWidth(); i++) {
			for (int x = 0; x < og_image.getHeight(); x++) {
				int red = (data[i][x][0] << 16) & 0x00FF0000; 
			    int green = (data[i][x][1] << 8) & 0x0000FF00; 
			    int blue = data[i][x][2] & 0x000000FF;

			    int average = (red + blue + green) / 3;
			    
			    red = average;
			    blue = average;
			    green = average;
			    
			    int clr = (0xFF000000 | red | green | blue);
				new_image.setRGB(i, x, clr);
			}
		}
		return new_image;
	}
	
	
}
