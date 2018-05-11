package com.qtfx;

import com.qtfx.lib.math.Vector;

public class TestDistance {

	public static void main(String[] args) {
		double[] a = new double[] { 1, 5, 10 };
		double[] b = new double[] { 1, 1, 1 };
		double[] c = new double[] { 2, 4, 6 };
		
		System.out.println("Euclidean");
		System.out.println(Vector.distanceEuclidean(a, b));
		System.out.println(Vector.distanceEuclidean(a, c));
		
		System.out.println();
		System.out.println("Cosine");
		System.out.println(Vector.distanceCosine(a, b));
		System.out.println(Vector.distanceCosine(a, c));
		
		System.out.println();
		System.out.println("Correlation");
		System.out.println(Vector.distanceCorrelation(a, b));
		System.out.println(Vector.distanceCorrelation(a, c));
		
		System.out.println();
		System.out.println("JensenShannon");
		System.out.println(Vector.distanceJensenShannon(a, b));
		System.out.println(Vector.distanceJensenShannon(a, c));
	}

}
