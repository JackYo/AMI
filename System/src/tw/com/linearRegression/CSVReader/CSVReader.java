package tw.com.linearRegression.CSVReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import tw.com.linearRegression.CBLResult.ResultInfo;

public class CSVReader {
	
	private ArrayList<File> path;
	private int timeInterval;
	private BufferedReader br = null;
	private static final String splitBy = ",";
	private String readLine = "";
	private ArrayList<InputResource> resultData = null;
	private String resultPath;
	
	public CSVReader(ArrayList<File> p)
	{
		//CSV path
		path = p;
	}
	
	public CSVReader(String p)
	{
		resultPath = p;
	}
	
	public ArrayList<InputResource> parse()
	{
		resultData = new ArrayList<InputResource>();
		String[] rl= null;
		
		try {
			for(int i = 0; i < path.size(); i ++)
			{
				float var = 0;
				float elcd = 0;
				float dmax = 0;
				float emax = 0;
				float dmin = 10000;
				float emin = 10000;
				int num = 0;
				InputResource inputR = new InputResource(path.get(i).getAbsolutePath());
				br = new BufferedReader(new FileReader(path.get(i).getAbsolutePath()));
				while((readLine = br.readLine()) != null)
				{
					rl = readLine.split(splitBy);
					System.out.println(readLine);
					inputR.addTime(rl[0]);
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
	
	public ArrayList<ResultInfo> resultParse()
	{
		ArrayList<ResultInfo> result = new ArrayList<ResultInfo>();
		String[] rl= null;
		try {
			br = new BufferedReader(new FileReader(resultPath));
			
			readLine = br.readLine();//ignore column title
			//br.readLine();
			while(readLine != null && !readLine.equals(""))
			{
				readLine = br.readLine();
				ResultInfo ri = new ResultInfo();
				rl = readLine.split(splitBy);
				System.out.println(readLine);
				ri.setDate(rl[0]);
				ri.setPredic_elc(Double.parseDouble(rl[1]));
				ri.setTrainScore(Double.parseDouble(rl[3]));
				ri.setPredicScore(Double.parseDouble(rl[4]));
				result.add(ri);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}finally {
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
		return result;
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
