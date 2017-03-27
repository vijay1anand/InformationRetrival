import java.io.*;
import java.util.*;

class TextFile implements Indexing{
    HashMap<String, Integer> freq = new HashMap<String, Integer>();  //Store frequency of unique stemmed words in file
    HashMap<String, Integer> stopmap = new HashMap<String, Integer>(); //List of stopword
    HashMap<String, ArrayList<Integer>> invertedindex = new HashMap<String, ArrayList<Integer>>();
	//Storing index of document where token is present
    int document_id=0;//for giving id to documents
    FileReader fr=null;
    BufferedReader br=null;
    FileReader fe=null;
    BufferedReader be=null;
    
//Taking stopword from list and storing in stopmap	
 void StoreStopWord(){         
		      try {  fe = new FileReader("stoplist.txt"); 
		       be = new BufferedReader(fe);
                      String sr;
			
                      while ((sr = be.readLine()) != null){
                             stopmap.put(sr,1);
                             //System.out.println(sr);
    			}
                     }catch (IOException e) {

			        e.printStackTrace();

		} 
   try {

				if (be != null)
					be.close();

				if (fe != null)
					fe.close();
				
       }
        catch (IOException ex) {

				ex.printStackTrace();

			  }

}
//reading files	
public void fileread(String FILENAME){
       try {  
           fr = new FileReader(FILENAME); 
	   br = new BufferedReader(fr);
           document_id++;

         String sCurrentLine;

	  br = new BufferedReader(fr);
		     String fname[]=FILENAME.split("/");
                 System.out.println(fname[1]+"  doc id = "+document_id);
                 int inside=0;  
			     int sLine;
		         String word="";
			while ((sLine = br.read()) != -1) {
				     				    char ch=(char)sLine;
				                   if(sLine==10 || sLine==32){
										  int l=word.length();
										  String temp=word.substring(0,l);
										   word="";
								    
                                     if(temp.contains("<title>")){
				                   inside=1;
                                                   String sr[]=temp.split(">");
                                                   if(sr.length>1)
                                                   CountWords(sr[1]);
		                                   
                                                  continue;
                                                }
									 if(temp.contains("</title>")){
										   inside=0;
                                                   String sr[]=temp.split("<");
                                                   CountWords(sr[0]);
                                                }	  
                                      if(temp.contains("<content>")){
						                   inside=1;
                                           continue;
 					                    }
                                      if(temp.contains("</content>")){
												inside=0;
                                                break;
 										}
                                      if(inside==1){
										       
										     if(temp.length()>1){
                                             		temp=RemovePanctuation(temp);
          				                     		CountWords(temp);
											 }	 
                                        }
									  } 
				                   else{
								         word += ch;
								    }
                                     
                                  }
        
      } catch (IOException e) {

			        e.printStackTrace();

		} 
   try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();
				
       }
        catch (IOException ex) {

				ex.printStackTrace();

			}
  }
boolean StopWord(String st){
   
    
   if(stopmap.containsKey(st)){
      //System.out.println("StopWord "+st);
     return true;
    }
   else 
    return false;    
}
 void CountWords(String sr){
         String a[]=sr.split(" ");
         
         for(int i=0;i<a.length;i++) {
                                              
                                       int len=a[i].length();
                                        if(!StopWord(a[i])){
					                     HindiStemmer hobj=new HindiStemmer();
                                         int end=hobj.stem(a[i]);
										  String token=a[i].substring(0,end);
                                          int count = freq.containsKey(token) ? freq.get(token) : 0;
                                          freq.put(token, count + 1);
										  InvertedList(token,document_id);
										 /*int count = freq.containsKey(a[i]) ? freq.get(a[i]) : 0;
                                          freq.put(a[i], count + 1);
										   Store word without Stemming
										  */
                                      }
              
  }
 }
void InvertedList(String token,int doc_id){
       ArrayList<Integer> docList = invertedindex.get(token);
     if(docList == null) {
         docList = new ArrayList<Integer>();
         docList.add(doc_id);
         invertedindex.put(token,docList);
    } else {
        // add if item is not already in list
        if(!docList.contains(doc_id)) docList.add(doc_id);
    }

}
 void CloseFile(FileWriter wct){
     try {


				if (wct != null)
					 wct.close();
				
        }
        catch (IOException ex) {

				ex.printStackTrace();

			  }
 }
String RemovePanctuation(String temp){
   if(temp.contains("।।") ||temp.contains("-") || temp.contains("॥") || temp.contains(":")|| temp.contains("।")|| temp.contains(",")|| temp.contains("!")|| temp.contains("?")){
                                                   temp=temp.replace("॥"," ");
						   temp=temp.replace(":"," ");
                                                   temp=temp.replace("।"," ");
                                                   temp=temp.replace(","," ");
                                                   temp=temp.replace("!"," ");
						   temp=temp.replace("?"," ");
                                                   temp=temp.replace("-"," ");
                                                   temp=temp.replace("।।"," ");    
                                                 }     
		return temp;
} 
public void storeCount(FileWriter wct){
	try{
    wct.write("Word\t\tCount"+" \n");
    int co=0;
                          for (String key : freq.keySet()) { co++;
                                         wct.write((co)+" - "+key+"\t\t"+freq.get(key)+" \n");
                               
						       }
	  }catch (IOException e) {

			        e.printStackTrace();

		} 
       CloseFile(wct);
                         
     }
public void storeInvertedIndex(FileWriter wct){
	try{
    wct.write("Word\t\tDocument"+" \n");
       
                          for (String key : invertedindex.keySet()) {
                                 
                                 
                               
                              
								  wct.write(key+"\t\t");
								  ArrayList<Integer> docList = invertedindex.get(key);
								  for(int document :docList ){
								   wct.write(document+"  ");  
									   
								  }
								  wct.write("\n");
                               
                               
                              }
                            
	}catch (IOException e) {

			        e.printStackTrace();

		} 
       CloseFile(wct);
	
                      }
}
