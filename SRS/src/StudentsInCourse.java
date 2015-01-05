/*
 * This Frame takes user input that is "cid". Calls the show_classinfo with the input. 
 * If Enable Validation check box is selected then the input is validated on GUI before sending the
 * data to DB. If it is disabled the data is directly sent to oracle procedure show_classinfo.
 * If any exceptions are raised, they are handled and respective error messages are printed on the screen. 
 * If "Display Everything in Table" check box is selected then all the data will be displayed on the JTable,
 * else 'classid' and 'course title' will be displayed in the respective text boxes and student details will 
 * be displayed in JTable
 */

//import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.SwingConstants;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JTextArea;

import java.awt.SystemColor;
import java.awt.Color;
import java.sql.*;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JTextField;

import oracle.jdbc.OracleTypes;
import oracle.jdbc.pool.OracleDataSource;

import javax.swing.JCheckBox;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

import javax.swing.JScrollPane;

import net.proteanit.sql.DbUtils;
import oracle.jdbc.*;
import oracle.jdbc.pool.OracleDataSource;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;


public class StudentsInCourse extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private HomeFrame homeFrame_m;
	private Utils util_m;
	private Boolean isValidateEnabled;
	private Boolean isShowTableEnabled;
	private JTextArea errorDisplay;
	private JTextField txtCID;
	private JLabel errMsgCID;
	private JCheckBox chckbxNewCheckBox;
	private JTextField txtResCID;
	private JTextField txtResCrsTtl;
	private JTable studentDisplayTab;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					StudentsInCourse frame = new StudentsInCourse();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public StudentsInCourse() {
		setTitle("CS532 : SRS : Students in a Class");
		homeFrame_m = null;
		isValidateEnabled = true;
		isShowTableEnabled = false;
		util_m = new Utils();
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			   @Override
			public void windowClosing(WindowEvent evt) {
				   loadHomeFrame();
			   }
		});
		setBounds(100, 100, 1600, 1000);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		
		JButton button = new JButton("< Home");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				StudentsInCourse.this.loadHomeFrame();
			}
		});
		button.setBounds(12, 13, 121, 42);
		contentPane.add(button);
		
		JLabel label = new JLabel("CS532 : Student Registration System");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Tahoma", Font.BOLD, 18));
		label.setBounds(610, 17, 361, 31);
		contentPane.add(label);
		
		JLabel lblStudentsInCourse = new JLabel("Students in a Class");
		lblStudentsInCourse.setHorizontalAlignment(SwingConstants.CENTER);
		lblStudentsInCourse.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblStudentsInCourse.setBounds(610, 79, 361, 31);
		contentPane.add(lblStudentsInCourse);
		
		JLabel lblExceptionsError = new JLabel("Exceptions / Error Message Display");
		lblExceptionsError.setHorizontalAlignment(SwingConstants.CENTER);
		lblExceptionsError.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblExceptionsError.setBounds(12, 164, 224, 68);
		contentPane.add(lblExceptionsError);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(248, 121, 1234, 154);
		contentPane.add(scrollPane_1);
		
		errorDisplay = new JTextArea();
		scrollPane_1.setViewportView(errorDisplay);
		errorDisplay.setWrapStyleWord(true);
		errorDisplay.setLineWrap(true);
		errorDisplay.setForeground(Color.RED);
		errorDisplay.setEditable(false);
		errorDisplay.setBackground(SystemColor.menu);
		
		chckbxNewCheckBox = new JCheckBox("Enable Validation");
		chckbxNewCheckBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if(arg0.getStateChange() == 1){
					//errorDisplay.append("Enabled Data Validation in GUI\n");
					StudentsInCourse.this.isValidateEnabled = true;
				}else{
					//errorDisplay.append("Disabled Data Validation in GUI\n");
					StudentsInCourse.this.isValidateEnabled = false;
					StudentsInCourse.this.clearFields(1);
				}
			}
		});
		chckbxNewCheckBox.setSelected(true);
		chckbxNewCheckBox.setBounds(248, 298, 157, 31);
		contentPane.add(chckbxNewCheckBox);
		
		JLabel lblCID = new JLabel("Class Id");
		lblCID.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblCID.setBounds(248, 355, 95, 31);
		contentPane.add(lblCID);
		
		txtCID = new JTextField();
		txtCID.setColumns(10);
		txtCID.setBounds(353, 355, 259, 31);
		contentPane.add(txtCID);
		addWindowListener( new WindowAdapter() {
		    @Override
			public void windowOpened( WindowEvent e ){
		    	txtCID.requestFocus();
		    }
		});
		
		errMsgCID = new JLabel("");
		errMsgCID.setForeground(Color.RED);
		errMsgCID.setBounds(621, 355, 650, 31);
		contentPane.add(errMsgCID);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean validationResult = !StudentsInCourse.this.isValidateEnabled;
				if(StudentsInCourse.this.isValidateEnabled){
					//errorDisplay.append("Calling Validation\n");
					validationResult = StudentsInCourse.this.validateTheFields();
				}
				if(validationResult){
					errorDisplay.append("Searching Data\n");
					StudentsInCourse.this.searchClassData();
				}
			}
		});
		btnSearch.setBounds(352, 397, 138, 31);
		contentPane.add(btnSearch);
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				StudentsInCourse.this.clearFields();
			}
		});
		btnClear.setBounds(500, 397, 138, 31);
		contentPane.add(btnClear);
		
		JLabel lblResCID = new JLabel("Class Id");
		lblResCID.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblResCID.setBounds(248, 458, 95, 31);
		contentPane.add(lblResCID);
		
		txtResCID = new JTextField();
		txtResCID.setEnabled(false);
		txtResCID.setEditable(false);
		txtResCID.setColumns(10);
		txtResCID.setBounds(353, 458, 259, 31);
		contentPane.add(txtResCID);
		
		JLabel lblCourseTitle = new JLabel("Course Title");
		lblCourseTitle.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblCourseTitle.setBounds(248, 502, 95, 31);
		contentPane.add(lblCourseTitle);
		
		txtResCrsTtl = new JTextField();
		txtResCrsTtl.setEnabled(false);
		txtResCrsTtl.setEditable(false);
		txtResCrsTtl.setColumns(10);
		txtResCrsTtl.setBounds(353, 502, 259, 31);
		contentPane.add(txtResCrsTtl);
		
		JLabel lblStudentsInClass = new JLabel("Students In Class");
		lblStudentsInClass.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblStudentsInClass.setBounds(248, 546, 243, 31);
		contentPane.add(lblStudentsInClass);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(248, 596, 1000, 276);
		contentPane.add(scrollPane);
		
		studentDisplayTab = new JTable();
		studentDisplayTab.setEnabled(false);
		scrollPane.setViewportView(studentDisplayTab);
		studentDisplayTab.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		studentDisplayTab.setFillsViewportHeight(true);
		
		JLabel label_1 = new JLabel("Copyright 2014 Harshavardhan B N & Matthew Mattyus");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setBounds(468, 917, 646, 23);
		contentPane.add(label_1);
		
		JCheckBox checkBox = new JCheckBox("Display Everything in Table");
		checkBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if(arg0.getStateChange() == 1){
					StudentsInCourse.this.isShowTableEnabled = true;
				}else{
					StudentsInCourse.this.isShowTableEnabled = false;
				}
			}
		});
		checkBox.setBounds(415, 546, 191, 31);
		contentPane.add(checkBox);
		
		getRootPane().setDefaultButton(btnSearch);
	}
	
  /*
  * Display the current frame
  */
	public void displayFrame(HomeFrame hf){
		homeFrame_m = hf;
		super.setVisible(true);
	}
	
  /*
  * Hide and close current frame and
  * display the home frame
  */
	public void loadHomeFrame(){
		if(homeFrame_m != null){
			homeFrame_m.home_frame.setVisible(true);
		}
		StudentsInCourse.super.setVisible(false);
		StudentsInCourse.super.dispose();
	}
	
  //Method override
	public void clearFields(){
		this.clearFields(0);
	}
	
  /*
  * Clear the input fields
  */
	public void clearFields(int type){
		if(type != 1){
			txtCID.setText("");
			txtResCID.setText("");
			txtResCrsTtl.setText("");
			DefaultTableModel d =(DefaultTableModel) studentDisplayTab.getModel();
			d.setRowCount(0);
			txtCID.requestFocus();
		}
		errMsgCID.setText("");
	}
	
  /*
  * Validate the input fields and set the cursor to the first error text block
  */
	public boolean validateTheFields(){
		boolean retValue = false;
		//String [] sTemp = new String[1];
		
		retValue = this.util_m.isTxtBxEmpty(txtCID.getText());
		if(retValue == false){
			errMsgCID.setText("");
		}else{
			errMsgCID.setText("Class Id Cannot be Empty");
			txtCID.requestFocus();
		}
		
		return !retValue;
	}
	
  
  /*
  * Function that calls oracle procedure and displays data/msg/error msg on screen. 
  */
	public void searchClassData(){
		Connection conn = null;
		CallableStatement cs = null;
		
		txtResCID.setEnabled(true);
		txtResCrsTtl.setEnabled(true);
		
		txtResCID.setText("");
		txtResCrsTtl.setText("");
		
		try{
			errorDisplay.setText("");
			//Connection to Oracle server
			OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
			ds.setURL("jdbc:oracle:thin:@grouchoIII.cc.binghamton.edu:1521:ACAD111");
			conn = ds.getConnection(this.util_m.dbUserName, this.util_m.dbUserPass);
			
			//Prepare to call stored procedure:
	        cs = conn.prepareCall("begin studentPackage.show_classinfo(?, ?); end;");
	        
	        //register the out parameter (the first parameter)
	        cs.registerOutParameter(2, OracleTypes.CURSOR);
	        
	        //Set Input Values
	        cs.setString(1, txtCID.getText());
	        
	        // execute and retrieve the result set
	        cs.execute();
	        ResultSet rs = (ResultSet)cs.getObject(2);
	        
	        if(!this.isShowTableEnabled){
		        String[] getData = new String[2];
		        TableModel tM = Utils.setToTableModel(rs, 2, getData);
		        if(tM == null)
		        	throw new NullPointerException("Failed to create Table Model");
		        studentDisplayTab.setModel(tM);
		        if(getData[0] != null)
		        	this.txtResCID.setText(getData[0].toString());
		        if(getData[1] != null)
		        	this.txtResCrsTtl.setText(getData[1].toString());
	        }else{
	        	//studentDisplayTab.setModel(DbUtils.resultSetToTableModel(rs));
	        	TableModel tM = Utils.setToTableModel(rs, 0);
	        	if(tM == null)
		        	throw new NullPointerException("Failed to create Table Model");
	        	studentDisplayTab.setModel(tM);
	        }
	        
	        errorDisplay.setForeground(Color.BLACK);
	        DefaultTableModel d = (DefaultTableModel)studentDisplayTab.getModel();
			if(d.getRowCount() == 0) {
				errorDisplay.setText("\n No student is enrolled in the class.\n");
			}else{
				errorDisplay.setText("\n Success. Data Displayed\n");
			}
	        cs.close();
	        conn.close();
		}
		catch (SQLException ex) {
			errorDisplay.setForeground(Color.RED);
			int iErrorCode = ex.getErrorCode();
			if(iErrorCode == Utils.SQL_ERROR_CODES_MAP.SQL_EXP_CLASSID_NOT_PRESENT.enumValue){
				Vector<String> v = this.util_m.parseJdbcErrorMessage(ex.getMessage());
				errorDisplay.setText("\n"+v.get(1).toString());
			}else{
				errorDisplay.setText("\n*** SQLException caught ***\n" + ex.getMessage());
			}
		}
		catch (Exception e) {
			errorDisplay.setForeground(Color.RED);
			errorDisplay.setText("\n*** other Exception caught ***\n");
		}finally{
			try{
				if(cs != null)
					cs.close();
				if(conn != null)
					conn.close();
			}catch (SQLException ex) {
				errorDisplay.setForeground(Color.RED);
				errorDisplay.setText("\n*** SQLException caught ***\n" + ex.getMessage());
			}
		}
	}
}
