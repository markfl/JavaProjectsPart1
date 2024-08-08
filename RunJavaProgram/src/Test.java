public class Test {

	public static void main(String[] args) {
		 System.out.println("Start");
	     for(String str : args){
	    	 System.out.println(str);
	     }
	     for (int i=0;i<100;i++) {
	    	 int ipsumCounter = (int) Math.ceil(Math.random()*9);
	    	 System.out.println(ipsumCounter);
	     }
	     for (int i=0;i<100;i++) {
	    	 int rnd1 = (int)(Math.random()*10);

	    	 // rnd2 is in the range 1-10 (including 10). The parentheses are necessary!
	    	 int rnd2 = (int)(Math.random()*10) + 1;

	    	 // rnd3 is in the range 5-10 (including 10). The range is 10-5+1 = 6.
	    	 int rnd3 = (int)(Math.random()*6) + 5;

	    	 // rnd4 is in the range -10 up to 9 (including 9). The range is doubled (9 - -10 + 1 = 20) and the minimum is -10.
	    	 int rnd4 = (int)(Math.random()*20) - 10;
	    	 System.out.println(rnd1 + " " + rnd2 + " " + rnd3 + " " + rnd4);
	     }
	    	 
    	 int max = 10; 
         int min = 1; 
         int range = max - min + 1; 
   
         // generate random numbers within 1 to 10 
         for (int i = 0; i < 10; i++) { 
             int rand = (int)(Math.random() * range) + min; 
   
             // Output is different everytime this code is executed 
             System.out.println(rand); 
	     }
	}
}