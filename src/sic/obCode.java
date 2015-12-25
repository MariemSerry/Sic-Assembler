package sic;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 *
 * @author DELL
 */
public class obCode {

    Addresses m = new Addresses();
    private ArrayList<Instruction> arraylist = m.getCode();
    private Hashtable<String, String> hashtable = m.getSymTab();
    private String obCode = null;
    private String x = null;
    private String targetAdd = "";
    private String operand = null;
    OpCode object = new OpCode();
    public Hashtable<String, ArrayList<String>> errortab = new Hashtable();
    private boolean startError = false;
    private boolean endError = false;

    public ArrayList<Instruction> getArraylist() {
        return arraylist;
    }

    public boolean isStartError() {
        return startError;
    }

    public boolean isEndError() {
        return endError;
    }

    public obCode() {
        errorss();

        startError = m.isStartError();
        endError = m.isEndError();
        for (Instruction l : arraylist) {

            if (l.isIsComment()) {
                continue;
            }
//            if (errortab.get(l.getAddresses()).size() > 0) {
//                continue;
//            }

            if (l.getOperation().equalsIgnoreCase("resw") || l.getOperation().equalsIgnoreCase("resb") || l.getOperation().equalsIgnoreCase("start") || l.getOperation().equalsIgnoreCase("end") || l.getOperation().equalsIgnoreCase("ltorg") || l.getOperation().equalsIgnoreCase("org") || l.getOperation().equalsIgnoreCase("equ")) {
                continue;
            } else if (l.getOperation().trim().equalsIgnoreCase("word")) {
                String word = l.getOperand();
                if(l.getLabels().startsWith("=")){
                    if(l.getLabels().startsWith("=*"))
                    {
                        l.setObCode(l.getOperand());
                        l.setOperand("=*");
                    }
                    else
                    {
                        l.setObCode(ASCII(l.getOperand().substring(1)));
                    }
                    continue;
                }
                int y = Integer.parseInt(word);
                String ob = Integer.toHexString(y);
                String finaal = ob;
                if (ob.length() < 6) {
                    for (int i = 0; i < 6 - ob.length(); i++) {
                        finaal = "0" + finaal;
                    }
                    l.setObCode(finaal.toUpperCase());
//                    System.out.println(finaal);
                }
            } else if (l.getOperation().equalsIgnoreCase("byte")) {
                if (l.getOperand().startsWith("x") || l.getOperand().startsWith("X")) {
                    String m = l.getOperand().substring(2, (l.getOperand().length()) - 1);
                    l.setObCode(m.toUpperCase());
                } else if (l.getOperand().startsWith("c") || l.getOperand().startsWith("C")) {
                    String m = l.getOperand().substring(2, (l.getOperand().length()) - 1);

                    String s = "";
                    for (int i = 0; i < m.length(); i++) {
                        char c = m.charAt(i);
                        String asccii = Integer.toHexString(c);

                        s = s + asccii;

                    }
                    l.setObCode(s.toUpperCase());
                }

            } //////////////////////////////////////////////////////////////////
            else if (l.getOperand().startsWith("0")) {
                obCode = object.getOpCode(l.getOperation()) + l.getOperand().substring(1);
                l.setObCode(obCode.toUpperCase());

            } else {
                if (l.getOperand().contains(",x") || l.getOperand().contains(",X")) {
                    x = "1";
                    operand = l.getOperand().substring(0, (l.getOperand().length()) - 2);
                } else {
                    x = "0";
                    operand = l.getOperand();
                }
                obCode = object.getOpCode(l.getOperation().toUpperCase());

                if (l.getOperation().equalsIgnoreCase("rsub")) {
                    l.setObCode(obCode.toUpperCase() + "0000");
                } else {
                    targetAdd = hashtable.get(operand);
                    if (targetAdd == null) {
                        continue;
                    }

//                System.out.println("");
                    l.setObCode(obCode.toUpperCase() + address().toUpperCase());
//                System.out.println(address());

                }

            }

        }
//        for(Instruction l:arraylist){
//           System.out.println(l.toString());
//        }

    }

