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
}
