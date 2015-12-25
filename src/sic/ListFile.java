/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author DELL
 */
public class ListFile {

    BufferedWriter output = null;
    obCode s = new obCode();
    ArrayList<String> obcodes = new ArrayList();
    private ArrayList<Instruction> lisFile = s.getArraylist();
     private boolean startError=false;
    private boolean endError=false;
    

    public ListFile() {
      startError=s.isStartError();
      endError=s.isEndError();
        File file = new File("LISFILE");
        ArrayList<Instruction> list= new ArrayList();
        list.addAll(lisFile);
        Instruction temp=new Instruction();
        for(int i=0;i<list.size();i++){
            if(list.get(i).isIsComment())
                continue;
            if(list.get(i).getLabels().startsWith("="))
                for(int j=i;j<list.size();j++){
                    if(list.get(j).getOperation().equalsIgnoreCase("ltorg")||list.get(j).getOperation().equalsIgnoreCase("end")){
                        temp=list.get(j);
//                        list.set(j, list.get(i));
                        list.remove(j);
                        list.add(i, temp);
                        
                       
                        i=j;
                        
                        break;
                        
                        
                        
                    }
                    
                        
                    
                }
            
            
        }
        for(Instruction h:list){
            if(h.isIsComment())
                continue;
            if(h.getLabels().startsWith("=")){
                h.setOperation(h.getLabels().substring(1));
                if(h.getLabels().substring(1).startsWith("*"))
                    h.setOperation("*");
                h.setLabels("*");
                h.setOperand("");
            }
            if(h.getOperand().startsWith("=*")){
                h.setOperand("=*");
            }
            if(h.getOperation().startsWith("=*")){
                h.setOperation("*");
            }
            if(!h.getTempOperand().equals("hehe")){
                h.setOperand(h.getTempOperand());
            }
           
            
        }

        try {
            output = new BufferedWriter(new FileWriter(file));
            output.write("SIC Assembler V1.0");
            output.newLine();
            output.newLine();
           if(startError)
           {output.write("**** invalid Starting Line");
          
           output.newLine();
           }
            for (Instruction m : list) {
                /////////////////////////////////////////////////////////////////////////////////
                if(m.isIsComment()){
                    output.write(m.getCommentLine());
            
                    output.newLine();
                    continue;
                }
                
                
               // System.out.println(s.errortab.get(m.getAddresses()).size()>0);
                if(s.errortab.get(m.getAddresses()).size()>0&&!m.getOperation().equalsIgnoreCase("Start")){
                    ////////////////////////////////////////////////////////////////////////////
                    //String out = m.getAddresses() + " " + m.getLabels()+ " " + m.getOperation() + " " + m.getOperand();
               
                    output.write(m.toString());
                    output.newLine();
                    for(int i=0;i<s.errortab.get(m.getAddresses()).size();i++){
                        String error = s.errortab.get(m.getAddresses()).get(i);
                        output.write("****"+error);
                        
                        output.newLine();
                        
                    }
                    continue;
                   
                }
                
                        if(m.getOperation().equalsIgnoreCase("org") || m.getOperation().equalsIgnoreCase("ltorg")||m.getOperation().equalsIgnoreCase("EQU")){
                m.setAddresses("    ");
            }
               String out=m.toString();
               String s[]=out.split("`");
                
               if(s.length==1){
                output.write(s[0]);
                output.newLine();
               }else{
                   for(int i=0;i<s.length;i++){
                       output.write(s[i]);
                       output.newLine();
                   }
               }
              if(m.isIsForward()&&!m.getOperand().equals("*")){
                        output.write("****operand not found");
                        
                        output.newLine();
                    }  
               if(m.isExpError()){
                        output.write("****invalid expression");
                        
                        output.newLine();
                    }  
              
            }
            if(endError)
               output.write("**** invalid Ending Line");
            
            output.close();

        } catch (IOException ex) {
            Logger.getLogger(ListFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        ArrayList<Instruction>finall=new ArrayList();
        for(Instruction s:lisFile){
            if(s.isIsComment())
                continue;
            if(!s.getOperation().equalsIgnoreCase("ltorg")&& !s.getOperation().equalsIgnoreCase("org")&& !s.getOperation().equalsIgnoreCase("equ"))
                finall.add(s);
        }
        lisFile=finall;
//        for( Instruction h:finall){
//            System.out.println(h);
//        }
        if(s.errCount<1)
  textRecord(lisFile.get(0).getAddresses());
//        
    }

    private void DEVF2() {
        
        File file = new File("OBJFILE");

        try {
            output = new BufferedWriter(new FileWriter(file));
            output.write(H());
            output.newLine();
            for(int i=0;i<obcodes.size();i++){
                output.write(obcodes.get(i));
                output.newLine();
            }
            output.write(E());
            
            output.close();

        } catch (IOException ex) {
            Logger.getLogger(ListFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.exit(1);
        
    }
    private String fix(String num,int limit){
        if(num.length()<limit){
                        String f=num;
                        for(int k=0;k<limit - f.length();k++){
                            num="0"+num;
                        }
                     }
     return num;   
    }

    private String length(String m, String n) {

        int x = Integer.parseInt(m, 16);
        int y = Integer.parseInt(n, 16);
        int sub = y - x;
        String f = Integer.toHexString(sub);
        return f.toUpperCase();
    }

    private String H() {
        String h1 = "H";
        String h2 = lisFile.get(0).getLabels().trim();
        String h3 = lisFile.get(0).getAddresses().trim();
        String end = lisFile.get(lisFile.size() - 1).getAddresses().trim();
        String h4 = length(h3, end).trim();

        if (h2.length() < 6) {
            String f = h2;
            for (int i = 0; i < 6 - f.length(); i++) {
                h2 = h2 + " ";

            }

        }
        if (h3.length() < 6) {
            String f = h3;
            for (int i = 0; i < 6 - f.length(); i++) {
                h3 = "0" + h3;

            }

        }
        if (h4.length() < 6) {
            String f = h4;
            for (int i = 0; i < 6 - f.length(); i++) {
                h4 = "0" + h4;

            }

        }

        return h1 + h2 + h3 + h4;

    }

    private String E() {
        String e1 = "E";
        String e2 = lisFile.get(0).getAddresses();
        if (e2.length() < 6) {
            String f = e2;
            for (int i = 0; i < 6 - f.length(); i++) {
                e2 = "0" + e2;

            }
        }
        return e1 + e2;
    }

    private void textRecord(String StAdd){
        int i =0;
        String obcode="";
        String currentobcode = "";
        String nextobcode = "";
        String ad="";
        boolean newRecord =false;
        boolean found = false;
        boolean repeated = false;
         
         
        for (Instruction m : lisFile) {
            if(m.isIsComment())
                continue;
            if(m.getOperation().equalsIgnoreCase("end")){
                String endAd;
                endAd = m.getAddresses();
                String delta = Integer.toHexString((Integer.parseInt(endAd, 16))-(Integer.parseInt(StAdd, 16)));
               //  System.out.print("T"+fix(StAdd,6)+fix(delta,2));
                
                obcodes.add("T"+fix(StAdd,6)+fix(delta,2)+obcode);
//                System.out.println(obcodes);

                DEVF2();
                return;
            }
            ad= m.getAddresses();
            if(found==false){
            if (ad.equalsIgnoreCase(StAdd)==false){
                i++;
                continue;
            }else
                found=true;
            
                }
            currentobcode = m.getObCode();
            
            if ((currentobcode.equalsIgnoreCase("")) && !(m.getOperation().equalsIgnoreCase("start"))){
                if(!repeated){
                    String endAd;
                endAd = m.getAddresses();
                String delta = Integer.toHexString((Integer.parseInt(endAd, 16))-(Integer.parseInt(StAdd, 16)));
                
//                System.out.print("T"+fix(StAdd,6)+fix(delta,2));
                obcodes.add("T"+fix(StAdd,6)+fix(delta,2)+obcode);
               // System.out.println(obcode);
                
                }
                newRecord=true;
                repeated = true;
                continue;
            }
                if(newRecord){
                    newRecord=false;
                    repeated=false;
                 textRecord(m.getAddresses());   
                }
                
            if(obcode.length()+ currentobcode.length()<=60){
                 obcode += currentobcode;

            }
           
            
            else{
                //newrecord
                String endAd;
                endAd = m.getAddresses();
                String delta = Integer.toHexString((Integer.parseInt(endAd, 16))-(Integer.parseInt(StAdd, 16)));
//                 System.out.print("T"+fix(StAdd,6)+fix(delta,2));
                 obcodes.add("T"+fix(StAdd,6)+fix(delta,2)+obcode);
                //System.out.println(obcode);
                textRecord(m.getAddresses());
            }
            
            
        }
        
        
    }
}
