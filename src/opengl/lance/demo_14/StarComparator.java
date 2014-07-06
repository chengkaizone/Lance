package opengl.lance.demo_14;

import java.util.Comparator;

public class StarComparator implements Comparator<int[][]> {
	DrawLine line;

	public StarComparator(DrawLine line) {
		this.line = line;
	}

	@Override
	public int compare(int[][] lhs, int[][] rhs) {
		int[] t1 = lhs[1];
		int[] t2 = rhs[1];
		int[] target = line.target;
		int a = line.visited[rhs[0][1]][rhs[0][0]]
				+ Math.abs(t1[0] - target[0]) + Math.abs(t1[1] - target[1]);
		int b = line.visited[rhs[0][1]][rhs[0][0]]
				+ Math.abs(t2[0] - target[0]) + Math.abs(t2[1] - target[1]);
		return a - b;
	}

	@Override
	public boolean equals(Object o) {
		return false;
	}

}
