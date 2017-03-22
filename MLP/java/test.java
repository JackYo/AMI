import java.io.*;

class test{
	public static void main(String a[]){
		try{
		 
			/*String prg = "import sys\nprint(int(sys.argv[1])+int(sys.argv[2]))\n";
			BufferedWriter out = new BufferedWriter(new FileWriter("test2.py"));
			out.write(prg);
			out.close();*/
			int number1 = 10;
			int number2 = 32;
			 
			//ProcessBuilder pb = new ProcessBuilder("python","test.py",""+number1,""+number2);
			//ProcessBuilder pb = new ProcessBuilder("python","mlp_test.py");
			ProcessBuilder pb = new ProcessBuilder("python","../python/mlp_test.py");
			Process p = pb.start();
			 
			String line;
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((line = in.readLine()) != null) {
				
				System.out.println(line);
			}
			System.out.println("\npython finished");
		}catch(Exception e){System.out.println(e);}
	}
}
