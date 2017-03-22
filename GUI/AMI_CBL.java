package tw.edu.nchu.cs.dmlab.ami;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JButton;
import javax.swing.JList;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JSpinner;

public class AMI_CBL {

	private JFrame frmCbl;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AMI_CBL window = new AMI_CBL();
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
	public AMI_CBL() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
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
				} 
			}
			
		});
		panel.add(button);
		
		JList<String> list = new JList<>();
		list.setBounds(14, 118, 413, 374);
		panel.add(list);
		
		JLabel label_1 = new JLabel("2.\u53C3\u6578\u9078\u64C7");
		label_1.setBounds(14, 83, 88, 19);
		panel.add(label_1);
		
		JButton button_1 = new JButton("3.\u5206\u6790");
		button_1.setBounds(14, 522, 99, 27);
		panel.add(button_1);
		
		
		JPanel panelDL = new JPanel();
		frmCbl.getContentPane().add(panelDL);
		panelDL.setLayout(null);
		
		JSpinner spinnerYear = new JSpinner(new SpinnerDateModel(new Date(),null,null,Calendar.YEAR));
		spinnerYear.setBounds(14, 32, 56, 26);
		spinnerYear.setEditor(new JSpinner.DateEditor(spinnerYear,"Y"));
		panelDL.add(spinnerYear);
		
		JSpinner spinnerMonth = new JSpinner(new SpinnerDateModel(new Date(),null,null,Calendar.MONTH));
		spinnerMonth.setBounds(84, 32, 47, 26);
		spinnerMonth.setEditor(new JSpinner.DateEditor(spinnerMonth,"M"));
		panelDL.add(spinnerMonth);
		
		JSpinner spinnerDate = new JSpinner(new SpinnerDateModel(new Date(),null,null,Calendar.DATE));
		spinnerDate.setBounds(146, 32, 37, 26);
		spinnerDate.setEditor(new JSpinner.DateEditor(spinnerDate,"D"));
		panelDL.add(spinnerDate);
		
		JSpinner spinnerHour = new JSpinner(new SpinnerDateModel(new Date(),null,null,Calendar.HOUR_OF_DAY));
		spinnerHour.setBounds(232, 32, 56, 26);
		spinnerHour.setEditor(new JSpinner.DateEditor(spinnerHour,"h"));
		panelDL.add(spinnerHour);
		
		JSpinner spinnerMinute = new JSpinner(new SpinnerDateModel(new Date(),null,null,Calendar.MINUTE));
		spinnerMinute.setBounds(318, 32, 47, 26);
		spinnerMinute.setEditor(new JSpinner.DateEditor(spinnerMinute,"m"));
		panelDL.add(spinnerMinute);
		
		
	}
}
