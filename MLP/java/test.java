import java.io.*;

class test{
	public static void main(String a[]){
		try{
		 
			String prg = "import sys\nprint(int(sys.argv[1])+int(sys.argv[2]))\n";
			BufferedWriter out = new BufferedWriter(new FileWriter("test2.py"));
			out.write(prg);
			out.close();
			int number1 = 10;
			int number2 = 32;
			 
			ProcessBuilder pb = new ProcessBuilder("python","test2.py",""+number1,""+number2);
			Process p = pb.start();
			 
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			int ret = new Integer(in.readLine()).intValue();
			System.out.println("value is : "+ret);
		}catch(Exception e){System.out.println(e);}
	}
}
/*
import java.io.BufferedReader;
import java.io.InputStreamReader;
publicclass Test5 {
publicstaticvoid main(String args){
	try{
		System.out.println("start");
		Process pr = Runtime.getRuntime.exec("python test.py");
		BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream));
		String line;
		while ((line = in.readLine) != null) {
			System.out.println(line);
		}
		in.close;
		pr.waitFor;
		"end");
	} catch (Exception e){
		e.printStackTrace;
	}
}
*/