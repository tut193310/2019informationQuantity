package s4.B193310;
import java.lang.*;
import s4.specification.*;

/*package s4.specification;
  ここは、１回、２回と変更のない外部仕様である。
  public interface FrequencerInterface {     // This interface provides the design for frequency counter.
  void setTarget(byte  target[]); // set the data to search.
  void setSpace(byte  space[]);  // set the data to be searched target from.
  int frequency(); //It return -1, when TARGET is not set or TARGET's length is zero
  //Otherwise, it return 0, when SPACE is not set or SPACE's length is zero
  //Otherwise, get the frequency of TAGET in SPACE
  int subByteFrequency(int start, int end);
  // get the frequency of subByte of taget, i.e target[start], taget[start+1], ... , target[end-1].
  // For the incorrect value of START or END, the behavior is undefined.
  }
*/



public class Frequencer implements FrequencerInterface{
    // Code to start with: This code is not working, but good start point to work.
    byte [] myTarget;
    byte [] mySpace;
    boolean targetReady = false;
    boolean spaceReady = false;

    int []  suffixArray; // Suffix Arrayの実装に使うデータの型をint []とせよ。


    // The variable, "suffixArray" is the sorted array of all suffixes of mySpace.                                    
    // Each suffix is expressed by a integer, which is the starting position in mySpace. 
                            
    // The following is the code to print the contents of suffixArray.
    // This code could be used on debugging.                                                                

    private void printSuffixArray() {
        if(spaceReady) {
            for(int i=0; i< mySpace.length; i++) {
                int s = suffixArray[i];
                for(int j=s;j<mySpace.length;j++) {
                    System.out.write(mySpace[j]);
                }
                System.out.write('\n');
            }
        }
    }

    private int suffixCompare(int i, int j) {
        // suffixCompareはソートのための比較メソッドである。
        // 次のように定義せよ。
        // comparing two suffixes by dictionary order.
        // suffix_i is a string starting with the position i in "byte [] mySpace".
        // Each i and j denote suffix_i, and suffix_j.                            
        // Example of dictionary order                                            
        // "i"      <  "o"        : compare by code                              
        // "Hi"     <  "Ho"       ; if head is same, compare the next element    
        // "Ho"     <  "Ho "      ; if the prefix is identical, longer string is big  
        //  
        //The return value of "int suffixCompare" is as follows. 
        // if suffix_i > suffix_j, it returns 1   
        // if suffix_i < suffix_j, it returns -1  
        // if suffix_i = suffix_j, it returns 0;   

        // ここにコードを記述せよ
        int k = 0;
        while(i+k < mySpace.length && j+k < mySpace.length){
            //System.out.println(mySpace[i+k]);
            //System.out.println(mySpace[j+k]);

            //これ外に出せるよ
            if((int)mySpace[i+k] < (int)mySpace[j+k]){
                //System.out.println("-1");
                return -1;
            }else if((int)mySpace[i+k] > (int)mySpace[j+k]){
                //System.out.println("1");
                return 1;
            }

            k++;
            
            if(i+k == mySpace.length && j+k == mySpace.length){
                return 0;
            }else if(i+k == mySpace.length){
                //System.out.println("0 -1");
                return -1;
            }else if(j+k == mySpace.length){
                //System.out.println("0 1");
                return 1;
            }
        }
        //System.out.println("0");
        return 0; // この行は変更しなければいけない。 
    }

    public void setSpace(byte []space) { 
        // suffixArrayの前処理は、setSpaceで定義せよ。
        mySpace = space; if(mySpace.length>0) spaceReady = true;
        // First, create unsorted suffix array.
        suffixArray = new int[space.length];
        // put all suffixes in suffixArray.
        for(int i = 0; i< space.length; i++) {
            suffixArray[i] = i; // Please note that each suffix is expressed by one integer.      
        }
        //                                            
        // ここに、int suffixArrayをソートするコードを書け。
        
        for (int i = 0; i < suffixArray.length-1; i++) {
            for (int j = suffixArray.length - 1; j > i; j--) {
                if (suffixCompare(suffixArray[j-1],suffixArray[j]) == 1) {
                    int temp = suffixArray[j - 1];
                    suffixArray[j - 1] = suffixArray[j];
                    suffixArray[j] = temp;
                }
            }
        }
        // 　順番はsuffixCompareで定義されるものとする。
    }

    // Suffix Arrayを用いて、文字列の頻度を求めるコード
    // ここから、指定する範囲のコードは変更してはならない。

    public void setTarget(byte [] target) {
        myTarget = target; if(myTarget.length>0) targetReady = true;
    }

    public int frequency() {
        if(targetReady == false) return -1;
        if(spaceReady == false) return 0;
        return subByteFrequency(0, myTarget.length);
    }

