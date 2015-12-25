/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DELL
 */
public class OpCode { 
 
 private String line=null;
 private String operation=null;
 private String opCode=null;
 private Hashtable<String,String> obtab=new Hashtable();
 
 
    public OpCode() {
        
   FileReader fileReader;
     try {
         fileReader = new FileReader(System.getProperty("user.dir")+"\\obCode.txt");
          BufferedReader bufferedReader = new BufferedReader(fileReader);
           while ((line = bufferedReader.readLine()) != null){
               operation=line.trim();
               opCode=hex(bufferedReader.readLine().trim());
              obtab.put(operation, opCode);
         }
//           System.out.println(obtab);
     } catch (FileNotFoundException ex) {
         Logger.getLogger(OpCode.class.getName()).log(Level.SEVERE, null, ex);
     } catch (IOException ex) {
         Logger.getLogger(OpCode.class.getName()).log(Level.SEVERE, null, ex);
     }
           
    
    }

    public Hashtable<String, String> getObtab() {
        return obtab;
    }

    public String getOpCode(String key){
       String value= obtab.get(key.toLowerCase());
       if(value==null){
           return "invalid";
       }
        return value;
        
    }
   
    private String hex(String x){
        int y=Integer.parseInt(x);
       String c=Integer.toHexString(y);
       if(c.length()<2){
           c="0"+c;
       }
        return c.toUpperCase();
  }
    
    
}
