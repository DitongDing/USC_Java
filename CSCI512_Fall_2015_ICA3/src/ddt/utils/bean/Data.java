package ddt.utils.bean;

import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

public class Data extends TreeSet<DataTuple> {
	private static final long serialVersionUID = -5255106630583747395L;

	public Data(String[]... dataTuples) {
		super();
		for (String[] dataTuple : dataTuples)
			addDataTuple(dataTuple);
	}
	
	public Data(List<String[]> dataTuples) {
		super();
		for (String[] dataTuple : dataTuples)
			addDataTuple(dataTuple);
	}
	
	public void addDataTuple(String[] dataTuple) {
		this.add(new DataTuple(dataTuple[0], dataTuple[1]));
	}

	@Override
	public String toString() {
		String result = "";

		Iterator<DataTuple> iterator = this.iterator();
		if (iterator.hasNext()) {
			result += iterator.next().toString();
			while (iterator.hasNext())
				result += "&" + iterator.next().toString();
		}

		return result;
	}

	@Override
	public int hashCode() {
		int hashCode = 0;
		Iterator<DataTuple> it = this.iterator();
		while (it.hasNext())
			hashCode = (hashCode + it.next().hashCode()) / 73;
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if (obj != null && obj instanceof Data) {
			result = true;
			final Data dtc = (Data) obj;
			Iterator<DataTuple> it = this.iterator();
			Iterator<DataTuple> io = dtc.iterator();
			while (result && it.hasNext() && io.hasNext())
				result = it.next().equals(io.next());
			if (result)
				result = !it.hasNext() && !io.hasNext();
		}
		return result;
	}
}