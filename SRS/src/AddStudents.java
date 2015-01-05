/*
 * This Frame takes user input. Adds the student into the table. 
 * If Enable Validation check box is selected then the input is validated on GUI before sending the
 * data to DB. If it is disabled the data is directly sent to oracle procedure add_students.
 * If any exceptions are raised, they are handled and respective error messages are printed on the screen. 
 */

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
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


public class AddStudents extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private HomeFrame homeFrame_m;
	private JTextArea errorDisplay;
	private JTextField txtSID;
	private JTextField txtFName;
	private JTextField txtLName;
	private JTextField txtStatus;
	private JTextField txtGPA;
	private JTextField txtEMail;
	private Boolean isValidateEnabled;
	private JLabel errMsgSID;
	private JLabel errMsgFName;
	private JLabel errMsgLName;
	private JLabel errMsgStatus;
	private JLabel errMsgGPA;
	private JLabel errMsgEMail;
	private Utils util_m;
	private JScrollPane scrollPane;
	private JLabel label_1;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					AddStudents frame = new AddStudents();
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
	public AddStudents() {
		setTitle("CS532 : SRS : Add Studetns");
		homeFrame_m = null;
		isValidateEnabled = true;
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
		
		JLabel label = new JLabel("CS532 : Student Registration System");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Tahoma", Font.BOLD, 18));
		label.setBounds(610, 17, 361, 31);
		contentPane.add(label);
		
		JLabel lblAddStudents = new JLabel("Add Students");
		lblAddStudents.setHorizontalAlignment(SwingConstants.CENTER);
		lblAddStudents.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblAddStudents.setBounds(610, 80, 361, 31);
		contentPane.add(lblAddStudents);
		
		JButton button = new JButton("< Home");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AddStudents.this.loadHomeFrame();
			}
		});
		button.setBounds(12, 11, 121, 42);
		contentPane.add(button);
		
		JLabel lblExceptionsError = new JLabel("Exceptions / Error Message Display");
		lblExceptionsError.setHorizontalAlignment(SwingConstants.CENTER);
		lblExceptionsError.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblExceptionsError.setBounds(10, 167, 224, 68);
		contentPane.add(lblExceptionsError);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(246, 124, 1234, 154);
		contentPane.add(scrollPane);
		
		errorDisplay = new JTextArea();
		scrollPane.setViewportView(errorDisplay);
		errorDisplay.setWrapStyleWord(true);
		errorDisplay.setLineWrap(true);
		errorDisplay.setForeground(Color.RED);
		errorDisplay.setEditable(false);
		errorDisplay.setBackground(SystemColor.menu);
		
		txtSID = new JTextField();
		txtSID.setBounds(351, 358, 259, 31);
		contentPane.add(txtSID);
		txtSID.setColumns(10);
		addWindowListener( new WindowAdapter() {
		    @Override
			public void windowOpened( WindowEvent e ){
		    	txtSID.requestFocus();
		    }
		}); 
		
		JLabel lblStudentId = new JLabel("Student Id");
		lblStudentId.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblStudentId.setBounds(246, 358, 95, 31);
		contentPane.add(lblStudentId);
		
		JLabel lblFirstName = new JLabel("First Name");
		lblFirstName.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblFirstName.setBounds(246, 400, 95, 31);
		contentPane.add(lblFirstName);
		
		JLabel lblLastName = new JLabel("Llast Name");
		lblLastName.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblLastName.setBounds(246, 442, 95, 31);
		contentPane.add(lblLastName);
		
		JLabel lblNewLabel = new JLabel("Status");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel.setBounds(246, 481, 95, 31);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("GPA");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_1.setBounds(246, 523, 95, 31);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("E-mail");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_2.setBounds(246, 565, 95, 31);
		contentPane.add(lblNewLabel_2);
		
		txtFName = new JTextField();
		txtFName.setColumns(50);
		txtFName.setBounds(351, 400, 259, 31);
		contentPane.add(txtFName);
		
		txtLName = new JTextField();
		txtLName.setColumns(10);
		txtLName.setBounds(351, 442, 259, 31);
		contentPane.add(txtLName);
		
		txtStatus = new JTextField();
		txtStatus.setColumns(10);
		txtStatus.setBounds(351, 481, 259, 31);
		contentPane.add(txtStatus);
		
		txtGPA = new JTextField();
		txtGPA.setColumns(10);
		txtGPA.setBounds(351, 523, 259, 31);
		contentPane.add(txtGPA);
		
		txtEMail = new JTextField();
		txtEMail.setColumns(10);
		txtEMail.setBounds(351, 565, 259, 31);
		contentPane.add(txtEMail);
		
		JButton btnInsert = new JButton("Insert");
		btnInsert.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean validationResult = !AddStudents.this.isValidateEnabled;
				if(AddStudents.this.isValidateEnabled){
					//errorDisplay.append("Calling Validation\n");
					validationResult = AddStudents.this.validateTheFields();
				}
				if(validationResult){
					//errorDisplay.append("Inserting Data\n");
					AddStudents.this.insertStudentData();
				}
			}
		});
		btnInsert.setBounds(351, 632, 138, 31);
		contentPane.add(btnInsert);
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				AddStudents.this.clearFields();
			}
		});
		btnClear.setBounds(499, 632, 138, 31);
		contentPane.add(btnClear);
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("Enable Validation");
		chckbxNewCheckBox.setSelected(true);
		chckbxNewCheckBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if(arg0.getStateChange() == 1){
					//errorDisplay.append("Enabled Data Validation in GUI\n");
					AddStudents.this.isValidateEnabled = true;
				}else{
					//errorDisplay.append("Disabled Data Validation in GUI\n");
					AddStudents.this.isValidateEnabled = false;
					AddStudents.this.clearFields(1);
				}
			}
		});
		chckbxNewCheckBox.setBounds(246, 301, 157, 31);
		contentPane.add(chckbxNewCheckBox);
		
		errMsgSID = new JLabel("");
		errMsgSID.setForeground(Color.RED);
		errMsgSID.setBounds(618, 358, 650, 31);
		contentPane.add(errMsgSID);
		
		errMsgFName = new JLabel("");
		errMsgFName.setForeground(Color.RED);
		errMsgFName.setBounds(618, 400, 650, 31);
		contentPane.add(errMsgFName);
		
		errMsgLName = new JLabel("");
		errMsgLName.setForeground(Color.RED);
		errMsgLName.setBounds(618, 442, 650, 31);
		contentPane.add(errMsgLName);
		
		errMsgStatus = new JLabel("");
		errMsgStatus.setForeground(Color.RED);
		errMsgStatus.setBounds(618, 481, 650, 31);
		contentPane.add(errMsgStatus);
		
		errMsgGPA = new JLabel("");
		errMsgGPA.setForeground(Color.RED);
		errMsgGPA.setBounds(618, 523, 650, 31);
		contentPane.add(errMsgGPA);
		
		errMsgEMail = new JLabel("");
		errMsgEMail.setForeground(Color.RED);
		errMsgEMail.setBounds(618, 565, 650, 31);
		contentPane.add(errMsgEMail);
		
		label_1 = new JLabel("Copyright 2014 Harshavardhan B N & Matthew Mattyus");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setBounds(468, 917, 646, 23);
		contentPane.add(label_1);
		
		getRootPane().setDefaultButton(btnInsert);
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
		AddStudents.super.setVisible(false);
		AddStudents.super.dispose();
	}
	
  /*
  * Validate the input fields and set the cursor to the first error text block
  */
	public boolean validateTheFields(){
		//errorDisplay.append("Entered Validate Function\n");
		boolean retValue = true;
		String [] sTemp = new String[1];
		boolean[] errorIndArray = new boolean[6];
		Arrays.fill(errorIndArray, false);
		
		errorIndArray[0] = this.util_m.valSID(txtSID.getText(), sTemp);
		if(errorIndArray[0] == false){
			errMsgSID.setText("");
		}else{
			errMsgSID.setText(sTemp[0].toString());
			retValue = false;
		}
		sTemp[0] = "";
		
		errorIndArray[1] = this.util_m.isTxtBxEmpty(txtFName.getText());
		
		if(errorIndArray[1] == false){
			errMsgFName.setText("");
		}else{
			errMsgFName.setText("First Name Cannot Be Empty");
			retValue = false;
		}
		
		errorIndArray[2] = this.util_m.isTxtBxEmpty(txtLName.getText());
		if(errorIndArray[2] == false){
			errMsgLName.setText("");
		}else{
			errMsgLName.setText("Last Name Cannot Be Empty");
			retValue = false;
		}
		
		if(!this.util_m.isTxtBxEmpty(txtStatus.getText())){
			if( !(
					(txtStatus.getText().equalsIgnoreCase("freshman")) ||
					(txtStatus.getText().equalsIgnoreCase("sophomore")) ||
					(txtStatus.getText().equalsIgnoreCase("junior")) ||
					(txtStatus.getText().equalsIgnoreCase("senior")) ||
					(txtStatus.getText().equalsIgnoreCase("graduate"))
					) ){
				errMsgStatus.setText("Status should be any one of 'freshman' // 'sophomore' // 'junior' // 'senior'// 'graduate'");
				errorIndArray[3] = true;
				retValue = false;
			}
			
		}
		
		if(errorIndArray[3] == false){
			errMsgStatus.setText("");
		}
		
		if(!this.util_m.isTxtBxEmpty(txtGPA.getText())){
			if(!this.util_m.isNumeric(txtGPA.getText())){
				errMsgGPA.setText("GPA should be a Number and valid range is between 0 and 4");
				errorIndArray[4] = true;
				retValue = false;
			}
			if( (Double.parseDouble(txtGPA.getText()) < 0) || (Double.parseDouble(txtGPA.getText()) > 4) ){
				errMsgGPA.setText("GPA should be a Number and valid range is between 0 and 4");
				errorIndArray[4] = true;
				retValue = false;
			}
		}
		
		
		
		if(errorIndArray[4] == false){
			errMsgGPA.setText("");
		}
		
		if(!txtEMail.getText().isEmpty()){
			if(!this.util_m.isEmailValid(txtEMail.getText())){
				errMsgEMail.setText("Please Enter a valid Email Address");
				errorIndArray[5] = true;
				retValue = false;
			}
		}
		
		if(errorIndArray[5] == false){
			errMsgEMail.setText("");
		}
		
		switch(this.util_m.firstErrorBlock(errorIndArray)){
			case 0:
					txtSID.requestFocus();
				break;
			case 1:
					txtFName.requestFocus();
				break;
			case 2:
					txtLName.requestFocus();
				break;
			case 3:
					txtStatus.requestFocus();
				break;
			case 4:
					txtGPA.requestFocus();
				break;
			case 5:
					txtEMail.requestFocus();
				break;
			default:
					txtSID.requestFocus();
				break;
		}
		
		return retValue;
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
			txtFName.setText("");
			txtLName.setText("");
			txtStatus.setText("");
			txtGPA.setText("");
			txtEMail.setText("");
			txtSID.requestFocus();
		}
		errMsgSID.setText("");
		errMsgFName.setText("");
		errMsgLName.setText("");
		errMsgStatus.setText("");
		errMsgGPA.setText("");
		errMsgEMail.setText("");
	}
	
	  /*
	  * Function that calls oracle procedure and displays data/msg/error msg on screen. 
	  */
	public void insertStudentData(){
		Connection conn = null;
		CallableStatement cs = null;
		
		try{
			errorDisplay.setText("");
			//Connection to Oracle server
			OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
			ds.setURL("jdbc:oracle:thin:@grouchoIII.cc.binghamton.edu:1521:ACAD111");
			conn = ds.getConnection(this.util_m.dbUserName, this.util_m.dbUserPass);
			
			//Prepare to call stored procedure:
			//String sProcedurePrep = "{call studentPackage.add_students(?,?,?,?,?,?)}";
			String sProcedurePrep = "{call studentPackage.add_students(?,?,?,?,?,?,?)}";
	        cs = conn.prepareCall(sProcedurePrep);
	        
	        //Set Input Values
	        cs.setString(1, txtSID.getText());
	        cs.setString(2, txtFName.getText());
	        cs.setString(3, txtLName.getText());
	        cs.setString(4, txtStatus.getText());
	        if(txtGPA.getText().isEmpty())
	        	cs.setString(5, txtGPA.getText());
	        else
	        	cs.setDouble(5, Double.parseDouble(txtGPA.getText()));
	        cs.setString(6, txtEMail.getText());
	        
	        //register the out parameter (the first parameter)
	        cs.registerOutParameter(7, java.sql.Types.INTEGER);
	        
	        // execute and retrieve the result
	        //cs.execute();
	        cs.executeUpdate();
	        
	        int iRet = cs.getInt(7);
	        
	        if(iRet == 1){
	        	errorDisplay.setForeground(Color.BLACK);
	        	errorDisplay.setText("\n Success. Student Inserted into Table\n");
	        }else{
	        	throw this.util_m.new DbStatFail();
	        }
	        cs.close();
	        conn.close();
		}
		catch (SQLException ex) {
			errorDisplay.setForeground(Color.RED);
			int iErrorCode = ex.getErrorCode();
			if(iErrorCode == Utils.SQL_ERROR_CODES_MAP.SQL_EXP_STU_ALREADY_PRESENT.enumValue){
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
