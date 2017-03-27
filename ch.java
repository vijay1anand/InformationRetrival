import java.io.*;
import java.util.*;
class fread{
static String Filename[]={};
public static void main(String args[]){
                  int document_id=0;//for giving id to documents
    			  FileReader fr=null;
    			  BufferedReader br=null;
                  FileReader fe=null;
    BufferedReader be=null;
 			   File folder = new File("hindi");//folder where file is stored
               File[] listOfFiles = folder.listFiles(); //list of file in folder
               String Filename[]=new String[listOfFiles.length];
                   for (int i = 0; i < listOfFiles.length; i++){
                           Filename[i]=listOfFiles[i].getName();//storing name
						   fr = new FileReader(listOfFiles[i]); 
	                      br = new BufferedReader(fr);
					      
                    }
			

}

}