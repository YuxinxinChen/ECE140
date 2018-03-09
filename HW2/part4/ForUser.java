public class ForUser {
	public static int sum1(For r)
	{
		ForIt rIt = new ForIt(r);
		int sum=0;
		while(rIt.hasNext())
			sum += rIt.next();
		return sum;
	}

}
