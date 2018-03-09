public class ForIt implements SeqIt {
	private int first;
	private int last;
	private int step;
	private int current;

	public ForIt(For forseq)
	{
		first = forseq.first;
		last = forseq.last;
		step = forseq.step;
		current = first-step;
	}
	public boolean hasNext()
	{
		if(step > 0)
			return ((current+step)<=last);
		else if(step < 0)
			return ((current+step)>=last);
		return true;
	}

	public int next()
	{
		if(!hasNext()) {
			System.err.println("ForIt called past end");
			System.exit(1);
		}
		current = current + step;
		return current;
	}	
}