    private String address() {

        Long y = Long.parseLong(targetAdd, 16);
        String m = String.format("%16s", Long.toBinaryString(y));
        m = x + m.replaceAll(" ", "0").substring(1);
        int l = Integer.parseInt(m, 2);
        String address = Integer.toString(l, 16).toUpperCase();

        if (address.length() < 4) {
            int f = address.length();
            for (int i = 0; i < 4 - f; i++) {
                address = "0" + address;
            }

        }
        return address;
    }
    public int errCount=0;

    public void errorss() {

        Addresses m = new Addresses();
        ArrayList<Instruction> listWithAdd = m.getCode();
        OpCode n = new OpCode();
        Hashtable<String, String> hashtable = n.getObtab();
        ArrayList<String> Labels = new ArrayList();
        Hashtable<String, String> hashtable2 = m.getSymTab();

        for (int i = 0; i < listWithAdd.size(); i++) {

            if (listWithAdd.get(i).isIsComment()) {
                continue;
            }
            ArrayList<String> error = new ArrayList();
            String operationcode = hashtable.get(listWithAdd.get(i).getOperation().toLowerCase());
            String operation = listWithAdd.get(i).getOperation().toLowerCase();
            String label = listWithAdd.get(i).getLabels();
            String operand = listWithAdd.get(i).getOperand();
            if (operation.equalsIgnoreCase("ltorg")) {
                continue;
            }
            if(listWithAdd.get(i).isExpError()){
                  
                error.add("invalid expression");
                errCount++;
            }
          
            if (listWithAdd.get(i).isIsForward()&& !listWithAdd.get(i).getOperand().equals("*")) {
              //  System.out.println(listWithAdd.get(i).getOperand());
                
                error.add("operand not found");
                                errCount++;

                
            }
            if (operationcode == null && !operation.equalsIgnoreCase("word") && !operation.equalsIgnoreCase("byte") && !operation.equalsIgnoreCase("resw") && !operation.equalsIgnoreCase("resb") && !operation.equalsIgnoreCase("start") && !operation.equalsIgnoreCase("end") && !operation.equalsIgnoreCase("ltorg") && !operation.equalsIgnoreCase("equ") && !operation.equalsIgnoreCase("org")) {
                error.add("unrecognized operation code");
                                errCount++;

            }

            if (listWithAdd.get(i).getLabels().trim().equals("") == false) {
               // System.out.println(Labels.contains(listWithAdd.get(i).getLabels()));
                if (Labels.contains(listWithAdd.get(i).getLabels())) {
                    error.add("duplicate Label");
                                    errCount++;

                        //   System.out.println(listWithAdd.get(i).getAddresses());         

                } else {
                    Labels.add(listWithAdd.get(i).getLabels());
                    
                    
                }
            }
          //  System.out.println(hashtable2.get(listWithAdd.get(i).getOperand())+listWithAdd.get(i).getOperand());
            if (hashtable2.containsKey(listWithAdd.get(i).getOperand()) == false && listWithAdd.get(i).getOperand().startsWith("0") == false && !operation.equalsIgnoreCase("word") && !operation.equalsIgnoreCase("byte") && !operation.equalsIgnoreCase("resw") && !operation.equalsIgnoreCase("resb") && !operation.equalsIgnoreCase("start") && !operation.equalsIgnoreCase("end") && !operation.equalsIgnoreCase("ltorg") && !operation.equalsIgnoreCase("equ") && !operation.equalsIgnoreCase("org")) {
                String temp = listWithAdd.get(i).getOperand();
                temp = temp.replaceAll(",x", "");
                temp = temp.replaceAll(",X", "");
               
             //   System.out.println(listWithAdd.get(i).getOperation()+"  "+listWithAdd.get(i).getOperand());
                if (hashtable2.containsKey(temp)==false &&!temp.trim().equals("") ) {
                    error.add("unrecognized operand");
                                    errCount++;

                    
                }

            }
          // System.out.println(error);
            errortab.put(listWithAdd.get(i).getAddresses(), error);
        }
       // System.out.println(Labels);
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
