import java.util.*;
public class AList {
	private ArrayList<Object> list;
	public AList() {
		list = new ArrayList<>();
	}

	public void add(Seq s) {
		list.add(s);
	}
	public void add(AList a) {
		list.add(a);
	}
	public void add(int i) {
		list.add(i);
	}
	public String toString() {
		String s = "[ ";
		int count=0;
		for(count=0; count<list.size(); count++) {
			s+=list.get(count).toString();
			s+=" ";
		}
		if(count ==0)
			s+=" ";
		s +="]";
		return s;
	}
	public AList expand() {
		AList newlist = new AList ();
		int count = 0;
		try{
		for(count=0; count<list.size();count++) {
			Object x = list.get(count);
			if(x.getClass().getName() == "For" || x.getClass().getName() == "Fib") {
				SeqIt it = ((Seq)x).createSeqIt();
				while(it.hasNext())
					newlist.add(it.next());
			}
			else if(x.getClass().getName() =="java.lang.Integer") {
				newlist.add((int)x);
			}
			else {
				newlist.add(((AList)x).expand());
			}
		}
		}
		catch (UsingIteratorPastEndException uipee)
		{	
			System.err.println("should never be here");
		}
		return newlist;
	}

	public AList flatten() {
		AList newlist = new AList();
		for(int count=0; count<list.size();count++) {
			Object x = list.get(count);
			if(x.getClass().getName() == "For" || x.getClass().getName() == "Fib")
				newlist.add((Seq)x);
			else if (x.getClass().getName() =="java.lang.Integer")
				newlist.add((int)x);
			else {
				AList it=((AList)x).flatten();
				for(int i=0; i<it.list.size();i++) {
					if(it.list.get(i).getClass().getName() == "For" || it.list.get(i).getClass().getName() == "Fib")
						newlist.add((Seq)it.list.get(i));
					else if(it.list.get(i).getClass().getName() == "java.lang.Integer")
						newlist.add((int)it.list.get(i));
					else {
						System.err.println("should not be here");
					}
				}
			}
		}
		return newlist;
	}
	


}
