/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sic;

/**
 *
 * @author DELL
 */
public class Instruction {

    private String labels;
    private String operation;
    private String operand;
    private String addresses;
    private String Comment="";
    private String CommentLine;
    private String tempOperand="hehe";
    private boolean isComment=false;
    private boolean isForward=false;
    private boolean expError=false;

    public String getTempOperand() {
        return tempOperand;
    }

    public boolean isExpError() {
        return expError;
    }

    public void setExpError(boolean expError) {
        this.expError = expError;
    }

    public void setTempOperand(String tempOperand) {
        this.tempOperand = tempOperand;
    }

    public boolean isIsComment() {
        return isComment;
    }

    public boolean isIsForward() {
        return isForward;
    }

    public void setIsForward(boolean isForward) {
        this.isForward = isForward;
    }

    public void setIsComment(boolean isComment) {
        this.isComment = isComment;
    }
    

    public String getComment() {
        return Comment;
    }

    public void setComment(String Comment) {
        this.Comment = Comment;
    }

    public String getCommentLine() {
        return CommentLine;
    }

    public void setCommentLine(String CommentLine) {
        this.CommentLine = CommentLine;
    }
    
    

    public Instruction() {
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String instruction) {
        this.operation = instruction;
    }

    public String getOperand() {
        return operand;
    }

    public void setOperand(String operand) {
        this.operand = operand;
    }

    public String getAddresses() {
        return addresses;
    }

    public void setAddresses(String addresses) {
        this.addresses = addresses;
    }

    public String getObCode() {
        return obCode;
    }

    public void setObCode(String obCode) {
        this.obCode = obCode;
    }
    private String obCode;

    @Override
    public String toString() {
        String s1 = "";
        String s2 = "";
        String s3 = "";
        String s4 = "";
        String m="";
       if(isComment)
       {
           return CommentLine;
       }
       
        if (obCode == null) {
            obCode="";
            s1 = "       ";
        } 
        else {
            for (int i = 0; i < 7 - obCode.length(); i++) {
                s1 = " " + s1;
            }

        }

        if (labels == null) {
            labels="";
            s2 = "         ";
        } else {
            for (int i = 0; i < 9 - labels.length(); i++) {
                s2 = " " + s2;
            }
        }

        for (int i = 0; i < 8 - operation.length(); i++) {
            s3 = " " + s3;
        }
        for (int i = 0; i < 17 - operand.length(); i++) {
            s4 = " " + s4;
        }
        if(obCode.length()<=6){
              m = addresses + " " + obCode + s1 + labels + s2 + operation + s3 + operand+s4+Comment+"`";
        }

        else{
            /* m = addresses + " " + obCode.substring(0,6)+" " + s1 + labels + s2 + operation + s3 + operand;
             int x=(int)Math.ceil(obCode.length()/6.0)-1;
             for(int i=1;i<=x;i++){
                 m+="\n";
                 m+="     ";
                 
                 if(i==x){
                     m+=obCode.substring(i*6).trim();
                 }
                 else{
                     m+=obCode.substring(i*6, (i*6)+6);
                 }*/
             m = addresses + " " + obCode.substring(0,6) +" "+ s1 + labels + s2 + operation + s3 + operand +s4+Comment+ "`";
            String mimo=obCode.substring(6);
                  String mimo2="";
                   boolean done = false;
                  while(mimo!=""){
                      if(mimo.length()>6)
                          mimo2=mimo.substring(0,6);
                      else{
                          mimo2=mimo.substring(0);
                           done = true;
                      }
                      ////////////////////////////////////////////////////////////
                      if(m.endsWith("`"))
                          m+="     "+mimo2;
                      else
                      m+="`     "+mimo2;
                      if(!done)
                      mimo=mimo.substring(6);
                      else
                          return m;
                 
             }
                  
             
             
        }
        
        return m;

    }

}
