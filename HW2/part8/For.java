public class For extends Seq {
        protected int first;
        protected int last;
        protected int step;

	private static int For_count = 0;

       public For(int first, int last, int step)
       {
               this.first = first;
               this.last = last;
               this.step = step;
	       For_count++;
	       Seq_count++;
       }

       public String toString()
       {
               String s = "{ "+String.valueOf(first)+" to "+String.valueOf(last)+" by "+String.valueOf(step)+" }";
               return s;
       }

       public int upperBound()
       {
	       if(step > 0 ) 
		       return(last);
	       return(first);
       }

       public static int getCount()
       {
	       return For_count;
       }

       public SeqIt createSeqIt() {
	       return new ForIt(this);
       }

}
