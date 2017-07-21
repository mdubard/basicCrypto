/**
 * Mary DuBard
 * Cryptology
 * PSet 10
 * December 11, 2016
 **/

public class TextbookRSA{
  //hard-coded for this homework problem
  public static void main(String args[]){
    for (int i = 0; i < 55; i++) { //value has to be less than 55
      if(((Math.pow(i, 3)) % 55) == 41) //will check by hand for gcd(i, 55) = 1
        System.out.println("message: " + i); 
    }
  }
}