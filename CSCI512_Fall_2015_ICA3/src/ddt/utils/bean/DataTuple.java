package ddt.utils.bean;

public class DataTuple implements Comparable<DataTuple> {
	private String name;
	private String value;

	public DataTuple(String name, String value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public int hashCode() {
		return (name.hashCode() + value.hashCode()) / 73;
	}

	@Override
	public String toString() {
		return name + "=" + value;
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if (obj != null && obj instanceof DataTuple) {
			final DataTuple dt = (DataTuple) obj;
			result = this.name.equals(dt.name) && this.value.equals(dt.value);
		}
		return result;
	}

	@Override
	public int compareTo(DataTuple arg0) {
		return name.compareTo(arg0.name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
