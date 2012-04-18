package main;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class AdditiveSteganography implements CryptMachine {
	
	@Override
	public boolean encrypt(File file, String message) {
		
		try {
			
			Image sourceImage=ImageIO.read(file);
			
			int imageWidth=sourceImage.getWidth(null);
			int imageHeight=sourceImage.getHeight(null);

			int[] pixels=new int[imageWidth*imageHeight];
			
			pixels=ImageHelper.getImagePixels(sourceImage);
			
			long imageLength=imageWidth*imageHeight;
			
			byte[] messageBytes=message.getBytes();
			
			long messageLength=messageBytes.length;
			
			if(messageLength>imageLength){
				System.out.println("Length of message is longer than image! image:"+imageLength+", message:"+messageLength);
				return false;
			}else{
				
				// считаем шаг, через который будут вписаны байты сообщения
				int step=(int)Math.floor(imageLength/messageLength);
				
				// вписываем байты ссобщения
				for(int i=0; i<messageLength; i++){

					byte messageUnit=messageBytes[i];
					
					pixels[step*i]+=messageUnit;					
				}

				// сохраняем картинку на диск
				MemoryImageSource mis=new MemoryImageSource(imageWidth, imageHeight, pixels, 0, imageWidth);
				Image outImage=Toolkit.getDefaultToolkit().createImage(mis);
				
				BufferedImage bi=new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
				Graphics2D g2=bi.createGraphics();
				g2.drawImage(outImage, 0, 0, null);		
				ImageIO.write(bi, "BMP", new File(file.getAbsolutePath()+".add"));
				g2.dispose();
				return true;
				
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.out.println("Cannot grab pixels of specified image!");
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Eror saving image to disk!");
			return false;
		}
	}

	@Override
	public boolean decrypt(File stegFile, File origin) {
		
		try {
			// считываем файлы
			Image cryptedImage=ImageIO.read(stegFile);
			Image originImage=ImageIO.read(origin);
			
			// граббим пикселы
			int[] stegPixels=ImageHelper.getImagePixels(cryptedImage);
			int[] originPixels=ImageHelper.getImagePixels(originImage);
			
			// если количество пикселов разное - ошибка
			if(stegPixels.length!=originPixels.length){
				System.out.println("Different images!");
				return false;
			}else{
				// отличающиеся байты
				ArrayList<Byte> messageBytes=new ArrayList<Byte>();
				// находим все отличия
				for(int i=0; i<stegPixels.length; i++){
					if(stegPixels[i]!=originPixels[i]){
						int diff=stegPixels[i]-originPixels[i];
						messageBytes.add(Integer.valueOf(diff).byteValue());
					}
				}
				
				byte[] bytes=new byte[messageBytes.size()];
				for(int i=0; i<bytes.length; i++){
					bytes[i]=messageBytes.get(i);
				}
				
				String message=null;
				// создаем строку из найденных отличий
				if(bytes.length>0){
					message=new String(bytes);
				}else{
					message="An empty string was founded in file!";
				}
				
				System.out.println(message);
				return true;
				
			}
		
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
	
	}
	

}
