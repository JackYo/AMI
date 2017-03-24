package tw.com.linearRegression.drawGraph;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Minute;
import org.jfree.data.time.SimpleTimePeriod;
import org.jfree.data.time.TimePeriodValues;
import org.jfree.data.time.TimePeriodValuesCollection;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import tw.com.linearRegression.CBLResult.ResultInfo;

public class DrawCBL extends ApplicationFrame{
	
	private String time;
	private ArrayList<ResultInfo> result;
	private JFreeChart chart ;
	private XYDataset dataset = null;
	private final DateAxis domainAxis = new DateAxis("Date");
	private final NumberAxis electricAxis = new NumberAxis("electric");
	
	public DrawCBL(String t , ArrayList<ResultInfo> r)
	{
		super("Result");
		time = t;
		result = r;
		dataset = createDataset();
	}
	
	public void draw()
	{
		
        domainAxis.setVerticalTickLabels(true);
        domainAxis.setTickUnit(new DateTickUnit(DateTickUnit.HOUR, 1));
        domainAxis.setDateFormatOverride(new SimpleDateFormat("hh:mm"));
        domainAxis.setLowerMargin(0.01);
        domainAxis.setUpperMargin(0.01);
        
        StandardXYItemRenderer renderer_line = new StandardXYItemRenderer(StandardXYItemRenderer.SHAPES_AND_LINES);
        renderer_line.setShapesFilled(true);
        XYPlot plot = new XYPlot(dataset ,domainAxis,  electricAxis , renderer_line);
        
        chart = new JFreeChart("Result" , plot);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        chartPanel.setMouseZoomable(true, false);
        setContentPane(chartPanel);
        this.pack();
        RefineryUtilities.centerFrameOnScreen(this);
        this.setVisible(true);
	}
	
	private XYDataset createDataset()
	{
		SimpleDateFormat  date = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		String start = result.get(0).getDate();
		String end = result.get(1).getDate();
		Date d1 = null;
		Date d2 = null;
		try {
		    d1 = date.parse(start);
		    System.out.println(d1.toString());
		    d2 = date.parse(end);
		    System.out.println(d2.toString());
		} catch (ParseException e) {
		    e.printStackTrace();
		}  	
		long diff = d2.getTime() - d1.getTime();
		
		int period_min = (int) (diff / (60 * 1000) % 60); 
		
		int periodInHour = 60 /period_min;
		int count = 0;
		TimePeriodValues s1 = new TimePeriodValues("Electricity");
		Day today = new Day();
		int k = 0;
		for (int i = 0; i < 24; i++) {
            final Minute m0 = new Minute(0, new Hour(i, today));
            final Minute m1 = new Minute(15, new Hour(i, today));
            final Minute m2 = new Minute(30, new Hour(i, today));
            final Minute m3 = new Minute(45, new Hour(i, today));
            final Minute m4 = new Minute(0, new Hour(i + 1, today));
            for(; k < result.size();)
            {
            	count = 0;
            	if(count == 0)
            	{
            		s1.add(new SimpleTimePeriod(m0.getStart(), m1.getStart()), result.get(k).getPredic_elc());
            		count = count + period_min;
            		k++;
            	}
            	if(count == 15)
            	{
            		s1.add(new SimpleTimePeriod(m1.getStart(), m2.getStart()), result.get(k).getPredic_elc());
            		count = count + period_min;
            		k++;
            	}
            	if(count == 30)
            	{
            		s1.add(new SimpleTimePeriod(m2.getStart(), m3.getStart()), result.get(k).getPredic_elc());
            		count = count + period_min;
            		k++;
            	}
            	if(count == 60)
            	{
            		s1.add(new SimpleTimePeriod(m3.getStart(), m4.getStart()), result.get(k).getPredic_elc());
            		count = count + period_min;
            		k++;
            	}
                break;
            }
            
        }
		
		TimePeriodValuesCollection dataset = new TimePeriodValuesCollection();
        dataset.addSeries(s1);

        return dataset;
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
	}

}
