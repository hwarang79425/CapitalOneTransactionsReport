/*
 * Connect to URL and pull all transactions or projected transactions
 * create new TreeMap which points to array and store pulled data
 * 
 */
package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Properties;
import java.util.TreeMap;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONObject;


public final class GetData {
	// Connect to URL using the endpoint value. And send and receive the data.
	protected HttpsURLConnection conn = null;
	protected String uid;
	protected String token;
	protected String api_token;
	protected boolean json_strict_mode;
	protected boolean json_verbose_response;
	protected String url;
	protected TreeMap<Integer, Node[][]> transactionsTable;
	protected String URLParameter;
	
	// connect to URL and return result
	protected HttpsURLConnection connectAndRetrieve(){
		try{
			// Set TLS connection
			SSLContext sc = SSLContext.getInstance("TLSv1.2");
			sc.init(null, null, new java.security.SecureRandom());
			
			// Connect to the Site to pull data
			URL urlObject = new URL(url);
			conn = (HttpsURLConnection) urlObject.openConnection();
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestMethod("POST");
			conn.setSSLSocketFactory(sc.getSocketFactory());
			OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
			// Pass argument
			
			out.write(URLParameter);
			out.flush();
			out.close();
			if(conn.getResponseCode() != 200) {
				JOptionPane.showMessageDialog(null, "Failed to connect to the site");
				return null;
			}
		}
		catch (Exception connectException){
			connectException.printStackTrace();
		}
		return conn;
	}
	
