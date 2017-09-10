import mickel.app.image.project.ByteStream;
import mickel.app.image.project.Codec;
import mickel.app.image.project.Pixel;


/** Encodes the pixels of an image in column-major error while
 *  using run-length encoding to achieve compression.
 * 
 * @author Jeff C. Mickel
 */
public class PleaseIgnore implements Codec {
	
	@Override
	public ByteStream pixelsToBytes(Pixel[][] pixels) {
		ByteStream bytes = new ByteStream();
		
		int width = pixels[0].length;
		int height = pixels.length;
		
		bytes.appendInt(width);;
		bytes.appendInt(height);
		for (int y = 0; y < (height); y += 4)
		{
			for (int x = 0; x < (width); x += 4) 
			{
				int r = 0, g = 0, b = 0, count = 0;
				for (int i = 0; i < 4; i++)
				{
					for (int j = 0; j < 4; j++)
					{
						if (y + i >= height)
							continue;
						if (x + j >= width)
							continue;
						r += pixels[y + i][x + j].getRed();
						g += pixels[y + i][x + j].getGreen();
						b += pixels[y + i][x + j].getBlue();
						count++;
					}
				}
				bytes.appendPixel(new Pixel(r / count, g / count, b / count));
			}
		}
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
		int width = bytes.nextInt();
		int height = bytes.nextInt();
		
		Pixel[][] pixels = new Pixel[height][width];
		if (bytes.size() < 4) 
		{
			throw new IllegalArgumentException();
		}
		
		
		for (int y = 0; y < (height) / 4; y++) 
		{
			for (int x = 0; x < (width) / 4; x++) 
			{
				
				pixels[y * 4][x * 4] = bytes.nextPixel();
				
				for (int i = 0; i < 4; i++)
					for (int j = 0; j < 4; j++)
					{
						
						if (y * 4 + i >= height)
						{
							continue;
						}
		
						if (x * 4 + j >= width)
						{
							continue;
						}
						
						pixels[y * 4 + i][x * 4 + j] = pixels[y * 4][x * 4];
					}
			}
		}



		for (int j = 1; j <= 3; j++)
		for (int i = 0; i < height; i++)
			pixels[i][width - j] = pixels[i][width - 4];

		for (int j = 1; j <= 3; j++)
		for (int i = 0; i < width; i++)
			pixels[height - j][i] = pixels[height - 4][i];
		Pixel rgb  = bytes.nextPixel();
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
	public String getName() { return "PleaseIgnore"; }
	
	
	@Override
	public String getExtension() { return "PleaseIgnore"; }
	

}


