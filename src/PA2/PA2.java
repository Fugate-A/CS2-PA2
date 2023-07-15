//

//

package PA2;

import java.util.*;
import java.io.*;

public class PA2
{
	static int[][] hold = null;
	
	public static void main(String[] args)
	{
		int VerticesToMake = 0;
		int SourceVertex = 0;
		int Edges = 0;
		
		String temp = null;
		
		File FileIn = new File("cop3503-asn2-input.txt");
		Scanner File = null;
				
		try
		{
			File = new Scanner ( FileIn );
		}
		
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		
		File FileIn2 = new File("cop3503-asn2-input.txt");
		Scanner File2 = null;
				
		try
		{
			File2 = new Scanner ( FileIn2 );
		}
		
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		
		VerticesToMake = File.nextInt();				
		temp = File.nextLine();
		SourceVertex = File.nextInt();
		temp = File.nextLine();
		Edges = File.nextInt();
		temp = File.nextLine();
				
		System.out.println();
		System.out.printf("Source Vertex is %d", SourceVertex );
		
		int[][] setup = UnderstandConnections( File, Edges, VerticesToMake );
		
		VertexStructure[] AllVertices = new VertexStructure[ VerticesToMake ];
		
		for( int i = 0; i < VerticesToMake; i++ )
		{
			System.out.println();
			System.out.printf("Vertex %d has %d edges leaving it", i+1, setup[i][0] );
			AllVertices[i] = new VertexStructure( i+1, setup[i][0] );
		}
		
		EdgeStructure[] AllEdges = new EdgeStructure[ Edges ];
		
		for( int i = 0; i < Edges; i++ )
		{
			System.out.println();
			System.out.printf("Edge %d has a weight of %d", i+1, hold[i][0] );
			AllEdges[i] = new EdgeStructure( hold[i][0] );
		}
		
		String File2Garbage = null;
		for( int g = 0; g < 3; g++ )
		{
			File2Garbage = File2.nextLine();
		}
		
		System.out.println();
		System.out.println( AllVertices[0].Edges.length );
		
		
	}

	private static int[][] UnderstandConnections( Scanner file, int noe, int nov )
	{
		int[][] setup = new int[nov][1];
		int[][] EdgeWeights = new int[noe][1];
		
		for( int i = 0; i < noe; i++ )
		{
			int temp = file.nextInt();
			int temp2 = file.nextInt();
			int temp3 = file.nextInt();
			
			EdgeWeights[i][0] = temp3;
			
			setup[temp-1][0]++;
			setup[temp2-1][0]++;
		}
		
		hold = EdgeWeights;
		
		return setup;
	}
	
	public static class VertexStructure
	{
		
		int label = 0;
		boolean visited = false;
		
		int EdgeIndexVar = 0;
		
		int EdgeCount = 0;
		EdgeStructure[] Edges = null;
		
		public VertexStructure( int label, int edgeCount )
		{
			super();
			this.label = label;
			EdgeCount = edgeCount;
			
			Edges = new EdgeStructure[ edgeCount ];  
		}
	}
	
	public static class EdgeStructure
	{
		//VA VB as in V1 V2 for Vertex1 and Vertex2 but avoiding too many numbers so using alpha
		VertexStructure VA = null;
		VertexStructure VB = null;
		
		int weight = -1;
		
		public EdgeStructure( VertexStructure vA, VertexStructure vB )
		{
			super();
			VA = vA;
			VB = vB;
		}

		public EdgeStructure( int weight )
		{
			this.weight = weight;
		}

	}
	
}

//

//


