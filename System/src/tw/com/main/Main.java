package tw.com.main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
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

import tw.com.linearRegression.LinearRegression;
import tw.com.linearRegression.CSVReader.CSVReader;
import tw.com.linearRegression.CSVReader.InputResource;
import tw.com.linearRegression.CSVWriter.CSVWriter;
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
	private ArrayList<InputResource> resources;

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
		
		JList<File> list = new JList<>();
		list.setBounds(14, 118, 394, 207);
		panel.add(list);
		
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
				for(int i = 0; i < resources.size(); i++)
				{
					LR.regressionAnalysis(resources.get(i));
					LR.reset();
				
					Graph drawGraph = new Graph(resources.get(i).getDataName() , resources.get(i) , LR.getSlope() , LR.getIntercept());
					drawGraph.drawGraph();
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
		
		

		/* Object[] col={"DataName","StandardError" , "R Square"};
		 Object[][] data={{"Test",123},{"XDDD",9453}};
		 table = new JTable(col);
		 table.setBounds(14, 378, 394, 207);
		panel.add(table);*/

		
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
		
		JSpinner spinnerHour = new JSpinner(new SpinnerDateModel(new Date(),null,null,Calendar.HOUR_OF_DAY));
		spinnerHour.setBounds(275, 32, 47, 26);
		spinnerHour.setEditor(new JSpinner.DateEditor(spinnerHour,"H"));
		panelDL.add(spinnerHour);
		
		JSpinner spinnerMinute = new JSpinner(new SpinnerDateModel(new Date(),null,null,Calendar.MINUTE));
		spinnerMinute.setBounds(343, 32, 47, 26);
		spinnerMinute.setEditor(new JSpinner.DateEditor(spinnerMinute,"m"));
		panelDL.add(spinnerMinute);
		
		JLabel label_2 = new JLabel("\u5E74");
		label_2.setBounds(84, 35, 20, 19);
		panelDL.add(label_2);
		
		JLabel label_3 = new JLabel("\u6708");
		label_3.setBounds(158, 35, 20, 19);
		panelDL.add(label_3);
		
		JLabel label_4 = new JLabel("\u65E5");
		label_4.setBounds(234, 35, 57, 19);
		panelDL.add(label_4);
		
		JLabel label_5 = new JLabel(":");
		label_5.setBounds(328, 35, 57, 19);
		panelDL.add(label_5);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(25, 142, 377, 354);
		panelDL.add(textArea);
		
		JButton button_3 = new JButton("\u4F7F\u7528\u985E\u795E\u7D93\u7DB2\u8DEF\u9810\u6E2C");
		button_3.setBounds(25, 88, 218, 27);
		button_3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				CSVWriter writer = new CSVWriter(resources);
				try {
					writer.write();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					System.out.println("�g�ɥ���");
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					System.out.println("�g�ɥ���");
				}
			}
		});
		panelDL.add(button_3);

		
		
	}
}