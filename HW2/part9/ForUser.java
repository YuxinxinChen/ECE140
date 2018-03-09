public class ForUser {
//	public ForUser() {}
//	protected static ForUser fu = new ForUser();
	public static int sum1(For r)
	{
		ForIt rIt = new ForIt(r);
		int sum=0;
		try {
		while(rIt.hasNext())
			sum += rIt.next();
		}
		catch (UsingIteratorPastEndException uipee){
			System.err.println("should never be here");
		}
		return sum;
	}

	public static int sum2(For r)
	{	
		ForIt rIt = new ForIt(r);
		int sum = 0;
		int nextone = 0;
		while(true) {
			try{
				nextone = rIt.next();
				sum += nextone;
			}
			catch (UsingIteratorPastEndException uipee){
				return sum;
			}
		}
	}

}
