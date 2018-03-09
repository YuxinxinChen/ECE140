// the Seq base class

public abstract class Seq implements AListElement{
	protected static int Seq_count=0;
	public abstract int upperBound();
	public static int getCount()
	{
		return Seq_count;
	}

	public abstract SeqIt createSeqIt();
	public abstract void expand_addto(AList list);
	public abstract void flatten_addto(AList list);
}


