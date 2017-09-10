import mickel.app.image.project.ByteStream;
import mickel.app.image.project.Codec;
import mickel.app.image.project.Pixel;
//import java.util.*;
import mickel.app.image.view.ImageCodecApp;


public class ABB  implements Codec 
{
	final int ERROR = 4;
	
	@Override
	public ByteStream pixelsToBytes(Pixel[][] pixels) 
	{
		int width = pixels[0].length;
		int height = pixels.length;
		
		ByteStream bytes = new ByteStream();
		bytes.appendInt(width);
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
		return bytes;
	}
	
	
	@Override
	public Pixel[][] bytesToPixels(ByteStream bytes) 
	{
		if (bytes.size() < 4) 
		{
			throw new IllegalArgumentException();
		}
		
		
		int width = bytes.nextInt();
		int height = bytes.nextInt();
				
		Pixel[][] pixels = new Pixel[height][width];
		
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
		
		
		
		return pixels;
	}
	
	
	@Override
	public String getName() { return "ABB"; }
	
	
	@Override
	public String getExtension() { return "ABB"; }

}
