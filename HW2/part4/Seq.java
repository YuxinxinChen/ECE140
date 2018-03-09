// the Seq base class

public abstract class Seq {
	protected static int Seq_count=0;
	public abstract int upperBound();
	public static int getCount()
	{
		return Seq_count;
	}
}


