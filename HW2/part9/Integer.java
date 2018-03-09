public class Integer implements AListElement{
	private int n;
	public Integer(int n) {
		this.n = n;
	}
	public void expand_addto(AList list) {
		list.add(n);
	}
	public void flatten_addto(AList list) {
		list.add(n);
	}
	public String toString() {
		return (""+n);
	}
}
