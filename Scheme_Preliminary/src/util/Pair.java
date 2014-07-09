package util;


public class Pair<A,B> implements Comparable<Pair<A,B>> {
	public final A first;
	public final B second;

	public Pair(A a, B b) {
		first = a;
		second = b;
	}

    @Override
	public String toString() {
		return "<" + first + ", " + second + ">";
	}

    @Override
	public boolean equals(Object object) {
		if (object instanceof Pair) {
			Pair<A,B> other = (Pair<A,B>) object;
			return first.equals(other.first) && second.equals(other.second);
	    }
		else {
			return false;
		}
	}

    // verbatim from anroid.util.Pair
    @Override
    public int hashCode() {
        return (first == null ? 0 : first.hashCode()) ^ (second == null ? 0 : second.hashCode());
    }

	public int compareTo(Pair<A,B> other) {
		Comparable<A> cFirst = (Comparable<A>) first;
		int comparison = cFirst.compareTo(other.first);
		if (comparison == 0) {
			Comparable<B> cSecond = (Comparable<B>) second;
			comparison = cSecond.compareTo(other.second);
		}
		return comparison;
	}

}
	
