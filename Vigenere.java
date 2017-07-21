public class Vigenere {
  //Double used to approximate desired frequency sum of .065
  final static double correctSumOfSquaresMin = .053;
  
  final static int asciiConst = 65;
  final static int alphabetLength = 26;
  
  final static double[] frequencies = new double[] {.082, .015, .028, .042, .127, .022, .020, .061, .070, 
    .001, .008, .040, .024,.067, .075, .019, .001, .060, .063, .090, .028, .010, .024, .020, .001, .001};
  
  public static void main(String[] args) {
    String cipherText = "KCCPKBGUFDPHQTYAVINRRTMVGRKDNBVFDETDGILTXRGUDDKOTFMBPV" + 
      "GEGLTGCKQRACQCWDNAWCRXIZAKFTLEWRPTYCQKYVXCHKFTPONCQQRHJVAJUWETMCMSPKQDYH" + 
      "JVDAHCTRLSVSKCGCZQQDZXGSFRLSWCWSJTBHAFSIASPRJAHKJRJUMVGKMITZHFPDISPZLVLG" +
      "WTFPLKKEBDPGCEBSHCTJRWXBAFSPEZQNRWXCVYCGAONWDDKACKAWBBIKFTIOVKCGGHJVLNHI" +
      "FFSQESVYCLACNVRWBBIREPBBVFEXOSCDYGZWPFDTKFQIYCWHJVLNHIQIBTKHJVNPIST";
    
    System.out.println("Encrypted Message:\n" + cipherText);
    String[] multipleShifts = multipleShifts(cipherText);
    System.out.println("Key:\n" + multipleShifts[1]);
    System.out.println("Decrypted Message:\n" + multipleShifts[0]);
  }
  
  /**
   * Returns a String[] that holds the decrypted message and key
   * 
   * @param cipherText The encrypted message
   * @return [decrypted message, key]
   **/
  public static String[] multipleShifts(String cipherText) {
    int keyLength = 1;
    boolean foundShifts = false;
    
    //Searches for key until a working key is found or until it's exhausted all possibilities
    while (!foundShifts && keyLength < cipherText.length()) {
      String[] subsets = new String[keyLength]; //stores letter sequences to test different shifts
      int[] shifts = new int[keyLength]; //stores shift options
      int checkFound = 1;
      
      //Calculates subsets and searches for working shifts for each subset
      for (int i = 0; i < keyLength; i++) {
        subsets[i] = everyNth(cipherText.substring(i,cipherText.length()), keyLength);
        shifts[i] = singleShift(everyNth(cipherText.substring(i,cipherText.length()), keyLength));
        checkFound = checkFound * shifts[i];
      }
      
      //If a working shift exists for every subset of our message
      if(checkFound != 0) { 
        foundShifts = true;
        
        //Calculate the key and decrypt the subsets
        String key = "";
        for (int i = 0; i < keyLength; i++) {
          subsets[i] = decryptMessage(subsets[i], shifts[i]);
          key += Character.toString((char) (shifts[i] + asciiConst));
        }
        
        //Combine subsets for full decrypted message
        String result = "";
        int i = 0;
        while (result.length() < cipherText.length()) {
          for (int j = 0; j < keyLength; j++) {
            if (i < subsets[j].length()) {
              result += subsets[j].charAt(i);
            }
          }
          i++;
        }
        return new String[] {result, key};
      }   
      
      keyLength++;
    } 
    return new String[] {"No decrypted message found.", "No key."};
  }
  
  //Helper methods
  
  /**
   * Helper method that returns integer shift for encrypted Vigenere message 
   * to become a legible decrypted message.
   * 
   * @param cipherText The encrypted message
   * 
   * @return keyInt    The shift; if no shift found for legible message,
   *                   returns 0.
   **/
  private static int singleShift(String cipherText) {
    int shift = 0;
    double shiftFreq = 0;
    
    //Search through alphabet for working key
    for (int keyInt = 0; keyInt < alphabetLength; keyInt++) {
      double sumOfSquares = 0;
      double decryptedCharFreq[] = new double[alphabetLength]; //stores letter frequencies
      
      //Calculate frequency of decrypted letters in cipherText
      for (int i = 0; i < cipherText.length(); i++) {
        int letter = (cipherText.charAt(i) - keyInt - asciiConst + alphabetLength) % alphabetLength;
        decryptedCharFreq[letter]++;
      }
      for (int i = 0; i < alphabetLength; i++) {
        decryptedCharFreq[i] = (decryptedCharFreq[i] / cipherText.length());
      }
      
      //Calculate sum of squares of frequencies
      for (int i = 0; i < alphabetLength; i++) {
        sumOfSquares += (decryptedCharFreq[i] * frequencies[i]);
      }
      
      //Find greatest frequency close to .065
      if(sumOfSquares > correctSumOfSquaresMin && sumOfSquares > shiftFreq) {
        shift = keyInt;
        shiftFreq = sumOfSquares;
      }
    }
    return shift;
  }
  
  /**
   * Helper method that returns string of every n letters from inputted string
   * 
   * @param str The original string
   * @param n   The amount of letters to skip
   * 
   * @return result The string of alternating letters
   **/
  private static String everyNth(String str, int n) {
    String result = "";
    for (int i = 0; i < str.length(); i = i+n) {
      result+=str.charAt(i);
    }
    return result;
  }
  
  /**
   * Helper method that returns a decrypted message given an encrypted message
   * and an integer shift.
   * 
   * @param cipherText The encrypted message
   * @param shift      The integer shift
   * 
   * @return origMessage The decrypted String
   **/
  private static String decryptMessage(String cipherText, int shift) {
    String origMessage = "";
    for (int i = 0; i < cipherText.length(); i++) {
      int newChar = cipherText.charAt(i)  - shift;
      if (newChar < asciiConst) {
        newChar += alphabetLength;
      }
      origMessage += Character.toString((char) newChar);
    }
    return origMessage;
  }
}