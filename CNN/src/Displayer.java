import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Displayer {
	private Main main;
	private Graphics g;
	
	public Displayer(Main main, Graphics g) {
		this.main = main;
		this.g = g;
	} 
	
	public void paint(BufferedImage image) {
		Graphics2D g2d = (Graphics2D) g;
		main.resize(image.getWidth(), image.getHeight());
		g2d.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
	}
	
	public void paint_img(int[][] data) {
		int cursorX = 0;
		int cursorY = 0;
		
		int pixelSize = 1;
		main.resize(data.length*pixelSize, data[0].length*pixelSize);
		for (int i = 0; i < data.length; i++) {
			for (int x = 0; x < data[i].length; x++) {
				g.setColor(new Color(data[i][x], data[i][x], data[i][x]));
				g.fillRect(cursorY, cursorX, pixelSize, pixelSize);
				
				cursorX += pixelSize;
			}
			cursorX = 0;
			cursorY += pixelSize;
		}
	}
	
	public void paint(int[][] data, int label) {
		int cursorX = 0;
		int cursorY = 0;
		
		int pixelSize = 20;
		main.resize(data.length*pixelSize, data[0].length*pixelSize);
		for (int i = 0; i < data.length; i++) {
			for (int x = 0; x < data[i].length; x++) {
				g.setColor(new Color(data[i][x], data[i][x], data[i][x]));
				g.fillRect(cursorX, cursorY, pixelSize, pixelSize);
				
				cursorX += pixelSize;
			}
			cursorX = 0;
			cursorY += pixelSize;
		}
		
		Font font = new Font("Arial", Font.BOLD, 50);

		g.setColor(Color.white);
		g.setFont(font);
		g.drawString(Integer.toString(label), pixelSize * data.length / 2, 50);
	}
	
	public void paint(int[][] data, int[][] data2, int label) {
		int cursorX = 0;
		int cursorY = 0;
		int defaultX = 0;
		
		int pixelSize = 20;
		main.resize(data.length*pixelSize*2, data[0].length*pixelSize);
		for (int i = 0; i < data.length; i++) {
			cursorX = defaultX;
			for (int x = 0; x < data[i].length * 2; x++) {
				if (x < data[i].length) {
					g.setColor(new Color(data[i][x], data[i][x], data[i][x]));
				} else {
					g.setColor(new Color(data2[i][x-data[i].length], data2[i][x-data[i].length], data2[i][x-data[i].length]));
				}
				g.fillRect(cursorX, cursorY, pixelSize, pixelSize);
				
				cursorX += pixelSize;
			}
			cursorY += pixelSize;
		}		
		
		Font font = new Font("Arial", Font.BOLD, 50);

		g.setColor(Color.white);
		g.setFont(font);
		g.drawString(Integer.toString(label), main.getWidth()/2, 50);
	}
}
