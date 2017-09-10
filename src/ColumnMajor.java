import mickel.app.image.project.ByteStream;
import mickel.app.image.project.Codec;
import mickel.app.image.project.Pixel;


/** Similar to the RawAlgorithm, but encodes the image in
 *  a column-major order.
 * 
 * @author Jeff C. Mickel
 */
public class ColumnMajor implements Codec {
	
	@Override
	public ByteStream pixelsToBytes(Pixel[][] pixels) {
		int height = pixels.length;
		int width = pixels[0].length;
		
		ByteStream bytes = new ByteStream();
		bytes.appendInt(height);
		bytes.appendInt(width);
				
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				bytes.appendPixel(pixels[y][x]);
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
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				pixels[y][x] = bytes.nextPixel();
			}
		}
		
		return pixels;
	}
	
	
	@Override
	public String getName() { return "Column-Major Order"; }
	
	
	@Override
	public String getExtension() { return "cmo"; }
	
}
