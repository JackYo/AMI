package tw.com.main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import tw.com.linearRegression.LinearRegression;
import tw.com.linearRegression.CBLResult.ResultInfo;
import tw.com.linearRegression.CSVReader.CSVReader;
import tw.com.linearRegression.CSVReader.InputResource;
import tw.com.linearRegression.CSVWriter.CSVWriter;
import tw.com.linearRegression.drawGraph.DrawCBL;
import tw.com.linearRegression.drawGraph.Graph;

import javax.swing.JButton;
import javax.swing.JList;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;

public class Main {

	private JFrame frmCbl;
	private JTextField textField;
	
	private ArrayList<File> fileList;
	private JTable table;
	private JTable resultTable;
	private ArrayList<InputResource> resources;
	private ArrayList<ArrayList<Object>> array;
	private DefaultTableModel tm;
	private DefaultTableModel resultTm;
	private DefaultTableModel predictTM;
	private ArrayList<ResultInfo> result;
	private JTable tableRight;
	private int paramNum = 0;
	private String userChooseTime;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frmCbl.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		fileList=new ArrayList<>();
		
		frmCbl = new JFrame();
		frmCbl.setTitle("CBL");
		frmCbl.setBounds(100, 100, 900, 700);
		frmCbl.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmCbl.getContentPane().setLayout(new GridLayout(1, 0, 0, 0));
		
		JPanel panel = new JPanel();
		frmCbl.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel label = new JLabel("1.\u6A94\u6848\u8B80\u53D6");
		label.setBounds(14, 13, 88, 19);
		panel.add(label);
		
		textField = new JTextField();
		textField.setBackground(Color.WHITE);
		textField.setFont(new Font("PMingLiU", Font.PLAIN, 15));
		textField.setEnabled(false);
		textField.setBounds(14, 45, 281, 25);
		panel.add(textField);
		textField.setColumns(10);
		
		JScrollPane scrollPaneLeftList = new JScrollPane();
		scrollPaneLeftList.setBounds(14, 118, 394, 207);
		panel.add(scrollPaneLeftList);
		
		
		JList<File> list = new JList<>();
		/*list.setBounds(14, 118, 394, 207);
		panel.add(list);*/
		scrollPaneLeftList.setViewportView(list);
		
		JButton button = new JButton("\u700F\u89BD");
		button.setBounds(309, 44, 99, 27);
		button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileFilter((FileFilter)new FileNameExtensionFilter("Comma Seperated Value(.csv)","csv"));
				int returnValue = fileChooser.showOpenDialog(null); 
				if (returnValue == JFileChooser.APPROVE_OPTION) 
				{ 
					File selectedFile = fileChooser.getSelectedFile();
					textField.setText(selectedFile.getPath());
					fileList.add(selectedFile);
					list.setListData(fileList.toArray(new File[fileList.size()]));
				} 
			}
			
		});
		panel.add(button);
		
		JLabel label_1 = new JLabel("2.\u53C3\u6578\u9078\u64C7");
		label_1.setBounds(14, 83, 88, 19);
		panel.add(label_1);
		
		JButton button_1 = new JButton("3.\u5206\u6790");
		button_1.setBounds(14, 338, 99, 27);
		panel.add(button_1);
		button_1.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				CSVReader reader = new CSVReader(fileList);
				resources = reader.parse();
				LinearRegression LR = new LinearRegression();
				paramNum = resources.size();
				for(int i = 0; i < resources.size(); i++)
				{
					LR.regressionAnalysis(resources.get(i));
					LR.reset();
				
					Graph drawGraph = new Graph(resources.get(i).getDataName() , resources.get(i) , LR.getSlope() , LR.getIntercept());
					drawGraph.drawGraph();
					ArrayList<Object> ar = new ArrayList<>();
					ar.add(resources.get(i).getDataName());
					ar.add(LR.getSlopeStdErr());
					ar.add(LR.getRSquare());
					tm.addRow(ar.toArray(new Object[ar.size()]));
				}
				
			}
			
		});

		JButton buttonDelete = new JButton("\u522A\u9664");
		buttonDelete.setBounds(309, 338, 99, 27);
		buttonDelete.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				for(File s:list.getSelectedValuesList()){
					fileList.remove(s);
				}
				list.setListData(fileList.toArray(new File[fileList.size()]));
			}});
		panel.add(buttonDelete);
		
		Object[] col={"DataName","StandError" , "R Square"};
		Object[][] data={};

		tm=new DefaultTableModel();
		tm.setDataVector(data, col);
		//tm.addRow(col);
		
		JScrollPane scrollPaneLeftTable = new JScrollPane();
		scrollPaneLeftTable.setBounds(14, 404, 394, 207);
		panel.add(scrollPaneLeftTable);
		/*JPanel panelLeftTable = new JPanel();
		scrollPaneLeftTable.setViewportView(panelLeftTable);
		panelLeftTable.setLayout(null);*/
		
		
		
		table = new JTable(tm);
		/*table.setBounds(0,0,392,205);
		panelLeftTable.add(table);*/
		scrollPaneLeftTable.setViewportView(table);
		
