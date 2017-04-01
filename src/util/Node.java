/*
 *  Node object to store each transaction
 *  
 */

package util;

public class Node {
	public Node next;
	protected long amount;
	protected boolean isPending;
	protected long aggTime;
	protected String accID;
	protected long clrDate;
	protected String tranID;
	protected String rMerchant;
	protected String categ;
	protected String merchant;
	protected String trantime;
	
	Node(long amt, boolean ispending, long aTime, String acctid, long cleardate, String transid, String rmer, String cat, String mer, String transtime){
		next = null;
		amount = amt;
		isPending = ispending;
		aggTime = aTime;
		accID = acctid;
		clrDate = cleardate;
		tranID = transid;
		rMerchant = rmer;
		categ = cat;
		merchant = mer;
		trantime = transtime;	
	}
	
	// update values method
	public void updateAmount(long amt){ amount = amt; }
	public void updatePendingStatus(boolean pending){ isPending = pending; }
	public void updateAggregateTime(long aggtime){ aggTime = aggtime; }
	public void updateAccountID(String acctid) { accID = acctid; }
	public void updateClearDate(long cleardate) { clrDate = cleardate; }
	public void updateTransactionID(String transid) { tranID = transid; }
	public void udpateRawMerchant(String rmerchant) { rMerchant = rmerchant; }
	public void updateCategorization(String cat) { categ = cat; }
	public void updateMerchant(String mer) { merchant = mer; }
	public void updateTransactionTime(String transactiontime) { trantime = transactiontime; }
	
	// remove values method
	public long removeAmount(){ long tmp = amount; amount = 0; return tmp;}
	public long removeAggregateTime(){ long tmp = aggTime; aggTime=0; return tmp; }
	public String removeAccountID() { String tmp = accID; accID = null; return tmp; }
	public long removeClearDate() { long tmp = clrDate; clrDate = 0; return tmp; }
	public String removeTransactionID() { String tmp = tranID; tranID = null; return tmp; }
	public String removeRawMerchant() { String tmp = rMerchant; rMerchant = null; return tmp; }
	public String removeCategorization() { String tmp = categ; categ = null; return tmp; }
	public String removeMerchant() { String tmp = merchant; merchant = null; return tmp; }
	public String removeTransactionTime() { String tmp = trantime; trantime = null; return tmp;}

	// get values method
	public long getAmount(){ return amount; }
	public boolean getPendingStatus(){ return isPending; }
	public long getAggregateTime(){ return aggTime; }
	public String getAccountID() { return accID; }
	public long getClearDate() { return clrDate; }
	public String getTransactionID() { return tranID; }
	public String getRawMerchant() { return rMerchant; }
	public String getCategorization() { return categ; }
	public String getMerchant() { return merchant; }
	public String getTransactionTime() { return trantime; }
	
	
	// get customized value
	public int getTransYear(){ return Integer.parseInt(trantime.substring(0, 4)); }
	public int getTransMonth() { return Integer.parseInt(trantime.substring(5, 7)); }
	public int getTransDay() { return Integer.parseInt(trantime.substring(8, 10)); }
}
