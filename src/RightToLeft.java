import mickel.app.image.project.ByteStream;
import mickel.app.image.project.Codec;
import mickel.app.image.project.Pixel;


/** Similar to the RawAlgorithm, but encodes the image in
 *  a right-to-left, row-major order.
 * 
 * @author Jeff C. Mickel
 */
public class RightToLeft implements Codec {
	
	@Override
	public ByteStream pixelsToBytes(Pixel[][] pixels) {
		int height = pixels.length;
		int width = pixels[0].length;
		
		ByteStream bytes = new ByteStream();
		bytes.appendInt(height);
		bytes.appendInt(width);
				
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				bytes.appendPixel(pixels[y][width - x - 1]);
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
		
		if (bytes.available() < height * width * 3) { throw new IllegalArgumentException(); }
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				pixels[y][width - x - 1] = bytes.nextPixel();
			}
		}
		
		return pixels;
	}
	
	
	@Override
	public String getName() { return "Right-to-Left Row-Major"; }
	
	
	@Override
	public String getExtension() { return "rtl"; }

}
