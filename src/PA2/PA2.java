//

//
package PA2;

import java.util.*;
import java.io.*;

public class PA2 {
	/*
	 * global int used for temporary storage; needed to return 2 items from 1
	 * method; this was my work around
	 */
	static int[][] hold = null;

	/* driver function and setting function */
	/*
	 * declares initial variables (ints strings files etc). take in the file input
	 * and stores it. sets source vertex and related info to being the source.
	 *
	 */
	public static void main(String[] args) {
		int VerticesToMake = 0;
		int SourceVertex = 0;
		int Edges = 0;

		/* essentially infinity */
		Integer MAX = Integer.MAX_VALUE;

		/*
		 * garbage variable for comments...when they were still apart of the assignment
		 */
		String temp = null;

		/* reads file */
		File FileIn = new File("cop3503-asn2-input.txt");
		Scanner File = null;

		try {
			File = new Scanner(FileIn);
		}

		catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		/*
		 * reads file again since you need to have 2 instances of a file; cannot set a
		 * var to the file at 1 certain point as a copy; must use another file
		 */
		File FileIn2 = new File("cop3503-asn2-input.txt");
		Scanner File2 = null;

		try {
			File2 = new Scanner(FileIn2);
		}

		catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		/* sets vars after file reading */
		VerticesToMake = File.nextInt();
		temp = File.nextLine();
		SourceVertex = File.nextInt();
		temp = File.nextLine();
		Edges = File.nextInt();
		temp = File.nextLine();

		/*
		 * figures out how many edges are needed in each vertex that will be used later
		 * for the creation of edge structures
		 */
		int[][] setup = UnderstandConnections(File, Edges, VerticesToMake);

		/* declares array of vertex data structures */
		VertexStructure[] AllVertices = new VertexStructure[VerticesToMake];

		/* creates number of vertices needed using my vertex structure data type */
		for (int i = 0; i < VerticesToMake; i++) {
			AllVertices[i] = new VertexStructure(i + 1, setup[i][0]);
		}

		/* creates array of edge strcuture data type */
		EdgeStructure[] AllEdges = new EdgeStructure[Edges];

		/* moves edges from input to the data type with the relevant info */
		for (int i = 0; i < Edges; i++) {
			AllEdges[i] = new EdgeStructure(hold[i][0]);
		}

		/*
		 * garbage collection string for when comments were still apart of the
		 * assignment
		 */
		String File2Garbage = null;

		for (int g = 0; g < 3; g++) {
			File2Garbage = File2.nextLine();
		}

		/* skips vertices from file, only caring about weights */
		for (int i = 0; i < Edges; i++) {
			AllEdges[i].VA = AllVertices[File2.nextInt() - 1];
			AllEdges[i].VB = AllVertices[File2.nextInt() - 1];
			String skip = File2.nextLine();
		}

		/* seets vertices edges and the relation */
		for (int i = 0; i < Edges; i++) {
			VertexStructure A = AllEdges[i].VA;
			VertexStructure B = AllEdges[i].VB;

			A.Edges[A.EdgeIndexVar] = AllEdges[i];
			A.EdgeIndexVar++;

			B.Edges[B.EdgeIndexVar] = AllEdges[i];
			B.EdgeIndexVar++;
		}

		/* declares output table; used for file output */
		int[][] OutputTable = new int[VerticesToMake][3];

		/*
		 * sets output table to the intial values - vertex: #-# - cost:
		 * Integer.max_value - from vert: _
		 */
		for (int i = 0; i < VerticesToMake; i++) {
			OutputTable[i][0] = AllVertices[i].label; // vertex:
			OutputTable[i][1] = MAX; // cost:
			OutputTable[i][2] = 0; // from vertex:
		}

		/* used for testing */
		// PrintTable(OutputTable, VerticesToMake);

		// ----------------------------------------------------------------------------------------------------------------------------------------------
		// dijkstras beings

		/* sets the souce vertex information in the output table */
		AllVertices[SourceVertex - 1].visited = true;
		OutputTable[SourceVertex - 1][1] = -1;
		OutputTable[SourceVertex - 1][2] = -1;

		// PrintTable(OutputTable, VerticesToMake);

		for (int i = 0; i < AllVertices[SourceVertex - 1].EdgeCount; i++) {
			if (AllVertices[SourceVertex - 1].Edges[i].VA.label != AllVertices[SourceVertex - 1].label
					&& AllVertices[SourceVertex - 1].Edges[i].VA.visited == false) {
				OutputTable[AllVertices[SourceVertex - 1].Edges[i].VA.label
						- 1][1] = AllVertices[SourceVertex - 1].Edges[i].weight;
				OutputTable[AllVertices[SourceVertex - 1].Edges[i].VA.label
						- 1][2] = AllVertices[SourceVertex - 1].Edges[i].VB.label;
			}

			else // if( AllVertices[ SourceVertex - 1 ].Edges[ i ].VB.label != AllVertices[
					// SourceVertex - 1 ].label && AllVertices[ SourceVertex - 1 ].Edges[ i
					// ].VB.visited == false )
			{
				OutputTable[AllVertices[SourceVertex - 1].Edges[i].VB.label
						- 1][1] = AllVertices[SourceVertex - 1].Edges[i].weight;
				OutputTable[AllVertices[SourceVertex - 1].Edges[i].VB.label
						- 1][2] = AllVertices[SourceVertex - 1].Edges[i].VA.label;
			}

			// PrintTable(OutputTable, VerticesToMake);
		}

		/* sets the output table information for all veritces except source */
		for (int i = 0; i < VerticesToMake - 1; i++) {
			/* calls a method to find the shortest path */
			int NextVert = SearchForShortestPath(AllVertices, OutputTable, VerticesToMake);

			AllVertices[NextVert - 1].visited = true;

			VertexStructure VisitingVert = AllVertices[NextVert - 1];
			VertexStructure OpposingVert = null;
			int LoopCount = VisitingVert.EdgeCount;

			for (int j = 0; j < LoopCount; j++) {
				EdgeStructure CheckEdge = VisitingVert.Edges[j];

				if (CheckEdge.VA != VisitingVert /* && CheckEdge.VA.visited == false */ ) {
					OpposingVert = VisitingVert.Edges[j].VA;
				}

				else if (CheckEdge.VB != VisitingVert /* && CheckEdge.VB.visited == false */ ) {
					OpposingVert = VisitingVert.Edges[j].VB;
				}

				else {
					// break;
				}

				if (CheckEdge.weight + OutputTable[VisitingVert.label - 1][1] < OutputTable[OpposingVert.label - 1][1]
						&& CheckEdge.weight >= 0) {
					OutputTable[OpposingVert.label - 1][1] = CheckEdge.weight + OutputTable[VisitingVert.label - 1][1];
					OutputTable[OpposingVert.label - 1][2] = VisitingVert.label;
				}

				else {
					// System.out.print("\nEvaluating edge of weight " + CheckEdge.weight + " with
					// VA " + CheckEdge.VA.label + " and VB " + CheckEdge.VB.label );
				}

				AllVertices[NextVert - 1].visited = true;

				// PrintTable(OutputTable, VerticesToMake);
			}
		}

		// ----------------------------------------------------------------------------------------------------------------------------------------------
		/* code is done, write the output information method */
		WriteToFile(OutputTable, VerticesToMake);
	}

