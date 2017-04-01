/*
 * if it is for old data < current month/day/year, then use existing TreeMap to pull data.
 * if it is for projection > current month/day/year, connect to project URL and pull the data and store them in Node Array
 * 
 */
package util;


import java.util.Calendar;
import java.util.TreeMap;

public class projectTransactions {

	protected TreeMap<Integer, Node[][]> transactionsTemp;
	protected long year;
	protected long month;
	
	public projectTransactions(long yr, long mth, TreeMap<Integer, Node[][]> transactionsTempMap){
		transactionsTemp = transactionsTempMap;
		year = yr;
		month = mth;
	}
	private String pullHistoricData(int iMonth, int iYear){
		StringBuilder sb = new StringBuilder("<meta http-equiv='Content-Type' content='text/html; charset=utf-8'><html><head><title></title></head><body><table border=1><tr><th colspan=2> Projected Transaction Report for " + iMonth + "/"+ iYear +"</th></tr><tr><th>Merchant</th><th>Spent</th></tr>");
  
		Node transTmp = null;
		transTmp = transactionsTemp.get(iYear)[0][iMonth-1];
		while(transTmp != null){
			sb.append("<tr><td>" + transTmp.getMerchant() + "</td><td>$" + transTmp.getAmount() + "</td></tr>");
			transTmp = transTmp.next;
		}
		sb.append("<tr><th>Merchant</th><th>Income</th></tr>");
		transTmp = transactionsTemp.get(iYear)[1][iMonth-1];
		while(transTmp != null){
			sb.append("<tr><td>" + transTmp.getMerchant() + "</td><td>$" + transTmp.getAmount() + "</td></tr>");
			transTmp = transTmp.next;
		}
    	sb.append("</table></body></html>");
    	return sb.toString();
	}
	public String createpTransHTML(){
		StringBuilder sb = null;
		Calendar now = Calendar.getInstance();
		int intMonth = (int) month;
		int intYear = (int) year;
		GetData getProjection = null;
		Node[] monthTmp = null;
		Node expenseTmp = null;
		Node incomeTmp = null;
		sb = new StringBuilder("<meta http-equiv='Content-Type' content='text/html; charset=utf-8'><html><head><title></title></head><body><table border=1><tr><th colspan=2> Projected Transaction Report for " + month + "/" + year + "</th></tr><tr><th>Merchant</th><th>Spent</th></tr>");
		if(now.get(Calendar.YEAR) >= year){
			if(now.get(Calendar.YEAR) == year && now.get(Calendar.MONTH) <= month){
				getProjection = new GetData("GET_PROJ", intYear, intMonth);
				monthTmp = getProjection.GetMonthData();
				expenseTmp = monthTmp[0];
				incomeTmp = monthTmp[1];
				while(expenseTmp != null){
					if(now.get(Calendar.MONTH) == month){
						if(now.get(Calendar.DATE) < expenseTmp.getTransDay()){
							sb.append("<tr><td>"+ expenseTmp.getMerchant() + "</td><td>$"+ expenseTmp.getAmount() + "</td></tr>");
						}
					}
					else{
						sb.append("<tr><td>"+ expenseTmp.getMerchant() + "</td><td>$"+ expenseTmp.getAmount() + "</td></tr>");
					}
					expenseTmp = expenseTmp.next;
				}
				sb.append("<tr><th>Merchant</th><th>Income</th></tr>");
				while(incomeTmp != null){
					if(now.get(Calendar.MONTH) == month){
						if(now.get(Calendar.DATE) < incomeTmp.getTransDay()){
							sb.append("<tr><td>"+ incomeTmp.getMerchant() + "</td><td>$"+ incomeTmp.getAmount() + "</td></tr>");
						}
					}
					else{
						sb.append("<tr><td>"+ incomeTmp.getMerchant() + "</td><td>$"+ incomeTmp.getAmount() + "</td></tr>");
					}
					incomeTmp = incomeTmp.next;
				}
			}			
			else{
				if(transactionsTemp.get(intYear) == null || (transactionsTemp.get(intYear)[0][intMonth-1] == null && transactionsTemp.get(intYear)[1][intMonth-1] == null)){
					return "<meta http-equiv='Content-Type' content='text/html; charset=utf-8'><html><head><title></title></head><body><table border=1><tr><th colspan=2> Projected Transaction Report for " + month + "/" + year + "</th></tr><tr><th>Spent</th><th>Income</th></tr><tr><td colspan=2>Not Enough historic data</td></tr></table></body></html>";
			    	
				}
				else{
					return pullHistoricData(intMonth, intYear);
				}
			}
		}
		else{
			getProjection = new GetData("GET_PROJ", intYear, intMonth);
			monthTmp = getProjection.GetMonthData();
			expenseTmp = monthTmp[0];
			incomeTmp = monthTmp[1];
			while(expenseTmp != null){
				sb.append("<tr><td>"+ expenseTmp.getMerchant() + "</td><td>$"+ expenseTmp.getAmount() + "</td></tr>");
				expenseTmp = expenseTmp.next;
			}
			sb.append("<tr><th>Merchant</th><th>Income</th></tr>");
			while(incomeTmp != null){
				sb.append("<tr><td>"+ incomeTmp.getMerchant() + "</td><td>$"+ incomeTmp.getAmount() + "</td></tr>");
				incomeTmp = incomeTmp.next;
			}
		}
		sb.append("</table></body></html>");
		return sb.toString();
	}
}
