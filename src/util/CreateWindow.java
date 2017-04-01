/*
 * Create windows to accept user input 
 * Also create html page for each action
 * createAllHTML -> create report page with all data
 * createWithoutDonutsHTML -> create a report page with all transactions except donuts
 * createWithoutCCHTML -> create a report page with all transactions except CreditCard payment. Also create new report with only CreditCard payemnt
 * createWithoutCCNDonutsHTML -> Create a report page with all transactions except Donuts and Credit Card paymenets. Also create new report with Credit Card payment only.
 */
package util;

import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.*;

public class CreateWindow extends Frame implements WindowListener,ActionListener{
    protected JButton submit;
    protected JCheckBox checkDonuts;
    protected JCheckBox checkCCpayment;
    protected JCheckBox checkCrystalBall;
    protected JTextField crystalBallYear;
    protected JTextField crystalBallMonth;
    protected TreeMap<Integer, Node[][]> transactionsTemp;
    
    // create a report page with all transactions.
    protected String createAllHTML(){
    	StringBuilder sb = new StringBuilder("<meta http-equiv='Content-Type' content='text/html; charset=utf-8'><html><head><title></title></head><body><table border=1><tr><th colspan=4> Transaction Report </th></tr><tr><th>Month</th><th>Year</th><th>Spent</th><th>Income</th>");
    	for(Map.Entry<Integer, Node[][]> entry : transactionsTemp.entrySet()) {
			  long income = 0L;
			  long expense = 0L;
			  long totalIncome = 0L;
			  long totalExpense = 0L;
			  int totalIncomeCnt = 0;
			  int totalExpenseCnt = 0;
			  int incomeCnt=0;
			  int expenseCnt=0;
			  Node transTmp = null;
			  for(int i = 0; i < 12; i++){
				  if(entry.getValue()[0][i] != null){
					  transTmp = entry.getValue()[0][i];
					  while(transTmp != null){
						  expense += transTmp.getAmount();
						  transTmp = transTmp.next;
						  expenseCnt++;
					  }
				  }
				  if(entry.getValue()[1][i] != null){
					  transTmp = entry.getValue()[1][i];
					  while(transTmp != null){
						  income += transTmp.getAmount();
						  transTmp = transTmp.next;
						  incomeCnt++;
					  }
				  }
				  if(incomeCnt != 0 || expenseCnt != 0) {
					  int month = i+1;
					  sb.append("<tr><td>"+month+"</td><td>"+entry.getKey()+"</td><td>$"+NumberFormat.getNumberInstance(Locale.US).format(expense*-1)+"</td><td>$"+NumberFormat.getNumberInstance(Locale.US).format(income)+"</td></tr>");
					  if(incomeCnt != 0 && expenseCnt != 0){
						  sb.append("<tr><th colspan=2>Average (per Transaction) Total number of Spent/Income Transaction: " + expenseCnt + "/" + incomeCnt + "</th><td>$"+NumberFormat.getNumberInstance(Locale.US).format((expense*-1)/expenseCnt)+"</td><td>$"+NumberFormat.getNumberInstance(Locale.US).format(income/incomeCnt)+"</td></tr>");
					  }
					  else if(incomeCnt == 0){
						  sb.append("<tr><th colspan=2>Average (per Transaction) Total number of Spent/Income Transaction: " + expenseCnt + "/" + incomeCnt + "</th><td>$"+NumberFormat.getNumberInstance(Locale.US).format((expense*-1)/expenseCnt)+"</td><td>$0</td></tr>");
					  }
					  else if (expenseCnt == 0){
						  sb.append("<tr><th colspan=2>Average (per Transaction) Total number of Spent/Income Transaction: " + expenseCnt + "/" + incomeCnt + "</th><td>$0</td><td>$"+NumberFormat.getNumberInstance(Locale.US).format(income/incomeCnt)+"</td></tr>");
					  }
					  totalIncomeCnt += incomeCnt;
					  totalExpenseCnt += expenseCnt;
					  totalIncome += income;
					  totalExpense += expense;
					  income = 0L;
					  expense = 0L;
					  expenseCnt = 0;
					  incomeCnt = 0;
				  }
			  }
			  sb.append("<tr><th colspan=2>Average (per Transaction) for "+entry.getKey()+ " Total number of Spent/Income Transaction: " + totalExpenseCnt + "/" + totalIncomeCnt + "</th><td>$"+NumberFormat.getNumberInstance(Locale.US).format((totalExpense*-1)/totalExpenseCnt)+"</td><td>$"+NumberFormat.getNumberInstance(Locale.US).format(totalIncome/totalIncomeCnt)+"</td></tr>");
			  totalIncomeCnt = 0;
			  totalExpenseCnt = 0;
			  totalIncome = 0L;
			  totalExpense = 0L;
		}
    	sb.append("</table></body></html>");
    	return sb.toString();
    }
    