	// setup TreeMap and store pulled transactions
	private void setupHashMap(int length, JSONObject jsonObj){
		transactionsTable = new TreeMap<Integer, Node[][]>();
		JSONArray jArray = jsonObj.getJSONArray("transactions");
		JSONObject tmp;
		int year, month;
		boolean expense;
		for(int i = 0; i < length; i++){
			tmp = jArray.getJSONObject(i);
			year = Integer.parseInt(tmp.getString("transaction-time").substring(0, 4));
			month = Integer.parseInt(tmp.getString("transaction-time").substring(5, 7));
			if(tmp.getLong("amount") < 0) expense = true;
			else expense = false;
			Node nodeTmp = new Node(tmp.getLong("amount"), tmp.getBoolean("is-pending"), tmp.getLong("aggregation-time"), tmp.getString("account-id"), tmp.getLong("clear-date"), tmp.getString("transaction-id"), tmp.getString("raw-merchant"), tmp.getString("categorization"), tmp.getString("merchant"), tmp.getString("transaction-time"));
			
			if(transactionsTable.get(year) == null){
				Node[][] monthTmp = new Node[2][12];
				if(expense){ monthTmp[0][month-1] = nodeTmp; }
				else { monthTmp[1][month-1] = nodeTmp; }
				transactionsTable.put(year, monthTmp);
			}
			else{
				Node[][] monthTmp = transactionsTable.get(year);
				Node nodePointer;
				if(expense){
					if(monthTmp[0][month-1] == null){
						monthTmp[0][month-1] = nodeTmp;
						transactionsTable.put(year, monthTmp);
					}
					else{
						nodePointer = monthTmp[0][month-1];
						while(nodePointer.next != null){
							nodePointer = nodePointer.next;
						}
						nodePointer.next = nodeTmp;
					}
				}
				else{
					if(monthTmp[1][month-1] == null){
						monthTmp[1][month-1] = nodeTmp;
						transactionsTable.put(year, monthTmp);
					}
					else{
						nodePointer = monthTmp[1][month-1];
						while(nodePointer.next != null){
							nodePointer = nodePointer.next;
						}
						nodePointer.next = nodeTmp;
					}
				}
			}
		}
	}
	// filter the result
	public TreeMap<Integer, Node[][]> GetAllData(){
		try{
			HttpsURLConnection conResult = connectAndRetrieve();			
			// Read the result/transactions
			if(conResult != null){
				BufferedReader streamReader = new BufferedReader(new InputStreamReader(conResult.getInputStream(), "UTF-8")); 
				String responseStr = streamReader.readLine();
				JSONObject jo = new JSONObject(responseStr);				
				setupHashMap(jo.getJSONArray("transactions").length(), jo);
					
				streamReader.close();
			}

		}
		catch (Exception e){
			e.printStackTrace();
		}
		return transactionsTable;
	}
	public Node[] GetMonthData(){
		Node[] monthNodes = new Node[2];
		try{
			HttpsURLConnection conMonthResult = connectAndRetrieve();
			Node monthNode = null;
			JSONObject monthTmp = null;
			int q = 0;
			if(conMonthResult != null){
				BufferedReader streamMonthReader = new BufferedReader(new InputStreamReader(conMonthResult.getInputStream(), "UTF-8")); 
				String responseMonthStr = streamMonthReader.readLine();
				JSONObject joMonth = new JSONObject(responseMonthStr);
				JSONArray jMonthArray = joMonth.getJSONArray("transactions");
				int length = joMonth.getJSONArray("transactions").length();
				for(int i = 0; i < length; i++){
					monthTmp = jMonthArray.getJSONObject(i);
					if(monthTmp.getLong("amount") < 0){ q = 0;}
					else { q = 1; }
					if(monthNodes[q] == null){
						monthNodes[q] = new Node(monthTmp.getLong("amount"), monthTmp.getBoolean("is-pending"), monthTmp.getLong("aggregation-time"), monthTmp.getString("account-id"), monthTmp.getLong("clear-date"), monthTmp.getString("transaction-id"), monthTmp.getString("raw-merchant"), monthTmp.getString("categorization"), monthTmp.getString("merchant"), monthTmp.getString("transaction-time"));
					}
					else{
						monthNode = monthNodes[q];
						while(monthNode.next != null){
							monthNode = monthNode.next;
						}
						monthNode.next = new Node(monthTmp.getLong("amount"), monthTmp.getBoolean("is-pending"), monthTmp.getLong("aggregation-time"), monthTmp.getString("account-id"), monthTmp.getLong("clear-date"), monthTmp.getString("transaction-id"), monthTmp.getString("raw-merchant"), monthTmp.getString("categorization"), monthTmp.getString("merchant"), monthTmp.getString("transaction-time"));
					}
				}
				streamMonthReader.close();
			}
		}
		catch (Exception connectException){
			JOptionPane.showConfirmDialog(null, "No values to predict", "Warning", JOptionPane.CANCEL_OPTION);
		}
		return monthNodes;
	}
	public GetData(String whichURL){
		try {
			// Get configuration file and load it.
			Properties prop = new Properties();
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			prop.load(loader.getResourceAsStream("config.properties"));
			uid = prop.getProperty("UID");
			token = prop.getProperty("TOKEN");
			api_token = prop.getProperty("API_TOKEN");
			json_strict_mode = Boolean.valueOf(prop.getProperty("JSON_STRICT_MODE"));
			json_verbose_response = Boolean.valueOf(prop.getProperty("JSON_VERBOSE_RESPONSE"));
			url = prop.getProperty(whichURL);
			URLParameter = "{\"args\": {\"uid\":  " + uid + ", \"token\":  \"" + token + "\", \"api-token\":  \"" + api_token +
					"\", \"json-strict-mode\":" + json_strict_mode + ", \"json-verbose-response\":" + json_verbose_response + "}}";
		}catch (IOException configLoadException) {
			// TODO Auto-generated catch block
			configLoadException.printStackTrace();
		}
	}
	public GetData(String whichURL, int iYear, int iMonth){
		try {
			// Get configuration file and load it.
			Properties prop = new Properties();
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			prop.load(loader.getResourceAsStream("config.properties"));
			uid = prop.getProperty("UID");
			token = prop.getProperty("TOKEN");
			api_token = prop.getProperty("API_TOKEN");
			json_strict_mode = Boolean.valueOf(prop.getProperty("JSON_STRICT_MODE"));
			json_verbose_response = Boolean.valueOf(prop.getProperty("JSON_VERBOSE_RESPONSE"));
			url = prop.getProperty(whichURL);
			URLParameter = "{\"args\": {\"uid\":  " + uid + ", \"token\":  \"" + token + "\", \"api-token\":  \"" + api_token +
						"\", \"json-strict-mode\":" + json_strict_mode + ", \"json-verbose-response\":" + json_verbose_response + "}, \"year\":" + iYear +", \"month\":" + iMonth + "}";		
		}catch (IOException configLoadException) {
			// TODO Auto-generated catch block
			configLoadException.printStackTrace();
		}
	}
}
