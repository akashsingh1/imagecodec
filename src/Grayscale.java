import mickel.app.image.project.ByteStream;
import mickel.app.image.project.Codec;
import mickel.app.image.project.Pixel;


public class Grayscale implements Codec {
	
	@Override
	public ByteStream pixelsToBytes(Pixel[][] pixels) {
		int height = pixels.length;
		int width = pixels[0].length;
		
		ByteStream bytes = new ByteStream();
		bytes.appendInt(height);
		bytes.appendInt(width);
		
		for (int r = 0; r < height; r++) {
			for (int c = 0; c < width; c++) {
				Pixel p = pixels[r][c];

				long gray = Math.round(
						0.21 * p.getRed() + 
						0.72 * p.getGreen() + 
						0.07 * p.getBlue());
				
				bytes.appendByte((int)gray);
			}
		}
		return bytes;
	}
	
	
	@Override
	public Pixel[][] bytesToPixels(ByteStream bytes) {
		if (bytes.available() < 8) { throw new IllegalArgumentException(); }
		int height = bytes.nextInt();
		int width = bytes.nextInt();
		
		Pixel[][] pixels = new Pixel[height][width];
		
		if (bytes.available() < height * width) { throw new IllegalArgumentException(); }
		for (int r = 0; r < height; r++) {
			for (int c = 0; c < width; c++) {
				int gray = bytes.nextByte();
				pixels[r][c] = new Pixel(gray, gray, gray);
			}
		}
		
		return pixels;
	}
	
	
	@Override
	public String getName() { return "Grayscale"; }
	
	
	@Override
	public String getExtension() { return "gray"; }

}
