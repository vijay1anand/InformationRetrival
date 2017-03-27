import java.util.*;
import java.io.*;
interface Indexing{ //implemented using TextFile class
    public void fileread(); //For file Reading
    public void storeCount(); //For Writing count of stemword in a file
    public void storeInvertedIndex(); //For Writing stemword and their posting list in a text file
    public void storebinaryIndex();//storing file in object form
}
interface Retrival{ //implemented using RankedRetrival class
   public void readbinaryIndex();//for printing whole inverted index
   public void postinglist();//for given string

} 
public class index{
	
public static void main(String[] args) throws Exception{
                  
       TextFile obj= new TextFile();
         //obj.fileread();
         //obj.storeCount();
         //obj.storeInvertedIndex();          
         //obj.storebinaryIndex();
   RankedRetrival ob=new RankedRetrival();
          //ob.readbinaryIndex();
          ob.postinglist();
         
 }

}


