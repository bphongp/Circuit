package homework6;
/*This program will record the status of a tuning circuit for a frequency range
 using random access file. Capacitor increment by 15pF. It will read all the 
 variables, compute value of L, compute the value of new frequency for a new
 capacitor and store it into random access. Then compare the frequency max
 to 16.7MHz and change the value of L and store it*/
import java.io.*;
import java.lang.Math;

public class CircuitHW
{
	public static void main(String[] args) throws java.io.IOException
	{
		String cminStr, cmaxStr, fStr;
		double f, c, l, cMin, cMax,numeratorL;
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);//for reading what user enters
		
		String filename = "raf1.txt";
		RandomAccessFile fileRandom = new RandomAccessFile(filename, "rw");//write
		RandomAccessFile rfm = new RandomAccessFile(filename, "rw");
		RandomAccessFile rfm1 = new RandomAccessFile(filename, "rw");
		PrintWriter writer = new PrintWriter(filename);//not necessary but want to erase contents of file
		writer.print("");
		writer.close();
		FileInputStream fis = new FileInputStream(filename);
		BufferedInputStream bis = new BufferedInputStream(fis);
		DataInputStream dis = new DataInputStream(bis);
		
		Double[] data = new Double[56];

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
		
		numeratorL = Math.pow(((2*Math.PI)/f), 2);//calculate numerator of L eqn
		c = Math.sqrt(cMin*cMax);//calculate c given cmax&cmin
		l = numeratorL/c;//calculate l
		System.out.println("L: "+l);
		System.out.println("C: "+c+" pF");
		System.out.println("f: "+f+" MHz");
		fileRandom.writeDouble(l);
		fileRandom.writeDouble(c);
		while(c<cMax)
		{
			System.out.println("Enter new value for C");
			cminStr = br.readLine();
			c = Double.parseDouble(cminStr);
			fileRandom.writeDouble(c);
			
			f=((2*Math.PI)/(Math.sqrt(l*c)));//finds value of f
			fileRandom.writeDouble(f);//write new f to file
			System.out.println("New f: "+f+" MHz");
			System.out.println("New C: " +c+ " pF");
			if (f>16.7)//if statement if fmax>typical f
			{
				int i = 0;
				rfm.seek(0);//set pointer at 0
				try//need try because need to catch EOFException
				{
					while (dis.available()>0)//as long as file has bytes to read
					{
						i = i+1;
						rfm.seek(i);//will change pointer pos. throughout loop
						if(f == rfm.readDouble())//finds the instants of f
						{
							l = l*1.02;
							System.out.println("L :" +l);
							fileRandom.writeDouble(l);
						}//end if statement when f is read
					}//end while to find pointer of f location
				}//end try
				catch (EOFException ex)//catches eof exception
				{
					System.out.println("New L has been written to file");
				}//end catch
			}//end if statement f>16.7
		}//end while loop c<cMax
		
		System.out.println("Read all data from file");
		for (int k = 0; k<56; k++)//for loop to read file
		{
			data[k] = rfm1.readDouble();
			System.out.println(data[k]);
		}//end for loop to read file
		fileRandom.close();
		rfm.close();
		rfm1.close();
		dis.close();
	}//end main
}//end class
