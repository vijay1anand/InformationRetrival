import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Collections;
import java.util.*;
class RankedRetrival implements Retrival{
HashMap<String, ArrayList<Integer>> index=new HashMap<String, ArrayList<Integer>>();//index of document containing
HashMap<String, ArrayList<Integer>> tf=new HashMap<String, ArrayList<Integer>>();//term count document-wise
//intialising index and term frequency
RankedRetrival(){

      boolean fileexisist=new File("inverted_index.ser").isFile();
   if(!fileexisist)
    { 
      TextFile fileCreating=new TextFile();
      System.out.println("File is creating");
      fileCreating.storebinaryIndex();
    }
    System.out.println("Extracting Data From Binary File");
    BinaryFileRead();        
      

 
}
void BinaryFileRead(){
try
{
FileInputStream fis1 = new FileInputStream("inverted_index.ser");
FileInputStream fis2 = new FileInputStream("document_term_count.ser");
      ObjectInputStream ois1 = new ObjectInputStream(fis1);
ObjectInputStream ois2 = new ObjectInputStream(fis2);
      this.index=(HashMap<String, ArrayList<Integer>>)ois1.readObject();
      this.tf=(HashMap<String, ArrayList<Integer>>)ois2.readObject();
      ois1.close();
      ois2.close();
}
  catch (Exception e){

            e.printStackTrace();

    }    

}
//
HashMap<Integer,Double> SortDocument(HashMap<Integer,Double> doc_list){ 
  List<Entry<Integer,Double>> list_formed = new LinkedList<Entry<Integer,Double>>(doc_list.entrySet());

      // Sorting the list based on values
      Collections.sort(list_formed, new Comparator<Entry<Integer,Double>>()
      {
          public int compare(Entry<Integer,Double> o1,
                  Entry<Integer,Double> o2)
          {
            
                  return o2.getValue().compareTo(o1.getValue());
            
           } 
          });
      HashMap<Integer,Double> sortedMap = new LinkedHashMap<Integer,Double>();
      for (Entry<Integer,Double> entry : list_formed)
      {
          sortedMap.put(entry.getKey(), entry.getValue());
      }

      return sortedMap;

}

//input 
public void postinglist(){
Scanner scan =new Scanner(System.in);
    System.out.println("Enter String");  
    String query=scan.nextLine();
    String query_word[]=query.split(" ");
    HashMap<Integer,Double> list=new HashMap<Integer,Double>();
    File folder = new File("hindi");
 File[] listOfFiles = folder.listFiles();
 int total_document=listOfFiles.length;
 String Filename[]=new String[listOfFiles.length];
 for (int k = 0; k < listOfFiles.length; k++){
                           Filename[k]=listOfFiles[k].getName();
                         }
Arrays.sort(Filename);
for(int i=0;i<query_word.length;i++){
 HindiStemmer hobj=new HindiStemmer();
 int end=hobj.stem(query_word[i]);
  String token=query_word[i].substring(0,end);
 ArrayList<Integer> docList=index.get(token);
 
//System.out.println("value = "+val+"IDF = "+Math.log(val));     
 ArrayList<Integer> termFrequency=tf.get(token);
if(docList==null)
 System.out.println("String not Found");

else{
//System.out.println("N = "+total_document+" DN = "+docList.size());
 double  val=(double)total_document/docList.size();
 double IDF=Math.log(val);  

//System.out.println("\tDoc_ID \t Frequency \t Weight of Page");
     for(int j=0;j<docList.size();j++){
                        double weight=termFrequency.get(j)*IDF;
//System.out.println("\t ["+docList.get(j)+"]\t{"+termFrequency.get(j)+"}\t{"+weight+"} ");
             if(!list.containsKey(docList.get(j)))
              list.put(docList.get(j),weight);
             else{
                  double weight_final = list.get(docList.get(j))+weight;
                   list.put(docList.get(j),weight_final);       
              }

            }
    }

  } 
  list=(HashMap<Integer,Double>)SortDocument(list);
  int m=0;
  System.out.println("Retrived document");   
  for(int key : list.keySet()){
    m++;
     if(m%11==0){
      System.out.println("More Files press Y \t Exit Press N");
      char ch=scan.nextLine().charAt(0);
       if(ch=='N' || ch=='n')
        break;
     }
    System.out.println(m+" "+Filename[key]); 
  }
  System.out.println("Further Search press 1 \t Exit Press any Other Number");
   int con=scan.nextInt();

if(con==1)
postinglist();

}

public void readbinaryIndex(){
if(index.isEmpty()){
System.out.println("empty");
BinaryFileRead();
}
try{
   FileWriter wct=new FileWriter("inverted_index.txt");
   wct.write("Word \t\t Inverted Index");
for (String key : index.keySet()){
        //System.out.print(key+" : ");
        wct.write(key+"\t");
  

ArrayList<Integer> docList=index.get(key);					  
              ArrayList<Integer> termFrequency=tf.get(key);
for(int i=0;i<docList.size();i++){
//System.out.print(" ["+docList.get(i)+"] --> {"+termFrequency.get(i)+"}");
              wct.write(" ["+docList.get(i)+"] --> {"+termFrequency.get(i)+"},"); 
              }       
             //System.out.println();
   
              wct.write("\n");
      } 
      wct.close();
      System.out.println("inverted_index.txt file created");                  
	}
 catch(Exception e){
  e.printStackTrace();
 } 
}

} 
   

