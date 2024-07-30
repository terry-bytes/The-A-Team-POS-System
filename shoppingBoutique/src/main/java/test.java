
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author T440
 */
public class test {

    /**
     * @param args the command line arguments
     */
    static String message;
    public static void main(String[] args) {
       int x=3;
       boolean b1 = true;
       boolean b2 = false;
       if((b1 | b2) || x++>4){
           System.out.println("f" + x++);
       }
       if((!b1 & b2) && ++x>4)
            System.out.println(x);
    }
    static public <E extends CharSequence> Collection<? extends CharSequence> getCollection(Collection<E> c){
        Collection<E> t = new TreeSet<E>();
        for(E e: c)
            t.add(e);
            return t;
        
    }
    public static String getMessage() throws Exception{
    String message="A";
    
    try{
        throw new Exception();
    }catch(Exception e){
        try{
            try{
                throw new Exception();
            }catch(Exception ex){
                message += "B";
            }
            throw new Exception();
        }catch(Exception x){
            message += "C";
        }finally {
            message += "D";
        }
    }finally{
        message += "E";
    }
    return message;
}
}
