package main;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class LSBSteganography implements CryptMachine {

	private final int BYTE_PARTS = 8;

	@Override
	public boolean encrypt(File file, String message) {
		Image sourceImage;
		try {
			// ��������� �������� �����������
			sourceImage = ImageIO.read(file);
			int imageWidth = sourceImage.getWidth(null);
			int imageHeight = sourceImage.getHeight(null);
			
			// ������� ������ ��������
			int[] pixels = new int[imageWidth * imageHeight];

			// ������� �������
			pixels = ImageHelper.getImagePixels(sourceImage);

			long imageLength = imageWidth * imageHeight;

			byte[] messageBytes = message.getBytes();

			long messageLength = messageBytes.length;

			// ���� ����� �������� ������ ����
			if (messageLength > imageLength / BYTE_PARTS) {
				System.out.println("Length of message is longer than image! image:" + imageLength + ", message:" + messageLength);
				return false;
			} else {

				// ����� ���������� ��������� �������� � ��������
				for (int i = 0; i < messageLength; i++) {
					byte mByte = messageBytes[i];
					byte[] mBits = splitByteToBits(mByte);
					for (int j = 0; j < BYTE_PARTS; j++) {
						int pixel = pixels[i * BYTE_PARTS + j];
						pixel >>>= 1;
						pixel <<= 1;
						pixels[i*BYTE_PARTS+j] = pixel | mBits[j];
					}
				}

				// �������� ����������������� ������� ������� � Image
				MemoryImageSource mis = new MemoryImageSource(imageWidth, imageHeight, pixels, 0, imageWidth);
				Image outImage = Toolkit.getDefaultToolkit().createImage(mis);

				// ��������� ��� �� ����
				BufferedImage bi = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
				Graphics2D g2 = bi.createGraphics();
				g2.drawImage(outImage, 0, 0, null);
				ImageIO.write(bi, "BMP", new File(file.getAbsolutePath() + ".lsb"));
				g2.dispose();

			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	// ������� ��������� ���� �� ��������� ����, ������ �� ������� �������� � byte
	private byte[] splitByteToBits(byte b) {
		byte sourceByte = b;
		byte[] result = new byte[BYTE_PARTS];
		for (int i = 0; i < BYTE_PARTS; i++) {
			result[i] = Integer.valueOf(sourceByte & 0x01).byteValue();
			sourceByte >>>= 1;
		}
		System.out.println(Arrays.toString(result));
		return result;
	}

	@Override
	public boolean decrypt(File stegFile, File origin) {
		try {

			// ��������� �����������
			Image cryptedImage = ImageIO.read(stegFile);
			Image originImage = ImageIO.read(origin);

			// ������������ �� �� �������
			int[] stegPixels = ImageHelper.getImagePixels(cryptedImage);
			int[] originPixels = ImageHelper.getImagePixels(originImage);

			// ���� ����� �������� �� ���������
			if (stegPixels.length != originPixels.length) {
				System.out.println("Different images!");
				return false;
			} else {

				// �����, ������� ���������� � ������������
				ArrayList<Integer> messageBytes = new ArrayList<Integer>();

				int lastDiff=0;//������ ���������� ������������� �������
				for (int i = 0; i < stegPixels.length; i++) {
					if (stegPixels[i] != originPixels[i]) {
						lastDiff=i;
					}
				}
				
				int qHiddenBits=lastDiff+1;// ���������� ������� ���
				
				// ���������� �� mod 8==0
				if(qHiddenBits%BYTE_PARTS!=0){
					qHiddenBits+=BYTE_PARTS-(qHiddenBits%BYTE_PARTS);
				}
				
				// ��������� ���������
				for(int i=0; i<qHiddenBits; i++){
					messageBytes.add(stegPixels[i]);
				}

				// �������� ���� �� ���������� ����
				int[] messageBits = new int[qHiddenBits];
				for (int i = 0; i < messageBits.length; i++) {
					messageBits[i] = messageBytes.get(i) & 0x01;
				}
				
				if(messageBits.length>0){
					System.out.println("Founded message: "+implode(messageBits));
				}else{
					System.out.println("Empty string was founded!");
				}			

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
	
	// ������� �������� ��������� ���� � ������
	private String implode(int[] bits){
		byte[] bytes=new byte[bits.length/BYTE_PARTS];
		for(int i=0; i<bits.length/BYTE_PARTS; i++){
			int b=0;
			for(int j=BYTE_PARTS-1; j>=0; j--){
				b|=bits[i*BYTE_PARTS+j]<<j;
			}
			bytes[i]=(byte)(b&0xff);
		}
		return new String(bytes);		
	}

}
