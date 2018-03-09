public class FibIt implements SeqIt {
	private int first1;
	private int first2;
	private int last;
	private int c1;
	private int c2;
	
	public FibIt(Fib fibseq)
	{
		first1 = fibseq.first1;
		first2 = fibseq.first2;
		last = fibseq.last;
		c2 = first1;
		c1 = first2;
	}

	public boolean hasNext()
	{
		return (c2 <= last);
		
	}

	public int next() throws UsingIteratorPastEndException
	{
		if(!hasNext()) {
			throw new UsingIteratorPastEndException();			
		}
		int returnv = c2;
		int newc1 = c1+c2;
		c2 = c1;
		c1 = newc1;
		return returnv;
	}
}