//====================== PANEL SEPARATOR ========================================================================		
				
		JPanel panelDL = new JPanel();
		frmCbl.getContentPane().add(panelDL);
		panelDL.setLayout(null);
		
		JSpinner spinnerYear = new JSpinner(new SpinnerDateModel(new Date(),null,null,Calendar.YEAR));
		spinnerYear.setBounds(25, 32, 56, 26);
		spinnerYear.setEditor(new JSpinner.DateEditor(spinnerYear,"Y"));
		panelDL.add(spinnerYear);
		
		JSpinner spinnerMonth = new JSpinner(new SpinnerDateModel(new Date(),null,null,Calendar.MONTH));
		spinnerMonth.setBounds(105, 32, 47, 26);
		spinnerMonth.setEditor(new JSpinner.DateEditor(spinnerMonth,"M"));
		panelDL.add(spinnerMonth);
		
		JSpinner spinnerDate = new JSpinner(new SpinnerDateModel(new Date(),null,null,Calendar.DATE));
		spinnerDate.setBounds(178, 32, 47, 26);
		spinnerDate.setEditor(new JSpinner.DateEditor(spinnerDate,"d"));
		panelDL.add(spinnerDate);
		
		
		JLabel label_2 = new JLabel("\u5E74");
		label_2.setBounds(84, 35, 20, 19);
		panelDL.add(label_2);
		
		JLabel label_3 = new JLabel("\u6708");
		label_3.setBounds(158, 35, 20, 19);
		panelDL.add(label_3);
		
		JLabel label_4 = new JLabel("\u65E5");
		label_4.setBounds(234, 35, 57, 19);
		panelDL.add(label_4);
		
		
		Object[] resultCol={"Date","預測電量" , "Prediction Score" , "Training Score"};
		Object[][] resultData={};

		resultTm=new DefaultTableModel();
		resultTm.setDataVector(resultData, resultCol);
		//resultTm.addRow(resultCol);
		resultTable = new JTable(resultTm);
		resultTable.setBounds(25, 142, 377, 354);
		//panelDL.add(resultTable);
		
		JButton button_3 = new JButton("\u4F7F\u7528\u985E\u795E\u7D93\u7DB2\u8DEF\u9810\u6E2C");
		button_3.setBounds(25, 88, 218, 27);
		button_3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				userChooseTime = String.valueOf(spinnerYear.getValue()) + String.valueOf(spinnerMonth.getValue()) 
				+ String.valueOf(spinnerDate.getValue());
				CSVWriter writer = new CSVWriter(resources);
				try {
					writer.write();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					System.out.println("寫檔失敗");
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					System.out.println("寫檔失敗");
				}
				try{
					
					System.out.println("python start");
					ProcessBuilder pb = new ProcessBuilder("D:\\WinPython-64bit-3.4.4.6Qt5\\python-3.4.4.amd64\\python","D:\\AMI\\AMI\\System\\python\\mlp_lbfgs.py" , userChooseTime , Integer.toString(getPeriodTime()) ,"D:\\AMI\\AMI\\System\\data\\predictData.csv");
					
					Process p = pb.start();
					 
					String line;
					BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
					while ((line = in.readLine()) != null) {
						System.out.println(line);
					}
					System.out.println("\npython finished");
					//result output
					String path = "D:\\AMI\\AMI\\System\\output\\output_complete.csv";
					CSVReader reader = new CSVReader(path);
					 result = reader.resultParse();
					
					for(int i = 0; i < result.size(); i++)
					{
						ArrayList<Object> ar = new ArrayList<>();
						ar.add(result.get(i).getDate());
						ar.add(result.get(i).getPredic_elc());
						ar.add(result.get(i).getPredicScore());
						ar.add(result.get(i).getTrainScore());
						resultTm.addRow(ar.toArray(new Object[ar.size()]));
					}
				}catch(Exception e4){
					e4.printStackTrace();
				}
			}
		});
		panelDL.add(button_3);
		
		JButton buttonInputPara = new JButton("\u53C3\u6578\u8F38\u5165");
		 buttonInputPara.setBounds(291, 88, 99, 27);
		 buttonInputPara.addActionListener(new ActionListener(){
		 
		 @Override
		 public void actionPerformed(ActionEvent arg0) {
		 		JFrame addPara=new JFrame();
		 		addPara.setTitle("Parameter");
		 		addPara.setBounds(100, 100, 425, 375);
		 		addPara.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				addPara.getContentPane().setLayout(new GridLayout(1, 0, 0, 0));
		 		JPanel addPanel=new JPanel();
		 		addPara.getContentPane().add(addPanel);
		 		addPanel.setLayout(null);
		 		addPara.setVisible(true);
		 		
		 		JScrollPane scrollPanePara = new JScrollPane();
		 		scrollPanePara.setBounds(25, 25, 350, 200);
		 		addPanel.add(scrollPanePara);
		 		
		 		Object[] userPredictCol= new Object[paramNum + 2];
		 		Object[][] userPredictData= new Object[96][paramNum + 2];
		 		userPredictCol[0] = "Time";
		 		userPredictCol[paramNum + 1] = "Electricity";
		 		int k = 1;
		 		if(resources != null && resources.size() != 0 && paramNum != 0)
		 		{
		 			
			 		for(int i = 0; i < resources.size(); i++)
			 		{
			 			userPredictCol[k] = resources.get(i).getDataName();
			 			k++;
			 		}
			 		
			 		
			 		for(int i = 0; i < 96; i++)
			 		{
			 			for(int j = 0; j < paramNum + 2; j++)
			 			{
			 				userPredictData[i][j] = "";
			 			}
			 		}
			 		
		 		}
		 		predictTM = new DefaultTableModel();
		 		predictTM.setDataVector(userPredictData, userPredictCol);
				JTable addTable=new JTable(predictTM);
				scrollPanePara.setViewportView(addTable);
				
		 		JButton addRead=new JButton("\u8B80\u6A94");
		 		addRead.setBounds(25, 250, 100, 25);
		 		addRead.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						JFileChooser fileChooser = new JFileChooser();
						fileChooser.setFileFilter((FileFilter)new FileNameExtensionFilter("Comma Seperated Value(.csv)","csv"));
						int returnValue = fileChooser.showOpenDialog(null); 
						if (returnValue == JFileChooser.APPROVE_OPTION) 
						{ 
							File selectedFile = fileChooser.getSelectedFile();
							CSVReader reader = new CSVReader(selectedFile.getPath() , paramNum);
							ArrayList<String> userPData = reader.userPredictParse();
							
							if(userPData != null && userPData.size() != 0)
							{
								System.out.println(userPData.size());
								int i= 0;
								for(int k = 0; k < 96; k++)
								{
									for(int j = 0; j < paramNum + 2; j++)
									{
										if(userPData.get(i)!= null && !userPData.get(i).equals(""))
											userPredictData[k][j] = (String)userPData.get(i);
										i++;
									}
									
								}
								predictTM.setDataVector(userPredictData, userPredictCol);
							}
							
						}
					}
		 			
		 		});
		 		addPanel.add(addRead);
		 		JButton addSave=new JButton("\u5B58\u6A94");
		 		addSave.setBounds(275, 250, 100, 25);
		 		addSave.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						String[][] predictData = new String[predictTM.getRowCount()][predictTM.getColumnCount()]; 
						for(int i = 0; i < predictTM.getRowCount();i++)
						{
							for(int j = 0; j < predictTM.getColumnCount(); j++)
							{
								predictData[i][j] = (String)predictTM.getValueAt(i, j);
							}
						}
						CSVWriter writer = new CSVWriter(predictData);
						try {
							writer.predictDataWriter();
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							System.out.println("writer predictData error");
						} catch (UnsupportedEncodingException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							System.out.println("writer predictData error");
						}
						
					}
		 			
		 		});
		 		addPanel.add(addSave);
		 }});
		 	panelDL.add(buttonInputPara);
		 	JScrollPane scrollPaneRightTable = new JScrollPane();
		 	scrollPaneRightTable.setBounds(25, 142, 377, 354);
		 	scrollPaneRightTable.setViewportView(resultTable);
		 	panelDL.add(scrollPaneRightTable);
		 	

		JButton btncbl = new JButton("\u986F\u793ACBL\u66F2\u7DDA");
		btncbl.setBounds(269, 523, 133, 27);
		btncbl.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
				DrawCBL drawCBL = new DrawCBL(userChooseTime , result);
				drawCBL.draw();
			}
		});
		panelDL.add(btncbl);
		
	}
	
	public int getPeriodTime()
	{
		SimpleDateFormat  date = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		String start = resources.get(0).getTime().get(0);
		String end = resources.get(0).getTime().get(1);
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
		return period_min;
	}
}