    // create report page with all transactions except donuts
    protected String createWithoutDonutsHTML(){
    	StringBuilder sb = new StringBuilder("<meta http-equiv='Content-Type' content='text/html; charset=utf-8'><html><head><title></title></head><body><table border=1><tr><th colspan=4> Transaction Report without Donuts expense</th></tr><tr><th>Month</th><th>Year</th><th>Spent</th><th>Income</th>");
    	for(Map.Entry<Integer, Node[][]> entry : transactionsTemp.entrySet()) {
			  long income = 0L;
			  long expense = 0L;
			  long totalIncome = 0L;
			  long totalExpense = 0L;
			  int totalIncomeCnt = 0;
			  int totalExpenseCnt = 0;
			  int incomeCnt=0;
			  int expenseCnt=0;
			  Node transTmp = null;
			  for(int i = 0; i < 12; i++){
				  if(entry.getValue()[0][i] != null){
					  transTmp = entry.getValue()[0][i];
					  while(transTmp != null){
						  if(!transTmp.getMerchant().equals("Krispy Kreme Donuts") && !transTmp.getMerchant().equals("DUNKIN #336784")){
							  expense += transTmp.getAmount();
							  expenseCnt++;
						  }
						  transTmp = transTmp.next;
					  }
				  }
				  if(entry.getValue()[1][i] != null){
					  transTmp = entry.getValue()[1][i];
					  while(transTmp != null){
						  income += transTmp.getAmount();
						  transTmp = transTmp.next;
						  incomeCnt++;
					  }
				  }
				  if(incomeCnt != 0 || expenseCnt != 0) {
					  int month = i+1;
					  sb.append("<tr><td>"+month+"</td><td>"+entry.getKey()+"</td><td>$"+NumberFormat.getNumberInstance(Locale.US).format(expense*-1)+"</td><td>$"+NumberFormat.getNumberInstance(Locale.US).format(income)+"</td></tr>");
					  if(incomeCnt != 0 && expenseCnt != 0){
						  sb.append("<tr><th colspan=2>Average (per Transaction)Total number of Spent/Income Transaction: " + expenseCnt + "/" + incomeCnt + "</th><td>$"+NumberFormat.getNumberInstance(Locale.US).format((expense*-1)/expenseCnt)+"</td><td>$"+NumberFormat.getNumberInstance(Locale.US).format(income/incomeCnt)+"</td></tr>");
					  }
					  else if(incomeCnt == 0){
						  sb.append("<tr><th colspan=2>Average (per Transaction)Total number of Spent/Income Transaction: " + expenseCnt + "/" + incomeCnt + "</th><td>$"+NumberFormat.getNumberInstance(Locale.US).format((expense*-1)/expenseCnt)+"</td><td>$0</td></tr>");
					  }
					  else if (expenseCnt == 0){
						  sb.append("<tr><th colspan=2>Average (per Transaction)Total number of Spent/Income Transaction: " + expenseCnt + "/" + incomeCnt + "</th><td>$0</td><td>$"+NumberFormat.getNumberInstance(Locale.US).format(income/incomeCnt)+"</td></tr>");
					  }
					  totalIncomeCnt += incomeCnt;
					  totalExpenseCnt += expenseCnt;
					  totalIncome += income;
					  totalExpense += expense;
					  income = 0L;
					  expense = 0L;
					  expenseCnt = 0;
					  incomeCnt = 0;
				  }
			  }
			  sb.append("<tr><th colspan=2>Average (per Transaction) for "+entry.getKey()+ " Total number of Spent/Income Transaction: " + totalExpenseCnt + "/" + totalIncomeCnt + "</th><td>$"+NumberFormat.getNumberInstance(Locale.US).format((totalExpense*-1)/totalExpenseCnt)+"</td><td>$"+NumberFormat.getNumberInstance(Locale.US).format(totalIncome/totalIncomeCnt)+"</td></tr>");
			  totalIncomeCnt = 0;
			  totalExpenseCnt = 0;
			  totalIncome = 0L;
			  totalExpense = 0L;
		}
    	sb.append("</table></body></html>");
    	return sb.toString();
    }
    // create a report page with all transactions except CreditCard payment. Also create new report with only CreditCard payemnt
    protected String createWithoutCCHTML(){
    	ArrayList<Node> ccList = new ArrayList<Node>();
    	StringBuilder sb = new StringBuilder("<meta http-equiv='Content-Type' content='text/html; charset=utf-8'><html><head><title></title></head><body><table border=1><tr><th colspan=4> Transaction Report filterd CreditCard Payment</th></tr><tr><th>Month</th><th>Year</th><th>Spent</th><th>Income</th>");
    	for(Map.Entry<Integer, Node[][]> entry : transactionsTemp.entrySet()) {
			  long income = 0L;
			  long expense = 0L;
			  long totalIncome = 0L;
			  long totalExpense = 0L;
			  int totalIncomeCnt = 0;
			  int totalExpenseCnt = 0;
			  int incomeCnt=0;
			  int expenseCnt=0;
			  Node transTmp = null;
			  Node incomeCC = null;
			  Node expenseCC = null;
			  for(int i = 0; i < 12; i++){
				  if(entry.getValue()[0][i] != null){
					  transTmp = entry.getValue()[0][i];
					  while(transTmp != null){
						  if(transTmp.getMerchant().equals("CC Payment")){
							  ccList.add(transTmp);
							  expenseCC = transTmp;
						  }
						  expense += transTmp.getAmount();
						  expenseCnt++;
						  transTmp = transTmp.next;
					  }
				  }
				  if(entry.getValue()[1][i] != null){
					  transTmp = entry.getValue()[1][i];
					  while(transTmp != null){
						  if(transTmp.getMerchant().equals("Credit Card Payment")){
							  ccList.add(transTmp);
							  incomeCC = transTmp;
						  }
						  income += transTmp.getAmount();
						  incomeCnt++;
						  transTmp = transTmp.next;
					  }
				  }
				  if(incomeCnt != 0 || expenseCnt != 0) {
					  if(expenseCC != null && incomeCC != null){
					    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					    	try{
					    		Date d1 = format.parse(expenseCC.getTransactionTime().substring(0, 10) + " " + expenseCC.getTransactionTime().substring(11, 19));
					    		Date d2 = format.parse(incomeCC.getTransactionTime().substring(0, 10) + " " + incomeCC.getTransactionTime().substring(11, 19));
					    		long diff;
					    		if(d1.compareTo(d2) > 0){
					    			diff = d1.getTime() - d2.getTime();
					    		}
					    		else{
					    			diff = d2.getTime() - d1.getTime();
					    		}
					    		long diffDays = diff / (24 * 60 * 60 * 1000);
					    		long diffHours = diff / (60 * 60 * 1000) % 24;
					    		if(diffHours >= 0 && diffHours <= 24 && diffDays <= 1){
					    			incomeCnt--;
					    			expenseCnt--;
					    			income -= incomeCC.getAmount();
					    			expense -= expenseCC.getAmount();
					    		}
					    	}
					    	catch(Exception e){
					    		e.printStackTrace();
					    	}
					  }
					  int month = i+1;
					  sb.append("<tr><td>"+month+"</td><td>"+entry.getKey()+"</td><td>$"+NumberFormat.getNumberInstance(Locale.US).format(expense*-1)+"</td><td>$"+NumberFormat.getNumberInstance(Locale.US).format(income)+"</td></tr>");
					  if(incomeCnt != 0 && expenseCnt != 0){
						  sb.append("<tr><th colspan=2>Average (per Transaction) Total number of Spent/Income Transaction: " + expenseCnt + "/" + incomeCnt + "</th><td>$"+NumberFormat.getNumberInstance(Locale.US).format((expense*-1)/expenseCnt)+"</td><td>$"+NumberFormat.getNumberInstance(Locale.US).format(income/incomeCnt)+"</td></tr>");
					  }
					  else if(incomeCnt == 0){
						  sb.append("<tr><th colspan=2>Average (per Transaction) Total number of Spent/Income Transaction: " + expenseCnt + "/" + incomeCnt + "</th><td>$"+NumberFormat.getNumberInstance(Locale.US).format((expense*-1)/expenseCnt)+"</td><td>$0</td></tr>");
					  }
					  else if(expenseCnt == 0){
						  sb.append("<tr><th colspan=2>Average (per Transaction) Total number of Spent/Income Transaction: " + expenseCnt + "/" + incomeCnt + "</th><td>$0</td><td>$"+NumberFormat.getNumberInstance(Locale.US).format(income/incomeCnt)+"</td></tr>");
					  }
					  totalIncomeCnt += incomeCnt;
					  totalExpenseCnt += expenseCnt;
					  totalIncome += income;
					  totalExpense += expense;
					  income = 0L;
					  expense = 0L;
					  expenseCnt = 0;
					  incomeCnt = 0;
				  }
			  }
			  sb.append("<tr><th colspan=2>Average (per Transaction) for "+entry.getKey()+ " Total number of Spent/Income Transaction: " + totalExpenseCnt + "/" + totalIncomeCnt + "</th><td>$"+NumberFormat.getNumberInstance(Locale.US).format((totalExpense*-1)/totalExpenseCnt)+"</td><td>$"+NumberFormat.getNumberInstance(Locale.US).format(totalIncome/totalIncomeCnt)+"</td></tr>");
			  totalIncomeCnt = 0;
			  totalExpenseCnt = 0;
			  totalIncome = 0L;
			  totalExpense = 0L;
		}
    	sb.append("</table><table border=1><tr><th colspan=2>Credit Card Payment transaction</th></tr><tr><th>Amount</th><th>Date</th></tr>");
    	Node ccTmp = null;
    	Node ccTmp1 = null;
    	int q = 0;
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	while(q < ccList.size()){
    		ccTmp = ccList.get(q);
    		if(q+1 >= ccList.size()) break;
    		ccTmp1 = ccList.get(q+1);
    		try{
    			Date d1 = format.parse(ccTmp.getTransactionTime().substring(0, 10) + " " + ccTmp.getTransactionTime().substring(11, 19));
    			Date d2 = format.parse(ccTmp1.getTransactionTime().substring(0, 10) + " " + ccTmp1.getTransactionTime().substring(11, 19));
    			long diff;
    			if(d1.compareTo(d2) > 0){
    				diff = d1.getTime() - d2.getTime();
    			}
    			else{
    				diff = d2.getTime() - d1.getTime();
    			}
    			long diffDays = diff / (24 * 60 * 60 * 1000);
    			long diffHours = diff / (60 * 60 * 1000) % 24;
    			if(diffHours >= 0 && diffHours <= 24 && diffDays <= 1){
    				sb.append("<tr><td>$" + ccTmp.getAmount() + "</td><td>Transaction DateTimes: " + d1.toString() + " and " + d2.toString()+ "</td></tr>");
        			q += 2;
    			}
    			else{
    				q++;
    			}
    		}
    		catch(Exception e){
    			e.printStackTrace();
    		}
    		
    	}
    	sb.append("</table></body></html>");
    	return sb.toString();
    }
    
