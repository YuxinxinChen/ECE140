public class For extends Seq {
        protected int first;
        protected int last;
        protected int step;
       public For(int first, int last, int step)
       {
               this.first = first;
               this.last = last;
               this.step = step;
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

}
