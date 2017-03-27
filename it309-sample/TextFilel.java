import java.io.*;
import java.util.*;

class TextFile implements Indexing{
    HashMap<String, Integer> freq = new HashMap<String, Integer>();  //Store frequency of unique stemmed words in file
    HashMap<String, Integer> stopmap = new HashMap<String, Integer>(); //List of stopword
    HashMap<String, ArrayList<Integer>> invertedindex = new HashMap<String, ArrayList<Integer>>();//inverted index
    	
    int document_id=0;//for giving id to documents
    FileReader fr=null;
    BufferedReader br=null;
    FileReader fe=null;
    BufferedReader be=null;
    TextFile(){
         StoreStopWord();     
      }
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
                 int inside=0; //for checking which part of document we need to read. 
			     int sLine;
		         String word="";
			while ((sLine = br.read()) != -1) {
				     				    char ch=(char)sLine;
                                        //System.out.println(sLine+"  "+ch);
				 if(sLine==10 || sLine==32 || sLine==45 || sLine==46 || sLine==61 || sLine==44|| sLine==2404)
                                             {  //check space,newline,dash,dot,equal to,comma,purna viram - to get word 
										   int l=word.length();
										   String temp=word.substring(0,l);
										   word="";
								    
                                     if(temp.contains("<title>")){
				                        inside=1;
                                                   String sr[]=temp.split(">");
                                                  if(sr.length>1) //if there is term remaining after spliting
                                                  {
                                                   if(sr[1].contains("</title")){  //if there is no gap until in the title block
                                                       String st[]=sr[1].split("<");
                                                       CountWords(st[0]);
                                                       inside=0;
                                                     }
                                                  
                                                   else
						                            CountWords(sr[1]);
		                                   
                                                  
 						   }
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
										       
										    
                                             		temp=RemovePanctuation(temp);
                                                         if(temp.length()>0)
          				                                    CountWords(temp);
											 	 
                                        }
									  } 
				                   else{
                                          if(!symbol(sLine)){
                                              word += ch;
                                           }
                                       
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
boolean symbol(int ascii){   
 return (ascii>32 && ascii<47)||(ascii>57 && ascii<60)||(ascii>62 && ascii<65)||(ascii>90 && ascii<97)||(ascii>122 && ascii<127);
}    
boolean StopWord(String st){
   
    
   if(stopmap.containsKey(st)){
      //System.out.println("StopWord "+st);
     return true;
    }
   else 
    return false;    
}
/* void CountWords(String sr){
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
										 //int count = freq.containsKey(a[i]) ? freq.get(a[i]) : 0;
                                          //freq.put(a[i], count + 1);
										   //Store word without Stemming
										  
                                      }
                                }      
              
  }*/
 
void CountWords(String sr){
    if(!StopWord(sr)){
					                     HindiStemmer hobj=new HindiStemmer();
                                         int end=hobj.stem(sr);
										  String token=sr.substring(0,end);
                                          int count = freq.containsKey(token) ? freq.get(token) : 0;
                                          freq.put(token, count + 1);
										  InvertedList(token,document_id);
                     } 
}    
void InvertedList(String token,int doc_id){
       ArrayList<Integer> docList = invertedindex.get(token);
     if(docList == null) {  //if list is not present
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
   if(temp.contains("’’") ||temp.contains("‘‘") ||temp.contains("॥") || temp.contains(":")|| temp.contains("।")|| temp.contains("/")|| temp.contains(">")|| temp.contains("<")){
                                                   temp=temp.replace("॥","");
						                           temp=temp.replace(":","");
                                                   temp=temp.replace("।","");
                                                   temp=temp.replace("/","");
                                                   temp=temp.replace("!","");
						                           temp=temp.replace(">","");
                                                   temp=temp.replace("<","");
						                           temp=temp.replace("’’","");
                                                   temp=temp.replace("‘‘","");
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
 public void storebinaryIndex(){
      try{
          FileOutputStream fos = new FileOutputStream("t.tmp");
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      
        oos.writeObject(invertedindex);
        oos.close();
      }
     catch (IOException e) {

			        e.printStackTrace();

		} 
        
        
          
 } 
 public void readbinaryIndex(){
       try{
        FileInputStream fis = new FileInputStream("t.tmp");
        ObjectInputStream ois = new ObjectInputStream(fis);
        HashMap<String, ArrayList<Integer>> index=(HashMap<String, ArrayList<Integer>>)ois.readObject();
        for (String key : index.keySet()) {
            
								  System.out.print(key+"\t\t");
								  ArrayList<Integer> docList = index.get(key);
								  for(int document :docList ){
								   System.out.print(document+"  ");  
									   
								  }
								  System.out.println();
                               
                                   
        }
        ois.close();
       }
      catch (Exception e) {

			        e.printStackTrace();

		}
     

 } 
 public void postinglist(String str){



  
}   
    
}
