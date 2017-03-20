package tw.com.linearRegression.CSVReader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class CSVReader {
	
	private ArrayList<String> path;
	private int timeInterval;
	private BufferedReader br = null;
	private static final String splitBy = ",";
	private String readLine = "";
	private ArrayList<InputResource> resultData = null;
	
	public CSVReader(ArrayList<String> p)
	{
		//CSV path
		path = p;
	}
	
	public ArrayList<InputResource> parse()
	{
		resultData = new ArrayList<InputResource>();
		String[] rl= null;
		float var = 0;
		float elcd = 0;
		float dmax = 0;
		float emax = 0;
		float dmin = 10000;
		float emin = 10000;
		try {
			for(int i = 0; i < path.size(); i ++)
			{
				int num = 0;
				InputResource inputR = new InputResource(path.get(i));
				br = new BufferedReader(new FileReader(path.get(i)));
				while((readLine = br.readLine()) != null)
				{
					rl = readLine.split(splitBy);
					System.out.println(readLine);
					var = Float.valueOf(rl[1]);
					inputR.addData(var);
					elcd = Float.valueOf(rl[2]);
					inputR.addElcData(elcd);
					if(var > dmax)
					{
						inputR.setMaxData(var);
						dmax = var;
					}
					if(elcd > emax)
					{
						inputR.setMaxElcData(elcd);
						emax = elcd;
					}
					if(var < dmin)
					{
						inputR.setMinData(var);
						dmin = var;
					}
					if(elcd < emax)
					{
						inputR.setMinElcData(elcd);
						emin = elcd;
					}
					num++;
				}
				inputR.setDataNum(num);
				resultData.add(inputR);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			if(br != null)
			{
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return resultData;
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
