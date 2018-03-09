import java.util.*;
public class AList implements AListElement{
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
		list.add(new Integer(i));
	}
	public void expand_addto(AList list) {
		list.add(expand());
	}
	public void flatten_addto(AList list) {
		AList it=flatten();
                for(int i=0; i<it.list.size();i++) {
			AListElement tmp = (AListElement)it.list.get(i);
			tmp.flatten_addto(list);
		}
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
		for(int count=0; count<list.size();count++) {
			AListElement x = (AListElement)list.get(count);
			x.expand_addto(newlist);
		}
		return newlist;
	}

	public AList flatten() {
		AList newlist = new AList();
		for(int count=0; count<list.size();count++) {
			AListElement x = (AListElement)list.get(count);
			x.flatten_addto(newlist);
		}
		return newlist;
	}
	


}