    public int subByteFrequency(int start, int end) {
        /* This method be work as follows, but much more efficient
           int spaceLength = mySpace.length;                      
           int count = 0;                                        
           for(int offset = 0; offset< spaceLength - (end - start); offset++) {
            boolean abort = false; 
            for(int i = 0; i< (end - start); i++) {
             if(myTarget[start+i] != mySpace[offset+i]) { abort = true; break; }
            }
            if(abort == false) { count++; }
           }
        */
        int first = subByteStartIndex(start, end);
        int last1 = subByteEndIndex(start, end);

        //System.out.println("first:"+first+" end:"+last1);

        return last1 - first;
    }
    // 変更してはいけないコードはここまで。

    private int targetCompare(int i, int j, int k) {
        // suffixArrayを探索するときに使う比較関数。
        // 次のように定義せよ
        // suffix_i is a string in mySpace starting at i-th position.
        // target_i_k is a string in myTarget start at j-th postion ending k-th position.
        // comparing suffix_i and target_j_k.
        // if the beginning of suffix_i matches target_i_k, it return 0.
        // The behavior is different from suffixCompare on this case.
        // if suffix_i > target_i_k it return 1; 
        // if suffix_i < target_i_k it return -1;
        // It should be used to search the appropriate index of some suffix.
        // Example of search 
        // suffix          target
        // "o"       >     "i"
        // "o"       <     "z"
        // "o"       =     "o"
        // "o"       <     "oo"
        // "Ho"      >     "Hi"
        // "Ho"      <     "Hz"
        // "Ho"      =     "Ho"
        // "Ho"      <     "Ho "   : "Ho " is not in the head of suffix "Ho"
        // "Ho"      =     "H"     : "H" is in the head of suffix "Ho"
        //
        // ここに比較のコードを書け 
        //

        int length = Math.min(mySpace.length-i, k-j);

        if((mySpace.length-i)<(k-j)){
            return -1;
        }
        
        for(int x=0; x<length; x++){
            //System.out.println("mySpace:" + mySpace[x+i] + " myTarget:" + myTarget[x+j]);
            if(mySpace[x+i]-myTarget[x+j]>0){
                return 1;
            }else if(mySpace[x+i]-myTarget[x+j]<0){
                return -1;   
            } 
        }
        return 0;
    }


    private int subByteStartIndex(int start, int end) {
        //suffix arrayのなかで、目的の文字列の出現が始まる位置を求めるメソッド
        // 以下のように定義せよ。
        /* Example of suffix created from "Hi Ho Hi Ho"
           0: Hi Ho
           1: Ho
           2: Ho Hi Ho
           3:Hi Ho
           4:Hi Ho Hi Ho
           5:Ho
           6:Ho Hi Ho
           7:i Ho
           8:i Ho Hi Ho
           9:o
           A:o Hi Ho
        */

        // It returns the index of the first suffix 
        // which is equal or greater than target_start_end.                         
        // Assuming the suffix array is created from "Hi Ho Hi Ho",                 
        // if target_start_end is "Ho", it will return 5.                           
        // Assuming the suffix array is created from "Hi Ho Hi Ho",                 
        // if target_start_end is "Ho ", it will return 6.                
        //                                                                          
        // ここにコードを記述せよ。                                                 
        //
        /*              
        for(int i=0; i<suffixArray.length;i++){
            System.out.println(i + ":" + targetCompare(suffixArray[i], start, end));
            if(targetCompare(suffixArray[i], start, end) == 0){
                return i;
            }
        }
        */

        int left = 0;
        int right = suffixArray.length - 1;
        while(right >= left){
            int mid = (right + left) / 2;
            //System.out.println("mid:" + mid +  " left:" + left + " right:" + right);
            int result = targetCompare(suffixArray[mid], start, end);
            if(mid-1 >= 0){
                if(result == 0 && targetCompare(suffixArray[mid-1], start, end) != 0){
                    //System.out.println("return:" + mid);
                    return mid;
                }
            }else if(mid == 0){
                if(result == 0){
                    return 0;
                }
            }

            //System.out.println(mid + ":" + targetCompare(suffixArray[mid], start, end));
            if(result < 0){
                left = mid+1;
            }else if(result >= 0){
                right = mid-1;
            }
        }
        return suffixArray.length;   
    }