    // Create a report page with all transactions except Donuts and Credit Card paymenets. Also create new report with Credit Card payment only.
    protected String createWithoutCCNDonutsHTML(){
    	ArrayList<Node> ccList = new ArrayList<Node>();
    	StringBuilder sb = new StringBuilder("<meta http-equiv='Content-Type' content='text/html; charset=utf-8'><html><head><title></title></head><body><table border=1><tr><th colspan=4> Transaction Report without Donuts and CreditCard Transactions</th></tr><tr><th>Month</th><th>Year</th><th>Spent</th><th>Income</th>");
    	for(Map.Entry<Integer, Node[][]> entry : transactionsTemp.entrySet()) {
			  long income = 0L;
			  long expense = 0L;
			  long totalIncome = 0L;
			  long totalExpense = 0L;
			  int totalIncomeCnt = 0;
			  int totalExpenseCnt = 0;
			  int incomeCnt=0;
			  int expenseCnt=0;
			  Node transTmp = null;
			  Node incomeCC = null;
			  Node expenseCC = null;
			  for(int i = 0; i < 12; i++){
				  if(entry.getValue()[0][i] != null){
					  transTmp = entry.getValue()[0][i];
					  while(transTmp != null){
						  if(transTmp.getMerchant().equals("CC Payment")){
							  ccList.add(transTmp);
							  expenseCC = transTmp;
						  }
						  if(!transTmp.getMerchant().equals("Krispy Kreme Donuts") && !transTmp.getMerchant().equals("DUNKIN #336784")){
							  expense += transTmp.getAmount();
							  expenseCnt++;
						  }
						  transTmp = transTmp.next;
					  }
				  }
				  if(entry.getValue()[1][i] != null){
					  transTmp = entry.getValue()[1][i];
					  while(transTmp != null){
						  if(transTmp.getMerchant().equals("Credit Card Payment")){
							  ccList.add(transTmp);
							  incomeCC = transTmp;
						  }
						  income += transTmp.getAmount();
						  incomeCnt++;
						  transTmp = transTmp.next;
					  }
				  }
				  if(incomeCnt != 0 || expenseCnt != 0) {
					  if(expenseCC != null && incomeCC != null){
					    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					    	try{
					    		Date d1 = format.parse(expenseCC.getTransactionTime().substring(0, 10) + " " + expenseCC.getTransactionTime().substring(11, 19));
					    		Date d2 = format.parse(incomeCC.getTransactionTime().substring(0, 10) + " " + incomeCC.getTransactionTime().substring(11, 19));
					    		long diff;
					    		if(d1.compareTo(d2) > 0){
					    			diff = d1.getTime() - d2.getTime();
					    		}
					    		else{
					    			diff = d2.getTime() - d1.getTime();
					    		}
					    		long diffDays = diff / (24 * 60 * 60 * 1000);
					    		long diffHours = diff / (60 * 60 * 1000) % 24;
					    		if(diffHours >= 0 && diffHours <= 24 && diffDays <= 1){
					    			incomeCnt--;
					    			expenseCnt--;
					    			income -= incomeCC.getAmount();
					    			expense -= expenseCC.getAmount();
					    		}
					    	}
					    	catch(Exception e){
					    		e.printStackTrace();
					    	}
					  }
					  int month = i+1;
					  sb.append("<tr><td>"+month+"</td><td>"+entry.getKey()+"</td><td>$"+NumberFormat.getNumberInstance(Locale.US).format(expense*-1)+"</td><td>$"+NumberFormat.getNumberInstance(Locale.US).format(income)+"</td></tr>");
					  if(incomeCnt != 0 && expenseCnt != 0){
						  sb.append("<tr><th colspan=2>Average (per Transaction) Total number of Spent/Income Transaction: " + expenseCnt + "/" + incomeCnt + "</th><td>$"+NumberFormat.getNumberInstance(Locale.US).format((expense*-1)/expenseCnt)+"</td><td>$"+NumberFormat.getNumberInstance(Locale.US).format(income/incomeCnt)+"</td></tr>");
					  }
					  else if (incomeCnt == 0){
						  sb.append("<tr><th colspan=2>Average (per Transaction) Total number of Spent/Income Transaction: " + expenseCnt + "/" + incomeCnt + "</th><td>$"+NumberFormat.getNumberInstance(Locale.US).format((expense*-1)/expenseCnt)+"</td><td>$0</td></tr>");
					  }
					  else if(expenseCnt == 0){
						  sb.append("<tr><th colspan=2>Average (per Transaction) Total number of Spent/Income Transaction: " + expenseCnt + "/" + incomeCnt + "</th><td>$0</td><td>$"+NumberFormat.getNumberInstance(Locale.US).format(income/incomeCnt)+"</td></tr>");
					  }
					  totalIncomeCnt += incomeCnt;
					  totalExpenseCnt += expenseCnt;
					  totalIncome += income;
					  totalExpense += expense;
					  income = 0L;
					  expense = 0L;
					  expenseCnt = 0;
					  incomeCnt = 0;
				  }
			  }
			  sb.append("<tr><th colspan=2>Average (per Transaction) for "+entry.getKey()+ " Total number of Spent/Income Transaction: " + totalExpenseCnt + "/" + totalIncomeCnt + "</th><td>$"+NumberFormat.getNumberInstance(Locale.US).format((totalExpense*-1)/totalExpenseCnt)+"</td><td>$"+NumberFormat.getNumberInstance(Locale.US).format(totalIncome/totalIncomeCnt)+"</td></tr>");
			  totalIncomeCnt = 0;
			  totalExpenseCnt = 0;
			  totalIncome = 0L;
			  totalExpense = 0L;
		}
    	sb.append("</table><table border=1><tr><th colspan=2>Credit Card Payment transaction</th></tr><tr><th>Amount</th><th>Date</th></tr>");
    	Node ccTmp = null;
    	Node ccTmp1 = null;
    	int q = 0;
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	while(q < ccList.size()){
    		ccTmp = ccList.get(q);
    		if(q+1 >= ccList.size()) break;
    		ccTmp1 = ccList.get(q+1);
    		try{
    			Date d1 = format.parse(ccTmp.getTransactionTime().substring(0, 10) + " " + ccTmp.getTransactionTime().substring(11, 19));
    			Date d2 = format.parse(ccTmp1.getTransactionTime().substring(0, 10) + " " + ccTmp1.getTransactionTime().substring(11, 19));
    			long diff;
    			if(d1.compareTo(d2) > 0){
    				diff = d1.getTime() - d2.getTime();
    			}
    			else{
    				diff = d2.getTime() - d1.getTime();
    			}
    			long diffDays = diff / (24 * 60 * 60 * 1000);
    			long diffHours = diff / (60 * 60 * 1000) % 24;
    			if(diffHours >= 0 && diffHours <= 24 && diffDays <= 1){
    				sb.append("<tr><td>$" + ccTmp.getAmount() + "</td><td>Transaction DateTimes: " + d1.toString() + " and " + d2.toString()+"</td></tr>");
        			q += 2;
    			}
    			else{
    				q++;
    			}
    		}
    		catch(Exception e){
    			e.printStackTrace();
    		}
    		
    	}
    	sb.append("</table></body></html>");
    	return sb.toString();
    }
    
