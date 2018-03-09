public class Fib extends Seq implements AListElement
{
        protected int first1;
        protected int first2;
        protected int last;

	private static int Fib_count=0;

        public Fib(int first1, int first2, int last)
        {
                this.first1 =  first1;
                this.first2 =  first2;
                this.last = last;
		Fib_count++;
		Seq_count++;
        }

        public String toString()
        {
                return( "< "+String.valueOf(first1)+", "+String.valueOf(first2)+" to "+String.valueOf(last)+" >");
        }

	public int upperBound()
	{
		return last;
	}

	public static int getCount()
	{
		return Fib_count;
	}

	public SeqIt createSeqIt() {
		return new FibIt(this);
	}

	public void flatten_addto(AList list) {
		list.add(this);
	}
	public void expand_addto(AList list) {
		SeqIt it = createSeqIt();
		try{
			while(it.hasNext())
				list.add(it.next());
		}
		catch(UsingIteratorPastEndException uipee)
               {
                       System.err.println("should never be here");
               }
	}

}
