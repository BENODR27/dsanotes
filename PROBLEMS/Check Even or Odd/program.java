import java.util.*;

public class program {
    public static boolean isEven(int num){
        return num % 2 == 0;
    }
     public static void main(String[] args){
         int n= 13;
         if(isEven(n)){
            System.out.println("even");
         }else{
            System.out.println("odd");
         }
     }  
}