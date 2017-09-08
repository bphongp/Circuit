package homework6;
/*This program will record the status of a tuning circuit for a frequency range
 using random access file. Capacitor increment by 15pF. It will read all the 
 variables, compute value of L, compute the value of new frequency for a new
 capacitor and store it into random access. Then compare the frequency max
 to 16.7MHz and change the value of L and store it*/
import java.io.*;
import java.lang.Math;

public class CircuitProgram
{
	
	private static double f, c, l, cMin, cMax, fmax;
	private static RandomAccessFile fileRandom;
	
	public static void main(String[] args) throws java.io.IOException
	{
		String cminStr, cmaxStr, fStr;
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);//for reading what user enters
		
		String filename = "raf.txt";
		fileRandom = new RandomAccessFile(filename, "rw");
	
		System.out.println("Please enter the following data: ");
		System.out.println("Cmin in pF: ");
		cminStr = br.readLine();
		cMin = Double.parseDouble(cminStr);
		fileRandom.writeDouble(cMin);
		System.out.println("Cmax in pF: ");
		cmaxStr = br.readLine();
		cMax = Double.parseDouble(cmaxStr);
		fileRandom.writeDouble(cMax);
		System.out.println("f in MHz: ");
		fStr = br.readLine();
		f = Double.parseDouble(fStr);
		fileRandom.writeDouble(f);
		
		calculateL();
		
		RandomAccessFile rfm = new RandomAccessFile(filename, "rw");
		while(fmax<16.7)
		{
			System.out.println("Enter new value for Cmin");
			cminStr = br.readLine();
			cMin = Double.parseDouble(cminStr);
			fileRandom.writeDouble(cMin);
			
			System.out.println("Enter new value for Cmax");
			cmaxStr = br.readLine();
			cMax = Double.parseDouble(cmaxStr);
			fileRandom.writeDouble(cMax);
			
			c = Math.sqrt(cMin*cMax);
			f=((2*Math.PI)/(Math.sqrt(l*c)));
			fileRandom.writeDouble(c);
			fileRandom.writeDouble(f);
			System.out.println("f "+f);
			System.out.println("c " +c);
			
			fmax = ((2*Math.PI)/(Math.sqrt(l*cMax)));
			fileRandom.writeDouble(fmax);
			System.out.println("fmax "+fmax);
		}//end while loop fmax<16.5
		int i = 0;
		rfm.seek(0);//set pointer at 0
		while (i<=rfm.length())
		{
			i = i+8;
			rfm.seek(i);
			if(fmax == rfm.readDouble() && fmax>16.7)
			{
				System.out.println("fmax :"+rfm.readDouble()+" MHz");
				int j = 0;
				fileRandom.seek(0);
				while(j<=fileRandom.length())
				{
					fileRandom.seek(j+8);
					if(l == fileRandom.readDouble())
					{
						l = l*1.02;
						System.out.println(fileRandom.getFilePointer());
						fileRandom.writeDouble(l);
						fileRandom.readDouble();
					}//end if for l
				}//end while loop to seek pos of l
			}//end if statement for finding fmax
		}//end while to seek the fmax
		fileRandom.close();
		rfm.close();
		
	}
	private static void calculateL() throws IOException
	{
		double numeratorL = Math.pow(((2*Math.PI)/f), 2);
		c = Math.sqrt(cMin*cMax);
		l = numeratorL/c;
		System.out.println("l "+l +"  C"+c);
		fileRandom.writeDouble(l);
	}//end calculateL method
}
