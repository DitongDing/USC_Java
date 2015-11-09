package hw.controlflow.samples;

public class Subject1 {
	
	public static void main(String[] args) {
		int N = 20;
		System.out.println("This will print out the odd/even status of numbers less than " + N);
		for (int i=0; i<N; i++) {
			if (i%2==0) {
				System.out.println(i + " is even");
			} else {
				System.out.println(i + " is odd");
			}
		}
	}

}
