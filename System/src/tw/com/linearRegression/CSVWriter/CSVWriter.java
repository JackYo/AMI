package tw.com.linearRegression.CSVWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import tw.com.linearRegression.CSVReader.InputResource;

public class CSVWriter {

	private ArrayList<InputResource> resultData = null;
	
	
	public CSVWriter(ArrayList<InputResource> r)
	{
		resultData = r;
	}
	
	public void write() throws FileNotFoundException, UnsupportedEncodingException
	{
		File rawDataFile = new File("rawData.csv");
		File answerDataFile = new File("answerData.csv");
		PrintWriter writer = new PrintWriter(rawDataFile , "UTF-8");
		PrintWriter ansWriter = new PrintWriter(answerDataFile , "UTF-8");
		int featureNum = resultData.size();
		int commaNum = featureNum + 1;
		int dataRow = resultData.get(0).getDataNum();
		System.out.println(dataRow + "," + featureNum);
		writer.print(dataRow + "," + featureNum);
		for(int i = 1; i <= commaNum; i++)
		{
			writer.print(",");
		}
		writer.print("\n");
		writer.print("Time" + ",");
		for(int i = 0 ;i < resultData.size(); i++)
		{
			writer.print(resultData.get(i).getDataName() + ",");
		}
		writer.print("electricity\n");
		
		for(int i = 0; i < dataRow; i++)
		{
			writer.print(resultData.get(0).getTime().get(i) + ",");
			for(int j = 0; j < featureNum; j++)
			{
				writer.print(resultData.get(j).getDatas().get(i) + ",");
			}
			writer.print(resultData.get(0).getElcDatas().get(i) + "\n");
			if(i != 0){
				ansWriter.println(resultData.get(0).getElcDatas().get(i));
			}
		}
		writer.close();
		ansWriter.close();
		
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
