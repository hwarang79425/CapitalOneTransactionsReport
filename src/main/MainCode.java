/*
 * Main code
 * it create new object and connect to URL to pull all transactions
 * and store pulled data in TreeMap -> Array
 * 
 */
package main;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import util.CreateWindow;
import util.GetData;
import util.Node;

public class MainCode {
	public static void main(String[] args) {
		
		GetData getTransaction = new GetData("GET_ALL");
		TreeMap<Integer, Node[][]> mappedTransaction = getTransaction.GetAllData();
		try{
			CreateWindow transactionWindow = new CreateWindow("Welcome to transaction report page", mappedTransaction);
			transactionWindow.setSize(850,100);
			transactionWindow.setVisible(true);
		}
		catch(Exception frameException){
			// Use terminal to print;
			for(Map.Entry<Integer, Node[][]> entry : mappedTransaction.entrySet()) {
				  long income = 0L;
				  long expense = 0L;
				  Node transTmp = null;
				  for(int i = 0; i < 12; i++){
					  if(entry.getValue()[0][i] != null){
						  transTmp = entry.getValue()[0][i];
						  while(transTmp != null){
							  expense += transTmp.getAmount();
							  transTmp = transTmp.next;
						  }
					  }
					  if(entry.getValue()[1][i] != null){
						  transTmp = entry.getValue()[1][i];
						  while(transTmp != null){
							  income += transTmp.getAmount();
							  transTmp = transTmp.next;
						  }
					  }
					  if(income != 0L && expense != 0L) System.out.println(i+1 + "/" + entry.getKey() + ": spent=$" + NumberFormat.getNumberInstance(Locale.US).format(expense*-1) + " income=$" + NumberFormat.getNumberInstance(Locale.US).format(income));
					  income = 0L;
					  expense = 0L;
				  }
			}	
		}
	}
}
