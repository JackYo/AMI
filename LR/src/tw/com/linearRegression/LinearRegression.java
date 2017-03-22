package tw.com.linearRegression;

import java.util.ArrayList;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import tw.com.linearRegression.CSVReader.CSVReader;
import tw.com.linearRegression.CSVReader.InputResource;
import tw.com.linearRegression.drawGraph.Graph;

public class LinearRegression {
	
	private SimpleRegression regression = new SimpleRegression();
	private double RSquare;
	private double intercept;
	private double slope;
	public double getRSquare() {
		return RSquare;
	}

	public void setRSquare(double rSquare) {
		RSquare = rSquare;
	}

	public double getIntercept() {
		return intercept;
	}

	public void setIntercept(double intercept) {
		this.intercept = intercept;
	}

	public double getSlope() {
		return slope;
	}

	public void setSlope(double slope) {
		this.slope = slope;
	}

	public double getSlopeStdErr() {
		return slopeStdErr;
	}

	public void setSlopeStdErr(double slopeStdErr) {
		this.slopeStdErr = slopeStdErr;
	}

	private double slopeStdErr;
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<String> paths = new ArrayList<String>();
		paths.add("D:\\AMI\\ElectricCBLEstimate\\LR\\temp.csv");
		CSVReader reader = new CSVReader(paths);
		ArrayList<InputResource> resources = reader.parse();
		
		LinearRegression LR = new LinearRegression();
		LR.regressionAnalysis(resources.get(0));
		
		Graph drawGraph = new Graph("temperature" , resources.get(0) , LR.getSlope() , LR.getIntercept());
		drawGraph.drawGraph();
	}	
	
	public void regressionAnalysis(InputResource input)
	{
		for(int i = 0; i < input.getDataNum(); i++)
		{
			System.out.println(input.getDatas().get(i) + " , " +  input.getElcDatas().get(i));
			regression.addData(input.getDatas().get(i) , input.getElcDatas().get(i));
		}
		
		System.out.println(regression.getR());
		
		System.out.println(RSquare = regression.getRSquare());
		
		//y = intercept + slope * x
		System.out.println(intercept = regression.getIntercept());
		// displays intercept of regression line

		System.out.println(slope = regression.getSlope());
		// displays slope of regression line

		System.out.println(slopeStdErr = regression.getSlopeStdErr());
		// displays slope standard error
		
		System.out.println(regression.predict(25));
		
	}

}
