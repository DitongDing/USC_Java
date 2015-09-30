package ddt.utils.bean;

import java.util.ArrayList;
import java.util.Iterator;

public class Data extends ArrayList<DataTuple> {
	private static final long serialVersionUID = -5255106630583747395L;

	public Data(String[]... dataTuples) {
		super(dataTuples.length);
		for (String[] dataTuple : dataTuples)
			this.add(new DataTuple(dataTuple[0], dataTuple[1]));
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
			final Data dtc = (Data) obj;
			Iterator<DataTuple> it = this.iterator();
			Iterator<DataTuple> io = dtc.iterator();
			while (it.hasNext() && io.hasNext()) {
				it.next();
				io.next();
			}
			if (!it.hasNext() && !io.hasNext())
				result = true;
		}
		return result;
	}
}