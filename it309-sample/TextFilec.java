import java.io.*;
import java.util.*;

class TextFile implements Indexing{
    HashMap<String, Integer> freq = new HashMap<String, Integer>();  //Store frequency of unique stemmed words in file
    HashMap<String, Integer> stopmap = new HashMap<String, Integer>(); //List of stopword
    HashMap<String, ArrayList<Integer>> invertedindex = new HashMap<String, ArrayList<Integer>>();//inverted index
    HashMap<String, ArrayList<Integer>> TF= new HashMap<String, ArrayList<Integer>>();//term frequency in different document	
    int document_id=0;//for giving id to documents
    public String Filename[]={};
    TextFile(){
         StoreStopWord();     
      }
public void storebinaryIndex(){
     if(invertedindex.isEmpty()){
       	fileread();
       }
try{
FileOutputStream fos1 = new FileOutputStream("inverted_index.ser");
FileOutputStream fos2 = new FileOutputStream("document_term_count.ser");
ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
oos1.writeObject(invertedindex);
oos2.writeObject(TF);
oos1.close();
oos2.close();
}
catch (IOException e) {

	        e.printStackTrace();

} 
    
        
}
//reading files	
public void fileread(){

       FileReader fr=null;
	   BufferedReader br=null;
     File folder = new File("hindi");
           File[] listOfFiles = folder.listFiles();
           Filename=new String[listOfFiles.length];
           for (int i = 0; i < listOfFiles.length; i++){
                   Filename[i]=listOfFiles[i].getName();
			      
            }
          
        

		System.out.println("File handling");

   Arrays.sort(Filename);
for(String FILENAME : Filename){
              String sr ="hindi/"+FILENAME;
   try {  
        fr = new FileReader(sr); 
        br = new BufferedReader(fr);
        document_id++;
        System.out.println(FILENAME+"  doc id = "+document_id);
         int inside=0; //for checking which part of document we need to read. 
	     int sLine;
         StringBuffer word=new StringBuffer();
         while ((sLine = br.read()) != -1) {
		     char ch=(char)sLine;
                                
         if(!Character.isLetter(ch) && symbol(sLine))
           {  
								int l=word.length(); 
								  
			if(l>0){
                                
                                 String temp=new String(word);
								  word.setLength(0);//making word empty
                             
                             
                
                             if(temp.contains("title")){
		                         inside=(inside==1)?0:1;
                                 continue;
                             }
				             if(temp.contains("content")){
		                        inside=(inside==1)?0:1;
                                continue;
                             }
                             if(inside==1){
                                            
								             CountWords(temp);
								             temp.setLength(0);
									      }
                
				
		
                }   
							  
             }
           
		                   
        else
        word.append(ch);
                                   
                               
                               
        }
storeTF();
} catch (IOException e) {

	        e.printStackTrace();

}
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
 return ((ascii>31 && ascii<48)||(ascii>57 && ascii<65)||(ascii>90 && ascii<97)||(ascii>122 && ascii<127)||(ascii==9)
         ||(ascii==10)||(ascii==2404)||(ascii==2405));
   
  
}   
boolean StopWord(String st){
   return stopmap.containsKey(st);
}

 
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
void storeTF(){
   for(String key : freq.keySet()){
   ArrayList<Integer> termFrequencyList = TF.get(key);
       if(termFrequencyList == null) {  //if list is not present
         termFrequencyList = new ArrayList<Integer>();
         termFrequencyList.add(freq.get(key));
         TF.put(key,termFrequencyList);//store count of words
    } else {
              int count=freq.get(key)-termFrequencyList.get(termFrequencyList.size()-1);//if counted in last document then subtract it 
            										//to get the count in present document
	    if(count>0) //new document containing that word 
            termFrequencyList.add(count);						
        
    }
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
public void storeCount(){

        FileWriter wct=null;
	try{
       wct=new FileWriter("stemwordcount.txt");
    wct.write("Word\t\tCount"+" \n");
    int co=0;
                          for (String key : freq.keySet()) { co++;
                                         wct.write((co)+" - "+key+"\t\t"+freq.get(key)+" \n");
                               
						       }
	  }catch (IOException e) {

			        e.printStackTrace();

		} 
       System.out.println("stemwordcount.txt is created");
       CloseFile(wct);
                         
     }

public void storeInvertedIndex(){
       FileWriter wct=null;
	try{
            wct=new FileWriter("inveted_index.txt");
    wct.write("Word\t\tDocument"+" \n");
       
                          for (String key : invertedindex.keySet()) {
                                 
                                 
                               
                              
								  wct.write(key+"\t\t");
								  ArrayList<Integer> docList = invertedindex.get(key);
								  ArrayList<Integer> termFrequencylist = TF.get(key);
								  for(int i=0;i<docList.size();i++){
								   wct.write(" ["+docList.get(i)+"]-->{"+termFrequencylist.get(i)+"} ");  
									   
								  }
								  wct.write("\n");
                               
                               
                              }
                            
	}catch (IOException e) {

			        e.printStackTrace();

		} 
    System.out.println("inverted_index.txt is created");
       CloseFile(wct);
	
  }
 //Taking stopword from list and storing in stopmap	
void StoreStopWord(){
        FileReader fe=null;
		BufferedReader be=null;         
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
 

}   
    

