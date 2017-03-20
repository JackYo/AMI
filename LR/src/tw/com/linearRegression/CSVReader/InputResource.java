package tw.com.linearRegression.CSVReader;

import java.util.ArrayList;

public class InputResource {
	
	private String dataName;
	private ArrayList<Float> datas = new ArrayList<>();
	private ArrayList<Float> elcDatas = new ArrayList<>();
	private int dataNum = 0;
	private float maxData = 0;
	private float maxElcData = 0;
	private float minData = 0;
	private float minElcData = 0;
	
	public ArrayList<Float> getDatas() {
		return datas;
	}

	public void setDatas(ArrayList<Float> datas) {
		this.datas = datas;
	}

	public InputResource(String dn)
	{
		setDataName(dn);
	}

	public String getDataName() {
		return dataName;
	}

	public void setDataName(String dataName) {
		this.dataName = dataName;
	}
	
	public void addData(float d)
	{
		datas.add(d);
	}
	
	public void addElcData(float d)
	{
		elcDatas.add(d);
	}

	public ArrayList<Float> getElcDatas() {
		return elcDatas;
	}

	public void setElcDatas(ArrayList<Float> elcDatas) {
		this.elcDatas = elcDatas;
	}

	public int getDataNum() {
		return dataNum;
	}

	public void setDataNum(int dataNum) {
		this.dataNum = dataNum;
	}

	public float getMaxData() {
		return maxData;
	}

	public void setMaxData(float maxData) {
		this.maxData = maxData;
	}

	public float getMaxElcData() {
		return maxElcData;
	}

	public void setMaxElcData(float maxElcData) {
		this.maxElcData = maxElcData;
	}

	public float getMinData() {
		return minData;
	}

	public void setMinData(float minData) {
		this.minData = minData;
	}

	public float getMinElcData() {
		return minElcData;
	}

	public void setMinElcData(float minElcData) {
		this.minElcData = minElcData;
	}

}
