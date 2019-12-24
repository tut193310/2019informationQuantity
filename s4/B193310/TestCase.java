package s4.B193310; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. 
import java.lang.*;
import s4.specification.*;

/*
interface FrequencerInterface {     // This interface provides the design for frequency counter.
    void setTarget(byte[]  target); // set the data to search.
    void setSpace(byte[]  space);  // set the data to be searched target from.
    int frequency(); //It return -1, when TARGET is not set or TARGET's length is zero
                    //Otherwise, it return 0, when SPACE is not set or Space's length is zero
                    //Otherwise, get the frequency of TAGET in SPACE
    int subByteFrequency(int start, int end);
    // get the frequency of subByte of taget, i.e target[start], taget[start+1], ... , target[end-1].
    // For the incorrect value of START or END, the behavior is undefined.
}
*/

/*
package s4.specification;
public interface InformationEstimatorInterface{
    void setTarget(byte target[]); // set the data for computing the information quantities
    void setSpace(byte space[]); // set data for sample space to computer probability
    double estimation(); // It returns 0.0 when the target is not set or Target's length is zero;
// It returns Double.MAX_VALUE, when the true value is infinite, or space is not set.
// The behavior is undefined, if the true value is finete but larger than Double.MAX_VALUE.
// Note that this happens only when the space is unreasonably large. We will encounter other problem anyway.
// Otherwise, estimation of information quantity, 
}                        
*/


public class TestCase {
    public static void main(String[] args) {
		try {
			FrequencerInterface myObject1,myObject2,myObject3;
			int freq1,freq2,freq3;
			System.out.println("checking s4.B193310.Frequencer");
			myObject1 = new s4.B193310.Frequencer();
			myObject2 = new s4.B193310.Frequencer();
			myObject3 = new s4.B193310.Frequencer();

			//setTarget check
			myObject1.setTarget("H".getBytes());
			myObject2.setTarget("".getBytes());
			myObject3.setTarget("H".getBytes());

			//setSpace check
			myObject1.setSpace("Hi Ho Hi Ho".getBytes());
			myObject2.setSpace("Hi Ho Hi Ho".getBytes());
			myObject3.setSpace("".getBytes());

			//frequency check
			freq1 = myObject1.frequency();
			System.out.println("\"H\" in \"Hi Ho Hi Ho\" appears "+freq1+" times. ");
			//Count
			if(4 == freq1) { 
				System.out.println("OK"); 
			} else {
				System.out.println("WRONG"); 
			}

			freq2 = myObject2.frequency();
			System.out.println("Target is not set or Target length is zero");
			if(-1 == freq2){
				System.out.println("OK");
			}else{
				System.out.println("WRONG");
			}

			freq3 = myObject3.frequency();
			System.out.println("Space is not set or Space length is zero");
			if(0 == freq3){
				System.out.println("OK");
			}else{
				System.out.println("WRONG");
			}

			/*Set Target and Space : OK
			  Set Space and not set Target : WRONG
			  Set Target and not set Space : OK
			  ターゲットを設定しなかった(文字列が零)場合に正確に動作しなかった
			*/
		}
		catch(Exception e) {
			System.out.println("Exception occurred: STOP");
		}

		try {
			InformationEstimatorInterface myObject;
			double value;
			System.out.println("checking s4.B193310.InformationEstimator");
			myObject = new s4.B193310.InformationEstimator();
			myObject.setSpace("3210321001230123".getBytes());

			myObject.setTarget("".getBytes());
			value = myObject.estimation();
			if(value == 0.0){
				System.out.println("No Target");
			}

			myObject.setTarget("0".getBytes());
			value = myObject.estimation();
			System.out.println(">0 "+value);

			myObject.setTarget("01".getBytes());
			value = myObject.estimation();
			System.out.println(">01 "+value);

			myObject.setTarget("0123".getBytes());
			value = myObject.estimation();
			System.out.println(">0123 "+value);

			myObject.setTarget("00".getBytes());
			value = myObject.estimation();
			System.out.println(">00 "+value);

			myObject.setTarget("5".getBytes());
			value = myObject.estimation();
			if(value == Double.MAX_VALUE){
				System.out.println("Not Set Space or Target Not Found");
			}

			myObject.setSpace("".getBytes());
			myObject.setTarget("0".getBytes());
			value = myObject.estimation();
			if(value == Double.MAX_VALUE){
				System.out.println("Not Set Space or Target Not Found");
			}
		}
		catch(Exception e) {
			System.out.println("Exception occurred: STOP");
		}

    }
}	    
	    
