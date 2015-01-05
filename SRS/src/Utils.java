import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;


public class Utils {
	
  /*
  * Pattern for E-mail validation
  */
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private Pattern emailPattern;
	private Matcher matcher;
	public final String dbUserName = "UserName";
	public final String dbUserPass = "passwordOfUser";
  
  /*
  * enum for Exception mapping to user defined SQL Exception
  */
	public enum SQL_ERROR_CODES_MAP {
		SQL_EXP_STUDENT_NOT_PRESENT(20001),
		SQL_EXP_STUDENT_NO_COURSES(20002),
		SQL_EXP_STU_ALREADY_PRESENT(20003),
		SQL_EXP_COURSE_DOES_NOT_EXISTS(20004),
		SQL_EXP_CLASSID_NOT_PRESENT(20005),
		SQL_EXP_CLASS_LIMIT_REACHED(20006),
		SQL_EXP_CLASS_SIZE_NEG(20007),
		SQL_EXP_STU_COURSE_LIMIT_4(20008),
		SQL_EXP_STU_PREREQ_NOT_COMP(20009),
		SQL_EXP_STU_DROP_PREREQ(20010)
		;
		
		public int enumValue;
		
		SQL_ERROR_CODES_MAP(int enumVal){
			this.enumValue = enumVal;
		}
		public int getValue(){
			return this.enumValue;
		}
	}

	public Utils(){
		emailPattern = Pattern.compile(EMAIL_PATTERN);
	}
	
  /*
  * Utility function to validate the student id input in GUI
  */
	public boolean valSID(final String s, String [] errMsg){
		boolean retVal = false;
		if(this.isTxtBxEmpty(s)){
			retVal = true;
			errMsg[0] = "SID Cannot Be Empty";
		/*}else if(!( s.startsWith("B") || s.startsWith("b%")) ){*/
		}else if(!( s.startsWith("B")) ){
			errMsg[0] = "SID Should be of form 'B<Number>'";
			retVal = true;
		}else if(!this.isNumeric(s.substring(1), 1)){
			errMsg[0] = "SID Should be of form 'B<Number>'";
			retVal = true;
		}else if(s.length() > 4){
			errMsg[0] = "SID Should be of form 'B<Number>'";
			retVal = true;
		}
		return retVal;
	}
	
  /*
  * Method to validated the emptiness of the text boxes
  */
	public boolean isTxtBxEmpty(final String s){
		boolean retVal = false;
		if(s.isEmpty()){
			retVal = true;
		}else if(s.equals("")){
			retVal = true;
		}
		return retVal;
	}
	
  //Method override
	public boolean isNumeric(String s){
		return this.isNumeric(s, 0);
	}
  
	/*
  * Method to check if input value is numeric
  */
	public boolean isNumeric(String s, int type){
		if(s.isEmpty()){
			return false;
		}else if(s.equals("")){
			return false;
		}
		
		if(type == 0){//Integer and Float both allowed
			NumberFormat formatter = NumberFormat.getInstance();
			ParsePosition pos = new ParsePosition(0);
			formatter.parse(s, pos);
			return s.length() == pos.getIndex();
		}else if(type == 1){//Checks if all digits are numbers. 
			for (char c : s.toCharArray())
		    {
		        if (!Character.isDigit(c)) return false;
		    }
		    return true;
		}
		
		return false;
		/*
		return s.matches("-?\\d+(\\.\\d+)?");
		*/
	}
	
  /*
  * Method to Validate E-mail block
  */
	public boolean isEmailValid(final String s){
		this.matcher = this.emailPattern.matcher(s);
		return matcher.matches();
	}
	
  /*
  * Method to check the first error block of inputs
  */
	public int firstErrorBlock(boolean[] inArray){
		for (int i = 0; i < inArray.length; i++) {
			if(inArray[i] == true){
				return i;
			}
		}
		
		return -1;
	}
	
  /*
  * Method to Parse the user defined SQL Exception message and to extract 
  * required error message part
  */
	public Vector<String> parseJdbcErrorMessage(String inMsg){
		Vector<String> v = new Vector<String>();
		for(String sMsg : inMsg.split("\n")){
			//boolean flag=true;
			for(String sMsg1 : sMsg.split(":")){
				v.add(sMsg1);
			}
			break;
		}
		return v;
	}
	
  /*
  * Utility class to throw an exception
  */
	public class DbStatFail extends Exception{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public String getMessage(){
			return "Database trasaction failed";
		}
	}
	
  //Method override
	public static TableModel setToTableModel(ResultSet rs, int noFirstColsToNeg){
		return setToTableModel(rs, noFirstColsToNeg, null);
	}
	/*
  * Utility method that creates a table model to display tuples in JTable
  */
	public static TableModel setToTableModel(ResultSet rs, int noFirstColsToNeg, String[] dData){
		try{
			ResultSetMetaData mData = rs.getMetaData();
			int noColumns = mData.getColumnCount();
			Vector colNames = new Vector();
			Vector rowVal = new Vector();
			for (int i = noFirstColsToNeg; i < noColumns; i++) {
				colNames.addElement(mData.getColumnLabel(i+1));
			}
			boolean flag;
					
			if(dData==null){
				flag=false;
			}else{
				flag=true;
			}
			
			while(rs.next()){
				Vector tempRow = new Vector();
				if(flag){
					for (int j = 0; j < noFirstColsToNeg; j++) {
						dData[j] = rs.getString(j+1);
					}
					flag=false;
				}
				for (int j = noFirstColsToNeg; j < noColumns; j++) {
					tempRow.addElement(rs.getObject(j+1));
				}
				rowVal.addElement(tempRow);
			}
			return new DefaultTableModel(rowVal, colNames);
		}
		catch (SQLException ex) { 
			return null;
		}
		catch(Exception e){ 
			return null;
		}
	}
	
}
