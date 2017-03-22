package tw.com.linearRegression.drawGraph;

import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.Shape;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.FastScatterPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.ShapeUtilities;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

import tw.com.linearRegression.CSVReader.InputResource;

public class Graph extends ApplicationFrame{
	private InputResource input;
	private NumberAxis domainAxis = new NumberAxis("X");
	private NumberAxis electricAxis = new NumberAxis("electric");
	//private FastScatterPlot plot = null;
	private float[][] data ;
	private final JFreeChart chart;
	private double lineSlope = 0;
	private double lineIntercept = 0;
	
	public Graph(String title , InputResource i , double slope , double intercept) {
		// TODO Auto-generated constructor stub
		super(title);
		input = i;
		domainAxis.setAutoRangeIncludesZero(false);
		electricAxis.setAutoRangeIncludesZero(false);
		lineSlope = slope;
		lineIntercept = intercept;
		chart = ChartFactory.createXYLineChart(title, "X", "Y", dataTransfer() , PlotOrientation.VERTICAL 
				, true , false , false);
	}
	
	public void drawGraph()
	{
		XYPlot xyPlot = (XYPlot)chart.getPlot();
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesLinesVisible(0, false);
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesLinesVisible(1, true);
        renderer.setSeriesShapesVisible(1, false);   
        xyPlot.setRenderer(renderer);
        final ChartPanel panel = new ChartPanel(chart, true);
		panel.setPreferredSize(new java.awt.Dimension(500, 270));
		panel.setMinimumDrawHeight(10);
		panel.setMaximumDrawHeight(2000);
		panel.setMinimumDrawWidth(20);
		panel.setMaximumDrawWidth(2000);
		setContentPane(panel);
		pack();
		RefineryUtilities.centerFrameOnScreen(this);
		setVisible(true);
	}
	
	/*public void drawGraph()
	{
		dataTransfer();
		Shape cross = ShapeUtilities.createDiagonalCross(3, 1);
		
		XYPlot xyPlot = (XYPlot)chart.getPlot();
		xyPlot.setDomainCrosshairVisible(true);
        xyPlot.setRangeCrosshairVisible(true);
		XYItemRenderer renderer = xyPlot.getRenderer();
		renderer.setSeriesShape(0, cross);
		renderer.setSeriesPaint(0, Color.red);
		
		chart.getRenderingHints().put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		final ChartPanel panel = new ChartPanel(chart, true);
		panel.setPreferredSize(new java.awt.Dimension(500, 270));
		panel.setMinimumDrawHeight(10);
		panel.setMaximumDrawHeight(2000);
		panel.setMinimumDrawWidth(20);
		panel.setMaximumDrawWidth(2000);
    

		setContentPane(panel);
		pack();
		RefineryUtilities.centerFrameOnScreen(this);
		setVisible(true);
	}*/
	
	private XYDataset dataTransfer()
	{
		XYSeriesCollection dataSet = new XYSeriesCollection();
		XYSeries dataPoints = new XYSeries("dataPoints");
		for(int i = 0; i < input.getDataNum(); i++)
		{
			float x = input.getDatas().get(i);
			float y = input.getElcDatas().get(i);
			dataPoints.add(x , y);
		}
		
		XYSeries line = new XYSeries("Regressionline");
		//calculate line point
		//y = intercept + slope * x
		float ymax , ymin;
		double datamax = input.getMaxData();
		double datamin = input.getMinData();
		//y = (float) lineIntercept;
		//lineSlope = 0.5;
		ymin = (float) (lineIntercept + lineSlope * (datamin));
		ymax = (float) (lineIntercept + lineSlope * (datamax));
		//line.add(0 , y);
		line.add(datamin , ymin);
		line.add(datamax , ymax);
				
		dataSet.addSeries(dataPoints);
		dataSet.addSeries(line);
        return dataSet;
	}
	
	
	


	
	
	
	
	

}
