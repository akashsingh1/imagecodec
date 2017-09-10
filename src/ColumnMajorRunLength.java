import mickel.app.image.project.ByteStream;
import mickel.app.image.project.Codec;
import mickel.app.image.project.Pixel;


/** Encodes the pixels of an image in column-major error while
 *  using run-length encoding to achieve compression.
 * 
 * @author Jeff C. Mickel
 */
public class ColumnMajorRunLength implements Codec {
	
	@Override
	public ByteStream pixelsToBytes(Pixel[][] pixels) {
		int height = pixels.length;
		int width = pixels[0].length;
		
		ByteStream bytes = new ByteStream();
		bytes.appendInt(height);
		bytes.appendInt(width);;
		
		Pixel[] all = new Pixel[width * height];
		for (int c = 0; c < width; c++) {
			for (int r = 0; r < height; r++) {
				all[c * height + r] = pixels[r][c];
			}
		}
		
		Pixel rgb = all[0];
		bytes.appendPixel(rgb);
		int run = 1;
		for (int i = 1; i < all.length; i++) {
			if (run < 255 && all[i].equals(rgb)) {
				run++;
			}
			else {
				bytes.appendByte(run);
				run = 1;
				rgb = all[i];
				bytes.appendPixel(rgb);
			}
		}
		bytes.appendByte(run);
		
		return bytes;
	}
	
	
	@Override
	public Pixel[][] bytesToPixels(ByteStream bytes) {		
		if (bytes.available() < 8) { throw new IllegalArgumentException(); }
		int height = bytes.nextInt();
		int width = bytes.nextInt();
		
		Pixel[][] pixels = new Pixel[height][width];
		
		if (bytes.available() % 4 != 0) { throw new IllegalArgumentException(); }
		Pixel rgb = bytes.nextPixel();
		int run = bytes.nextByte();
		for (int c = 0; c < width; c++) {
			for (int r = 0; r < height; r++) {
				if (run == 0 && bytes.hasNextPixel()) {
					rgb = bytes.nextPixel();
					run = bytes.nextByte();
				}
				pixels[r][c] = rgb;
				run--;
			}
		}
		
		return pixels;
	}
	
	
	@Override
	public String getName() { return "ColMajorRunLength"; }
	
	
	@Override
	public String getExtension() { return "cmrl"; }
	

}
