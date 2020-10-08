package kickstart.TelegramInterface;

import kickstart.articles.Article;
import kickstart.Micellenious.ReorderableInventoryItem;
import org.salespointframework.catalog.Product;
import org.salespointframework.inventory.Inventory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Scanner;


@Service
public class  BotManager {

	private Map<ReorderableInventoryItem,Boolean> allreadyInformed = new HashMap<>();
    private  final String CHAT_ID_ME = "374013262";
	private  final String CHAT_ID_KARSTEN = "";
    private String message="Connected";
    private  final String BOT_URL_IP =
            "https://api.telegram.org/bot795371834:AAF3Pq_kTgdIxh0gpI6Ny4uWozhCvf-_PPE/";

    public BotManager(Inventory<ReorderableInventoryItem> inventory) {
		for (ReorderableInventoryItem item:inventory.findAll()){
			allreadyInformed.put(item,false);
		}
    }

	public boolean checkItemsForCriticalAmount(Inventory<ReorderableInventoryItem> inventory) {
    	String message="";
    	for (ReorderableInventoryItem item:inventory.findAll()){
			if (item==null){
				System.out.println("MY ERROR: item darf nicht null sein");
				return false;
			}
			Article article=null;
			Product product =item.getProduct();
			if (product instanceof Article){
				article=((Article)product);
			}
			else {
				throw new IllegalArgumentException("Produkt not an article");
			}
			int differenz=(int)item.getArticle().getCriticalAmount() -item.getAmountBwB();
			if (article.getCriticalAmount()>item.getAmountBwB()&&allreadyInformed.containsKey(item)&&!allreadyInformed.get(item)){

				//sendMessage("low");
				message=item.getProduktName().toString()+" ist kritisch mit "+item.getAmountBwB()+item.getUnitQuant()
						+ ", was "+differenz+item.getUnitQuant()+ " unterhalb der Kritischen Menge "+item.getArticle().getCriticalAmount()+ " liegt.";
				sendMessage(message);

				allreadyInformed.put(item,true);

				continue;
			}
			if (article.getCriticalAmount()<item.getAmountBwB()&&allreadyInformed.containsKey(item)&&allreadyInformed.get(item)) {
				allreadyInformed.put(item,false);
				continue;
			}

		}
		return false;
	}


	private boolean sendMessage(String	message){
		try {
			URL urlBot = new URL(
					getIpBotURL()
							+ getURL_SendMessage(getCHAT_ID_ME(), message));
			URLConnection urlConnection = urlBot.openConnection();
			urlConnection.getInputStream();
			return true;

		} catch (Exception e) {
			System.out.println("Error " + e.getMessage());
			return  false;
		}

	}

	public boolean criticalAmountAfterUndoCheck(ReorderableInventoryItem item,int redoAmount){
		if (item==null){
			System.out.println("MY ERROR: item darf nicht null sein");
			return false;
		}
		Article article=null;
		Product product =item.getProduct();
		if (product instanceof Article){
			article=((Article)product);
		}
		else {
			throw new IllegalArgumentException("Produkt not an article");
		}

		if (article.getCriticalAmount()>item.getAmountBwB()){
			//	botItemLowAmount(item);
			return true;
		}
		return false;
	}



	//<editor-fold desc="Code for Reference">
	public String getPhysicalIp(){
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            System.out.println("IP Address:- " + inetAddress.getHostAddress());
            return        "IP Address:- " + inetAddress.getHostAddress();
        }catch (Exception e){
            System.out.println("MyError: Unknown Host");
        }


       return null;
    }

    public String getInternetIp() {

        return "141.30.224.254";
    }

    public String getHostName(){
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            System.out.println("Host Name:- " + inetAddress.getHostName());
            return   "Host Name:- " + inetAddress.getHostName()    ;
        }catch (Exception e){
            System.out.println("MyError: Unknown Host");
        }
       return null;
    }

    public String getIpBotURL(){
        return BOT_URL_IP;
    }

    public String getURL_SendMessage(String chat_ID, String message) {
        return "sendMessage?chat_id="+chat_ID+"&text="+message;
    }

    public String getCHAT_ID_ME(){
        return CHAT_ID_ME;
    }

    public void printRequest(URLConnection urlConnection){
        try{
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            urlConnection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                System.out.println(inputLine);
            in.close();
        }catch (Exception e){
            System.out.println("Error While printing in console");
        }

    }

    public static final String JSON_FILE="employee.txt";

    /*
    * public void getJsonObject() throws IOException {

        JSONObject obj = new JSONObject(" .... ");
        String pageName = obj.getJSONObject("pageInfo").getString("pageName");

        JSONArray arr = obj.getJSONArray("posts");
        for (int i = 0; i < arr.length(); i++)
        {
            String post_id = arr.getJSONObject(i).getString("post_id");

        }

    }
    *

    public int displayFbRequest(){
            String manschaft1="Hamburger SV";
            String manschaft2="Eintracht Frankfurt";
            int result=0;
            int bundesliga=1;
            boolean alleBundesLigaspiele=false;
            int jahr=2018;
            int uberXJahr=2;

            int matchcount=0;
            for (int i=0;i<uberXJahr;i++) {
                int loopingJear=jahr-i;
                String apiRequestString = "https://www.openligadb.de/api/getmatchdata/bl" + bundesliga + "/" + loopingJear;

                //<editor-fold desc="Fetch Api to Object">
                Scanner scan = scan(apiRequestString);
                String str = new String();
                while (scan.hasNext())
                    str += scan.nextLine();
                System.out.println("\nJSON data in string format");
                System.out.println(str);
                scan.close();

                Gson g = new Gson();
                FB_Data[] obj2 = g.fromJson(str, FB_Data[].class);
                //</editor-fold>

               //Für jedes Match in der Liga und dem Jahr
                for (FB_Data data :
                        obj2) {
                    //prüfe ob die manschaften die gesuchten sind
                    if ((data.team1.TeamGroupName.equals(manschaft1)&&data.team2.TeamGroupName.equals(manschaft2))
                        ||data.team1.TeamGroupName.equals(manschaft2)&&data.team2.TeamGroupName.equals(manschaft1)) {
                        // summiere alle gefallenen Tore
                        result+= data.Goals.size();
                        //und zähle die matches zwischen den manschaften
                        matchcount++;
                    }
                }
            }
            //bilde das arithmetische mittel
            result=result/matchcount;


            System.out.println("Matches Found: "+matchcount);
            System.out.println("RESULT: "+result);
            return result;
    }
*/
    private Scanner scan(String apiRequestString) {

        Scanner scan;
        try{
            URL url;
            url = new URL(apiRequestString);
            HttpURLConnection conn =(HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responsecode=conn.getResponseCode();
            if(responsecode != 200)
                throw new RuntimeException("HttpResponseCode: " +responsecode);
            scan = new Scanner(url.openStream());
        }catch (Exception e){
            throw new IllegalArgumentException("IO Exception");
        }
        return scan;
    }




	//</editor-fold>


}


