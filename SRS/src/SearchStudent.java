/*
 * This Frame takes user input that is "SID". Calls the show_studentinfo with the input. 
 * If Enable Validation check box is selected then the input is validated on GUI before sending the
 * data to DB. If it is disabled the data is directly sent to oracle procedure show_studentinfo.
 * If any exceptions are raised, they are handled and respective error messages are printed on the screen. 
 * If "Display Everything in Table" check box is selected then all the data will be displayed on the JTable,
 * else sid and first name will be displayed in the respective text boxes and Courses details will be displayed in JTable
 */
import java.awt.BorderLayout;
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


public class SearchStudent extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel pnlSearch;
	private HomeFrame homeFrame_m;
	private Utils util_m;
	private Boolean isValidateEnabled;
	private Boolean isShowTableEnabled;
	private JTextArea errorDisplay;
	private JTextField txtSID;
	private JLabel errMsgSID;
	private JCheckBox chckbxNewCheckBox;
	private JCheckBox chkTable; 
	private JTextField txtResSID;
	private JTextField txtResFName;
	private JTable courseDisplayTab;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					SearchStudent frame = new SearchStudent();
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
	public SearchStudent() {
		setTitle("CS532 : SRS : Search a student");
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
		pnlSearch = new JPanel();
		pnlSearch.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(pnlSearch);
		pnlSearch.setLayout(null);
		setLocationRelativeTo(null);
		
		JButton button = new JButton("< Home");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SearchStudent.this.loadHomeFrame();
			}
		});
		button.setBounds(12, 13, 121, 42);
		pnlSearch.add(button);
		
		JLabel label = new JLabel("CS532 : Student Registration System");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Tahoma", Font.BOLD, 18));
		label.setBounds(610, 17, 361, 31);
		pnlSearch.add(label);
		
		JLabel lblSeacrhStudents = new JLabel("Search a Student");
		lblSeacrhStudents.setHorizontalAlignment(SwingConstants.CENTER);
		lblSeacrhStudents.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblSeacrhStudents.setBounds(610, 79, 361, 31);
		pnlSearch.add(lblSeacrhStudents);
		
		JLabel lblExceptionsError = new JLabel("Exceptions / Error Message Display");
		lblExceptionsError.setHorizontalAlignment(SwingConstants.CENTER);
		lblExceptionsError.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblExceptionsError.setBounds(12, 179, 224, 68);
		pnlSearch.add(lblExceptionsError);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(248, 136, 1234, 154);
		pnlSearch.add(scrollPane_1);
		
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
					SearchStudent.this.isValidateEnabled = true;
				}else{
					//errorDisplay.append("Disabled Data Validation in GUI\n");
					SearchStudent.this.isValidateEnabled = false;
					SearchStudent.this.clearFields(1);
				}
			}
		});
		chckbxNewCheckBox.setSelected(true);
		chckbxNewCheckBox.setBounds(248, 313, 157, 31);
		pnlSearch.add(chckbxNewCheckBox);
		
		JLabel lblStudentId = new JLabel("Student Id");
		lblStudentId.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblStudentId.setBounds(248, 370, 95, 31);
		pnlSearch.add(lblStudentId);
		
		txtSID = new JTextField();
		txtSID.setColumns(10);
		txtSID.setBounds(353, 370, 259, 31);
		pnlSearch.add(txtSID);
		addWindowListener( new WindowAdapter() {
		    @Override
			public void windowOpened( WindowEvent e ){
		    	txtSID.requestFocus();
		    }
		});
		
		errMsgSID = new JLabel("");
		errMsgSID.setForeground(Color.RED);
		errMsgSID.setBounds(621, 370, 650, 31);
		pnlSearch.add(errMsgSID);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean validationResult = !SearchStudent.this.isValidateEnabled;
				if(SearchStudent.this.isValidateEnabled){
					//errorDisplay.append("Calling Validation\n");
					validationResult = SearchStudent.this.validateTheFields();
				}
				if(validationResult){
					errorDisplay.append("Searching Data\n");
					SearchStudent.this.searchStudentData();
				}
			}
		});
		btnSearch.setBounds(353, 414, 138, 31);
		pnlSearch.add(btnSearch);
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SearchStudent.this.clearFields();
			}
		});
		btnClear.setBounds(501, 414, 138, 31);
		pnlSearch.add(btnClear);
		
		JLabel lblResSID = new JLabel("Student Id");
		lblResSID.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblResSID.setBounds(248, 500, 95, 31);
		pnlSearch.add(lblResSID);
		
		txtResSID = new JTextField();
		txtResSID.setEnabled(false);
		txtResSID.setEditable(false);
		txtResSID.setColumns(10);
		txtResSID.setBounds(353, 500, 259, 31);
		pnlSearch.add(txtResSID);
		
		JLabel lblFirstName = new JLabel("First Name");
		lblFirstName.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblFirstName.setBounds(248, 550, 95, 31);
		pnlSearch.add(lblFirstName);
		
		txtResFName = new JTextField();
		txtResFName.setEnabled(false);
		txtResFName.setEditable(false);
		txtResFName.setColumns(10);
		txtResFName.setBounds(353, 550, 259, 31);
		pnlSearch.add(txtResFName);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(248, 644, 1000, 276);
		pnlSearch.add(scrollPane);
		
		courseDisplayTab = new JTable();
		courseDisplayTab.setEnabled(false);
		scrollPane.setViewportView(courseDisplayTab);
		courseDisplayTab.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		courseDisplayTab.setFillsViewportHeight(true);
		
		JLabel lblCoursesTakenBy = new JLabel("Courses Taken by student");
		lblCoursesTakenBy.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblCoursesTakenBy.setBounds(248, 594, 243, 31);
		pnlSearch.add(lblCoursesTakenBy);
		
		JLabel label_1 = new JLabel("Copyright 2014 Harshavardhan B N & Matthew Mattyus");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setBounds(468, 917, 646, 23);
		pnlSearch.add(label_1);
		
		chkTable = new JCheckBox("Display Everything in Table");
		chkTable.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if(arg0.getStateChange() == 1){
					SearchStudent.this.isShowTableEnabled = true;
				}else{
					SearchStudent.this.isShowTableEnabled = false;
				}				
			}
		});
		chkTable.setBounds(438, 594, 191, 31);
		pnlSearch.add(chkTable);
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
		SearchStudent.super.setVisible(false);
		SearchStudent.super.dispose();
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
			txtSID.setText("");
			txtResFName.setText("");
			txtResSID.setText("");
			DefaultTableModel d = (DefaultTableModel)courseDisplayTab.getModel();
			d.setRowCount(0);
			txtSID.requestFocus();
		}
		errMsgSID.setText("");
	}
	
  /*
  * Validate the input fields and set the cursor to the first error text block
  */
	public boolean validateTheFields(){
		boolean retValue = false;
		String [] sTemp = new String[1];
		
		retValue = this.util_m.valSID(txtSID.getText(), sTemp);
		if(retValue == false){
			errMsgSID.setText("");
		}else{
			errMsgSID.setText(sTemp[0].toString());
			txtSID.requestFocus();
		}
		sTemp[0] = "";
		return !retValue;
	}
	
  /*
  * Function that calls oracle procedure and displays data/msg/error msg on screen. 
  */
	public void searchStudentData(){
		Connection conn = null;
		CallableStatement cs = null;
		
		this.txtResSID.setEnabled(true);
		this.txtResFName.setEnabled(true);
		
		this.txtResSID.setText("");
		this.txtResFName.setText("");
		
		try{
			errorDisplay.setText("");
			//Connection to Oracle server
			OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
			ds.setURL("jdbc:oracle:thin:@grouchoIII.cc.binghamton.edu:1521:ACAD111");
			conn = ds.getConnection(this.util_m.dbUserName, this.util_m.dbUserPass);
			
			//Prepare to call stored procedure:
	        cs = conn.prepareCall("begin studentPackage.show_studentinfo(?,?); end;");
	        
	        //Set student ID
	        cs.setString(1, txtSID.getText());
	        
	        //register the out parameter (the first parameter)
	        cs.registerOutParameter(2, OracleTypes.CURSOR);
	        
	        // execute and retrieve the result set
	        cs.execute();
	        ResultSet rs = (ResultSet)cs.getObject(2);
	        
	        if(!this.isShowTableEnabled){
		        String[] getData = new String[2];
		        TableModel tM = Utils.setToTableModel(rs, 2, getData);
		        if(tM == null)
		        	throw new NullPointerException("Failed to create Table Model");
		        courseDisplayTab.setModel(tM);
		        if(getData[0] != null)
		        	this.txtResSID.setText(getData[0].toString());
		        if(getData[1] != null)
		        	this.txtResFName.setText(getData[1].toString());
	        }else{
	        	//courseDisplayTab.setModel(DbUtils.resultSetToTableModel(rs));
	        	TableModel tM = Utils.setToTableModel(rs, 0);
		        if(tM == null)
		        	throw new NullPointerException("Failed to create Table Model");
	        	courseDisplayTab.setModel(tM);
	        }
	        errorDisplay.setForeground(Color.BLACK);
	        errorDisplay.setText("\n Success. Data Displayed\n");
	        cs.close();
	        conn.close();
		}
		catch (SQLException ex) {
			errorDisplay.setForeground(Color.RED);
			int iErrorCode = ex.getErrorCode();
			if( (iErrorCode == Utils.SQL_ERROR_CODES_MAP.SQL_EXP_STUDENT_NOT_PRESENT.enumValue) ||
			    (iErrorCode == Utils.SQL_ERROR_CODES_MAP.SQL_EXP_STUDENT_NO_COURSES.enumValue) ){
				Vector<String> v = this.util_m.parseJdbcErrorMessage(ex.getMessage());
				errorDisplay.setText("\n"+v.get(1).toString());
			}else{
				errorDisplay.setText("\n*** SQLException caught ***\n" + ex.getMessage());
			}
		}
		catch (Exception e) {
			errorDisplay.setForeground(Color.RED);
			errorDisplay.setText("\n*** other Exception caught ***\n" + e.getMessage());
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