	/*
	 * takes in the arrays of vertices and edges as well as how many vertices there
	 * are and returns the shortest path. It does so by test traversing and storing
	 * the lowest path it found
	 */
	private static int SearchForShortestPath(VertexStructure[] verts, int[][] out, int nov) {
		int CSP = Integer.MAX_VALUE;
		int NTR = -1;

		for (int i = 0; i < nov; i++) {
			if (verts[i].visited == false) {
				if (out[verts[i].label - 1][1] < CSP && out[verts[i].label - 1][1] >= 0) {
					// System.out.printf("\nShowing vertex %d has the lowest path of %d on check
					// %d\nvertex %d's cost is %d", verts[i].label, out[ verts[i].label - 1 ][1],
					// i+1,verts[i].label, out[verts[i].label-1][1]);
					CSP = out[verts[i].label - 1][1];
					NTR = verts[i].label;
				}
			}
		}
		return NTR;
	}

	/*
	 * takes in the current output table (should be the expected output) and writes
	 * it
	 */
	private static void WriteToFile(int[][] OutputTable, int VerticesToMake) {
		try {
			FileWriter results = new FileWriter("cop3503-asn2-output-Fugate-Andrew.txt");

			for (int i = 0; i < VerticesToMake + 1; i++) {
				if (i == 0) {
					results.write("" + VerticesToMake + "\n");
				}

				else {
					results.write(
							OutputTable[i - 1][0] + " " + OutputTable[i - 1][1] + " " + OutputTable[i - 1][2] + "\n");
				}
			}

			results.close();
		}

		catch (Exception e) {
			e.getStackTrace();
		}
	}

	/*
	 * takes in the output table and number of vertices and prints it...mostly used
	 * for testing
	 */
	private static void PrintTable(int[][] out, int nov) {

		System.out.println();
		System.out.printf("\nVertex: Cost: From:");

		for (int i = 0; i < nov; i++) {
			System.out.printf("\n%d %d %d", out[i][0], out[i][1], out[i][2]);
		}
	}

	/*
	 * takes in the file and number of edges and vertices and assigns the
	 * appropraite values that each should have in its structure
	 */
	private static int[][] UnderstandConnections(Scanner file, int noe, int nov) {
		int[][] setup = new int[nov][1];
		int[][] EdgeWeights = new int[noe][1];

		for (int i = 0; i < noe; i++) {
			int temp = file.nextInt();
			int temp2 = file.nextInt();
			int temp3 = file.nextInt();

			EdgeWeights[i][0] = temp3;

			setup[temp - 1][0]++;
			setup[temp2 - 1][0]++;
		}

		hold = EdgeWeights;

		return setup;
	}

	/* structure of the vertices */
	public static class VertexStructure {

		int label = 0;
		boolean visited = false;

		int EdgeIndexVar = 0;

		int EdgeCount = 0;
		EdgeStructure[] Edges = null;

		public VertexStructure(int label, int edgeCount) {
			super();
			this.label = label;
			EdgeCount = edgeCount;

			Edges = new EdgeStructure[edgeCount];
		}
	}

	/* structure of the edges */
	public static class EdgeStructure {
		// VA VB as in V1 V2 for Vertex1 and Vertex2 but avoiding too many numbers so
		// using alpha
		VertexStructure VA = null;
		VertexStructure VB = null;

		int weight = -1;

		public EdgeStructure(VertexStructure vA, VertexStructure vB) {
			super();
			VA = vA;
			VB = vB;
		}

		public EdgeStructure(int weight) {
			this.weight = weight;
		}

	}

}

//

//