    // Create report page with action
	public CreateWindow(String title, TreeMap<Integer, Node[][]> transactionsTempMap){
		super(title);
        setLayout(new FlowLayout());
        addWindowListener(this);
        submit = new JButton("Submit");
        checkDonuts = new JCheckBox("Ignore Donuts transaction");
        checkCCpayment = new JCheckBox("Ignore Credit Card Payment");
        checkCrystalBall = new JCheckBox("Build projected transactions");
        JLabel yearLabel = new JLabel("Year:");
        JLabel monthLabel = new JLabel("Month:");
        crystalBallYear = new JTextField(4);
        crystalBallMonth = new JTextField(2);
        add(checkDonuts);
        add(checkCCpayment);
        add(checkCrystalBall);
        add(yearLabel);
        add(crystalBallYear);
        add(monthLabel);
        add(crystalBallMonth);
        add(submit);
        submit.addActionListener(this);
        transactionsTemp = transactionsTempMap;
	}
	 public void actionPerformed(ActionEvent e) {
		 JFrame reportFrame = new JFrame("Transaction Report");
		 JTextPane reportPane = new JTextPane();
		 reportPane.setEditable(false);
		 reportPane.setContentType("text/html");
		 
		 if(checkCrystalBall.isSelected()){
			 JFrame ptransFrame = new JFrame("Projected Transactions for Month");
			 JTextPane ptransPane = new JTextPane();
			 ptransPane.setEditable(false);
			 ptransPane.setContentType("text/html");
			 try{
				 long year = Long.parseLong(crystalBallYear.getText());
				 long month = Long.parseLong(crystalBallMonth.getText());
				 if(year > 2020L || year < 2000L || month > 12L || month < 1L){
					 checkCrystalBall.setSelected(false);
					 crystalBallYear.setText("");
					 crystalBallMonth.setText("");
					 JOptionPane.showConfirmDialog(null, "Incorrect Year/Month", "Warning", JOptionPane.CANCEL_OPTION);
				 }
				 else{
					 projectTransactions pTrans = new projectTransactions(year, month, transactionsTemp);
					 ptransPane.setText(pTrans.createpTransHTML());
					 JScrollPane ptranScroll = new JScrollPane(ptransPane);
					 ptranScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
					 ptransFrame.add(ptranScroll);
					 ptransFrame.setSize(500, 300);
					 ptransFrame.setVisible(true);
				 }
			 }
			 catch(NumberFormatException inputException){
				 checkCrystalBall.setSelected(false);
				 JOptionPane.showConfirmDialog(null, "Please enter numbers only in Year and Month", "Warning", JOptionPane.CANCEL_OPTION);
			 }
		 }
		 if(checkDonuts.isSelected() && checkCCpayment.isSelected()){
			 reportPane.setText(createWithoutCCNDonutsHTML());
		 }
		 else if(checkDonuts.isSelected()){
			 reportPane.setText(createWithoutDonutsHTML());
		 }
		 else if(checkCCpayment.isSelected()){
			reportPane.setText(createWithoutCCHTML()); 
		 }	
		 else{
			 reportPane.setText(createAllHTML());
		 }
		 JScrollPane scroll = new JScrollPane(reportPane);
		 scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		 reportFrame.add(scroll);
		 reportFrame.setSize(800, 600);
		 reportFrame.setVisible(true);
	 }

	 public void windowClosing(WindowEvent e) {
         dispose();
         System.exit(0);
	 }

	 public void windowOpened(WindowEvent e) {}
	 public void windowActivated(WindowEvent e) {}
	 public void windowIconified(WindowEvent e) {}
	 public void windowDeiconified(WindowEvent e) {}
	 public void windowDeactivated(WindowEvent e) {}
	 public void windowClosed(WindowEvent e) {}
}
