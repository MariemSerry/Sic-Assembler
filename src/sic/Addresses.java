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
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 *
 * @author DELL
 */
public class Addresses {

    private ArrayList<Instruction> code = new ArrayList();
    private String line = null;
    private String adCount = null;
    private String old = null;
    private String labels;
    private String operation;
    private String operand;
    private int count = 0;
    private String comment;
    private Hashtable<String, String> symTab = new Hashtable();
    private boolean startError=false;
    private boolean endError=false;
    private ArrayList<Instruction>literals=new ArrayList();
    private Hashtable<String,String>duplicates=new Hashtable();


    public Addresses() {
        try {
            FileReader fileReader = new FileReader(System.getProperty("user.dir") + "\\SRCFILE");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                Instruction s = new Instruction();
/////////////////////////////////////////////////////////////////////////////
               
                if (line.startsWith(".")) {
                    s.setCommentLine(line);
                    s.setIsComment(true);
                    code.add(s);
                    continue;
                }
                if(line.trim().length()==0)
                    continue;
                labels = line.substring(0, 8).trim();
                operation = line.substring(9, 15).trim();
                if (line.length() > 35) {

                    operand = line.substring(17, 35).trim();
                    comment = line.substring(35);
                    s.setComment(comment);
                } else {
                    operand = line.substring(17).trim();

                }

                s.setLabels(labels);
                s.setOperation(operation);
                s.setOperand(operand);
                //law operation b equ n7seb loperand w yb2a hwa da l address
                if(operation.equalsIgnoreCase("equ")){
                    
                    s.setAddresses(evaluate(operand,s));
                  //  System.out.println(s.isIsForward());
                }
                //law operation b org woperand fih + aw - aw constant n7sbo 
                if(operation.equalsIgnoreCase("org")){
                    if(operand.contains("+")||operand.contains("-")||isConstant(operand)){
                   adCount=(evaluate(operand,s));
//                        System.out.println(adCount);
                  
                    }
                    //law operation b org ngeeb value l operand mn symTab
                   else if(symTab.containsKey(operand)){
                        adCount= symTab.get(operand);
                        
                        
                    }
                   else if(!symTab.containsKey(operand)){
                        s.setIsForward(true);
                   //     System.out.println(s.isIsForward());
                       
                   }
                   else if(isConstant(operand))
                       adCount=evaluate(operand,s);
                    
                   
                      
                }
                
                
                if(operand.startsWith("=")){
                    Instruction f=new Instruction();
                   String temp=operand.substring(1);
                   if(temp.startsWith("x")||temp.startsWith("X")||temp.startsWith("c")||temp.startsWith("C")){
                       
                       f.setOperation("byte");
                    
                   }
                   else if(temp.equals("*")){
                       
                       operand="=*"+adCount;
                       temp=adCount;
                        f.setOperation("word");
                        s.setOperand(operand);
                       
                               }
                   else{
                      
                       f.setOperation("word");
                      
                   }
                   f.setLabels(operand);
                   f.setOperand(temp);
                 //   System.out.println(temp);
                 //  System.out.println(ASCII(temp));
                   if(!duplicates.containsKey(ASCII(temp))){
                        duplicates.put(ASCII(temp),operand);
                        literals.add(f);
                   }
                   else{
                      s.setTempOperand(operand);
                       s.setOperand( duplicates.get(ASCII(temp)));
                   }
                       
                  
                   
                   
                }
                
                if (count == 0) {
                    if(operation.equalsIgnoreCase("Start")==false)
                        startError=true;
                    adCount = operand;
                    if (operand.length() < 4) {
                        String f = operand;
                        for (int k = 0; k < 4 - f.length(); k++) {
                            operand = "0" + operand;
                        }
                    }

                    s.setAddresses(operand);

                }
                else if(operand.contains("+")||operand.contains("-")||operation.equalsIgnoreCase("equ")){
                   s.setAddresses(evaluate(operand,s)); 
                    
                    
                    
                    
                }
                else {
                    getAddress(operand, operation);
                    if (old.length() < 4) {
                        String f = old;
                        for (int k = 0; k < 4 - f.length(); k++) {
                            old = "0" + old;
                        }
                    }

                    s.setAddresses(old);
                   
                }
                
                if(operation.equalsIgnoreCase("end")||operation.equalsIgnoreCase("LTORG")){
                   
                    for(Instruction k:literals){
                         getAddress(k.getOperand(), k.getOperation());
                    if (old.length() < 4) {
                        String f = old;
                        for (int v = 0; v < 4 - f.length(); v++) {
                            old = "0" + old;
                        }
                    }

                    k.setAddresses(old);
                    symTab.put(k.getLabels(), k.getAddresses());
                   
                        
                    }
                    if(operation.equalsIgnoreCase("end")){
                        if((bufferedReader.readLine()) != null)
                            endError=true;
                        
                    }
                    code.addAll(literals);
                   // System.out.println(literals);
                    literals.clear();
                }
                code.add(s);
                
                if (!labels.trim().equalsIgnoreCase("") && !operation.trim().equalsIgnoreCase("start")) {
                  if(!operation.equalsIgnoreCase("equ"))
                    symTab.put(labels, old);
                  else
                      symTab.put(labels, s.getAddresses());
                }

                count++;

            }
//            if(operation.equalsIgnoreCase("end")==false)
//                endError=true;
for(Instruction ll: literals)
{
    System.out.println(ll);
}
            bufferedReader.close();
//        for(Instruction l:code)
//             System.out.println(l.getOperation());

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Addresses.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Addresses.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ScriptException ex) {
            Logger.getLogger(Addresses.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public boolean isStartError() {
        return startError;
    }

    public boolean isEndError() {
        return endError;
    }

   

    public ArrayList<Instruction> getCode() {
        return code;
    }

    public Hashtable<String, String> getSymTab() {
        return symTab;
    }

    private String getAddress(String operand, String operation) {
        int m = 3;
        old = adCount;
        if (operation.equalsIgnoreCase("resw")) {
            m = Integer.parseInt(operand) * 3;

        } else if (operation.equalsIgnoreCase("resb")) {
            m = Integer.parseInt(operand);

        } else if (operation.equalsIgnoreCase("byte")) {
            if (operand.startsWith("c") || operand.startsWith("C")) {
                m = (operand.length()) - 3;

            } else if (operand.startsWith("x") || operand.startsWith("X")) {
                m = ((operand.length()) - 3) / 2;

            }
           

        }
         else if(operation.equalsIgnoreCase("end")||operation.equalsIgnoreCase("LTORG")||operation.equalsIgnoreCase("org")){
                m=0;
                
            }
        adCount = hex(adCount, m);
        return adCount;
    }

    private String hex(String adCount, int x) {
String s="";
        int y = Integer.parseInt(adCount, 16);
        y += x;
        String c = Integer.toHexString(y);
        return c.toUpperCase();

    }
    
    private String evaluate(String operand,Instruction f) throws ScriptException{
  operand=operand.replaceAll(" ", "");
       // System.out.println(operand);
        // law operand * bn7oto equal l address
        if (operand.equals("*")){
                   return adCount;     
                    }
        if(isConstant(operand)){
            return operand;
        }
                   
       String[] result=new String[operand.split("[+-]").length];
        result=operand.split("[+-]");
        
        for(int j=0;j<result.length;j++){
          //  System.out.println(symTab.containsKey(result[j]));
         //   System.out.println(result[j]);
           // law etneen relative plusb3d ydii error
            if(j<result.length-2){
                if(!isConstant(result[j])&&!isConstant(result[j+1])&&!isConstant(result[j+2])&&operand.charAt(operand.indexOf(result[j])+result[j].length())=='-'&&operand.charAt(operand.indexOf(result[j+1])+result[j+1].length())=='-')
                f.setExpError(true);
                   return adCount;
                    }
          if(j<result.length-1){
            if(!isConstant(result[j])&&!isConstant(result[j+1])&&operand.charAt(operand.indexOf(result[j])+result[j].length())=='+'){
                      
                   f.setExpError(true);
                   return adCount;
                    
                }}
            // law operand mwgood fi symTab n replace l esm b value bt3to
             if(symTab.containsKey(result[j])){
                int x=Integer.parseInt(symTab.get(result[j]),16);
               operand= operand.replaceAll(result[j],""+x);
                
            }
           else  if(isConstant(result[j])){
                
                //law awl7eta fioperand b constant w eli b3dha relative w benhom minus
               if(result.length-j>1){
                if(!isConstant(result[j+1])&&operand.charAt(operand.indexOf(result[j])+result[j].length())=='-'){
                    
                   f.setExpError(true);
                   return adCount;
                    
                }
                
                 int x=Integer.parseInt((result[j]));
               operand= operand.replaceAll(result[j],""+x);
               
                
            }}
           
            
           else if(!symTab.containsKey(result[j])){
                f.setIsForward(true);
                return adCount;
            }
            
         
            
            
            //errors
        }
        System.out.println(operand);
       ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
      //  System.out.println(operand);
           String heh= engine.eval(operand).toString();
           if(heh.startsWith("-")){
               f.setExpError(true);
               return adCount;
           }
           int y=Integer.parseInt(heh);
           
           heh=Integer.toHexString(y);
           if(heh.length()<4){
              int m=heh.length();
              for(int i=0;i<4-m;i++)
                  heh="0"+heh;
           }
//           System.out.println(heh);
       return heh.toUpperCase();
       
    }
    private boolean isConstant(String s){
       boolean is_number = true;
try {
  Integer.parseInt(s);
} catch (NumberFormatException  e) {
  is_number = false;
}
return is_number;
    }
    
    private String ASCII(String k){
       if(k.startsWith("c")||k.startsWith("C")){
           String j=k.substring(2, k.length()-1);
           String s = "";
                    for (int i = 0; i < j.length(); i++) {
                        char c = j.charAt(i);
                        String asccii = Integer.toHexString(c);

                        s = s + asccii;

                    }
                    return s.toUpperCase();
       }
       else if(k.startsWith("x")||k.startsWith("X")){
             return k.substring(2, k.length()-1).toUpperCase();
            
       }
       
          return k; 
        
    } 
    }