    private int subByteEndIndex(int start, int end) {
        //suffix arrayのなかで、目的の文字列の出現しなくなる場所を求めるメソッド
        // 以下のように定義せよ。
        /* Example of suffix created from "Hi Ho Hi Ho"
           0: Hi Ho                                    
           1: Ho                                       
           2: Ho Hi Ho                                 
           3:Hi Ho                                     
           4:Hi Ho Hi Ho                              
           5:Ho                                      
           6:Ho Hi Ho                                
           7:i Ho                                    
           8:i Ho Hi Ho                              
           9:o                                       
           A:o Hi Ho                                 
        */
        // It returns the index of the first suffix 
        // which is greater than target_start_end; (and not equal to target_start_end)
        // Assuming the suffix array is created from "Hi Ho Hi Ho",                   
        // if target_start_end is "Ho", it will return 7 for "Hi Ho Hi Ho".  
        // Assuming the suffix array is created from "Hi Ho Hi Ho",          
        // if target_start_end is"i", it will return 9 for "Hi Ho Hi Ho".    
        //                                                                   
        //　ここにコードを記述せよ                                           
        //
        
        /*
        int p = subByteStartIndex(start, end);
        
        if(suffixArray.length < end - start){
            return p;
        }

        while(targetCompare(suffixArray[p], start, end) == 0){
            p++;
            if(p == suffixArray.length){
                break;
            }
        }
        //System.out.println("return : " + p);
        return p;
        */
        ///*
        int left = 0;
        int right = suffixArray.length - 1;
        while(right >= left){
            int mid = (right + left) / 2;
            //System.out.println("mid:" + mid +  " left:" + left + " right:" + right);
            int result = targetCompare(suffixArray[mid], start, end);
            if(mid+1 <= suffixArray.length-1){
                if(result == 0 && targetCompare(suffixArray[mid+1], start, end) == 1){
                    //System.out.println("return:" + mid);
                    return mid+1;
                }
            }else if(mid == suffixArray.length-1){
                if(result == 0){
                    return suffixArray.length;
                }
            }

            //System.out.println(mid + ":" + targetCompare(suffixArray[mid], start, end));
            if(result > 0){
                right = mid-1;
            }else if(result <= 0){
                left = mid+1;
            }
        }
        return suffixArray.length; // この行は変更しなければならない、
        //*/      
    }


    // Suffix Arrayを使ったプログラムのホワイトテストは、
    // privateなメソッドとフィールドをアクセスすることが必要なので、
    // クラスに属するstatic mainに書く方法もある。
    // static mainがあっても、呼びださなければよい。
    // 以下は、自由に変更して実験すること。
    // 注意：標準出力、エラー出力にメッセージを出すことは、
    // static mainからの実行のときだけに許される。
    // 外部からFrequencerを使うときにメッセージを出力してはならない。
    // 教員のテスト実行のときにメッセージがでると、仕様にない動作をするとみなし、
    // 減点の対象である。
    public static void main(String[] args) {
        Frequencer frequencerObject;
        try {
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("Hi Ho Hi Ho".getBytes());
            frequencerObject.printSuffixArray(); // you may use this line for DEBUG
            /* Example from "Hi Ho Hi Ho"    
               0: Hi Ho                      
               1: Ho                         
               2: Ho Hi Ho                   
               3:Hi Ho                       
               4:Hi Ho Hi Ho                 
               5:Ho                          
               6:Ho Hi Ho                    
               7:i Ho                        
               8:i Ho Hi Ho                  
               9:o                           
               A:o Hi Ho                     
            */

            frequencerObject.setTarget("H".getBytes());

            //                                         
            // ****  Please write code to check subByteStartIndex, and subByteEndIndex
            //

            int result = frequencerObject.frequency();

            System.out.print("Freq = "+ result+" ");
            if(4 == result) { 
                System.out.println("OK");
            } else {
                System.out.println("WRONG"); 
            }

            
            int a;
            System.out.println("\nSuffix Compare");
            a=frequencerObject.suffixCompare(4, 4);
            System.out.println("Compare 4 4 :" + a);
            a=frequencerObject.suffixCompare(4, 5);
            System.out.println("Compare 4 5 :" + a);
            a=frequencerObject.suffixCompare(5, 4);
            System.out.println("Compare 5 4 :" + a);

            System.out.println("Target Compare");
            a=frequencerObject.targetCompare(3, 0, 1);
            System.out.println("Compare \"H\" start 3 :" + a);
            a=frequencerObject.targetCompare(7, 0, 1);
            System.out.println("Compare \"H\" start 7 :" + a);
            a=frequencerObject.targetCompare(9, 0, 1);
            System.out.println("Compare \"H\" start 9 :" + a);

            System.out.println("Check Target Start and End");
            a=frequencerObject.subByteStartIndex(0, 1);
            System.out.println("H start :" + a);
            a=frequencerObject.subByteEndIndex(0, 1);
            System.out.println("H end :" + a);


            frequencerObject.setTarget(" H".getBytes());
            System.out.println("\nChange Target :  _H (_ is space)");

            System.out.println("Target Compare");
            a=frequencerObject.targetCompare(2, 0, 2);
            System.out.println("Compare \"_H\" start 2 :" + a);
            a=frequencerObject.targetCompare(4, 0, 2);
            System.out.println("Compare \"_H\" start 4 :" + a);
            a=frequencerObject.targetCompare(9, 0, 2);
            System.out.println("Compare \"_H\" start 9 :" + a);

            System.out.println("Check Target Start and End");
            a=frequencerObject.subByteStartIndex(0, 2);
            System.out.println("H start :" + a);
            a=frequencerObject.subByteEndIndex(0, 2);
            System.out.println("H end :" + a);            
        }
        catch(Exception e) {
            System.out.println("STOP");
        }
    }
}

