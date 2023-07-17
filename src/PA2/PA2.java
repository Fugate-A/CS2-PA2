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
		
		Integer MAX = Integer.MAX_VALUE;
		
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
		
		int[][] setup = UnderstandConnections( File, Edges, VerticesToMake );
		
		VertexStructure[] AllVertices = new VertexStructure[ VerticesToMake ];
		
		for( int i = 0; i < VerticesToMake; i++ )
		{
			AllVertices[i] = new VertexStructure( i+1, setup[i][0] );
		}
		
		EdgeStructure[] AllEdges = new EdgeStructure[ Edges ];
		
		for( int i = 0; i < Edges; i++ )
		{
			AllEdges[i] = new EdgeStructure( hold[i][0] );
		}
		
		String File2Garbage = null;
		
		for( int g = 0; g < 3; g++ )
		{
			File2Garbage = File2.nextLine();
		}
		
		for( int i = 0; i < Edges; i++ )
		{
			AllEdges[i].VA = AllVertices[ File2.nextInt() - 1 ];
			AllEdges[i].VB = AllVertices[ File2.nextInt() - 1 ];
			String skip = File2.nextLine();
		}
		
		for( int i = 0; i < Edges; i++ )
		{
			VertexStructure A = AllEdges[i].VA;
			VertexStructure B = AllEdges[i].VB;
			
			A.Edges[A.EdgeIndexVar] = AllEdges[i];
			A.EdgeIndexVar++;
			
			B.Edges[B.EdgeIndexVar] = AllEdges[i];
			B.EdgeIndexVar++;
		}
		
		int[][] OutputTable = new int[VerticesToMake][3];
		
		for( int i = 0; i < VerticesToMake; i++ )
		{
			OutputTable[i][0] = AllVertices[i].label; //vertex:
			OutputTable[i][1] = MAX;	//cost:
			OutputTable[i][2] = 0;	//from vertex:
		}

		PrintTable(OutputTable, VerticesToMake);
		
		//----------------------------------------------------------------------------------------------------------------------------------------------
		//dijkstras
		
		AllVertices[ SourceVertex - 1 ].visited = true;
		OutputTable[ SourceVertex - 1 ][1] = -1;
		OutputTable[ SourceVertex - 1 ][2] = -1;
		
		PrintTable(OutputTable, VerticesToMake);
		
		for( int i = 0; i < AllVertices[ SourceVertex - 1 ].EdgeCount; i++  )
		{
			if( AllVertices[ SourceVertex - 1 ].Edges[ i ].VA.label != AllVertices[ SourceVertex - 1 ].label && AllVertices[ SourceVertex - 1 ].Edges[ i ].VA.visited == false )
			{
				OutputTable[ AllVertices[ SourceVertex - 1 ].Edges[ i ].VA.label - 1 ][1] = AllVertices[ SourceVertex - 1 ].Edges[ i ].weight;
				OutputTable[ AllVertices[ SourceVertex - 1 ].Edges[ i ].VA.label - 1 ][2] = AllVertices[ SourceVertex - 1 ].Edges[ i ].VB.label;		
			}
			
			else //if( AllVertices[ SourceVertex - 1 ].Edges[ i ].VB.label != AllVertices[ SourceVertex - 1 ].label && AllVertices[ SourceVertex - 1 ].Edges[ i ].VB.visited == false )
			{
				OutputTable[ AllVertices[ SourceVertex - 1 ].Edges[ i ].VB.label - 1 ][1] = AllVertices[ SourceVertex - 1 ].Edges[ i ].weight;
				OutputTable[ AllVertices[ SourceVertex - 1 ].Edges[ i ].VB.label - 1 ][2] = AllVertices[ SourceVertex - 1 ].Edges[ i ].VA.label;
			}
			
			PrintTable(OutputTable, VerticesToMake);
		}
		
		
		System.out.printf("\n\n\n\n\nAFTER SOURCE EVALUATION\n\n\n\n");
		
		
		for( int i = 0; i < VerticesToMake - 1; i++ )
		{
			
			int NextVert = SearchForShortestPath( AllVertices, OutputTable, VerticesToMake );			
			
			AllVertices[ NextVert - 1 ].visited = true;
			
			VertexStructure VisitingVert = AllVertices[ NextVert - 1 ];
			VertexStructure OpposingVert = null; 
			int LoopCount = VisitingVert.EdgeCount;
			
			for( int j = 0; j < LoopCount ; j++ )
			{
				EdgeStructure CheckEdge = VisitingVert.Edges[ j ];
				
				if( CheckEdge.VA != VisitingVert /*&& CheckEdge.VA.visited == false*/ )
				{
					OpposingVert = VisitingVert.Edges[ j ].VA;
				}
				
				else if( CheckEdge.VB != VisitingVert /*&& CheckEdge.VB.visited == false*/ )
				{
					OpposingVert = VisitingVert.Edges[ j ].VB;
				}
				
				else
				{
					//break;
				}
				
				if( CheckEdge.weight + OutputTable[VisitingVert.label - 1][1] < OutputTable[OpposingVert.label - 1][1] && CheckEdge.weight >= 0 )
				{
					OutputTable[OpposingVert.label - 1][1] = CheckEdge.weight + OutputTable[VisitingVert.label - 1][1];
					OutputTable[OpposingVert.label - 1][2] = VisitingVert.label;
				}
				
				else
				{
					System.out.print("\nEvaluating edge of weight " + CheckEdge.weight + " with VA " + CheckEdge.VA.label + " and VB " + CheckEdge.VB.label );
				}
				
				AllVertices[ NextVert - 1 ].visited = true;
				
				PrintTable(OutputTable, VerticesToMake);
			}
		}
		
		//----------------------------------------------------------------------------------------------------------------------------------------------		
		
       WriteToFile( OutputTable, VerticesToMake );
	}

	private static int SearchForShortestPath( VertexStructure[] verts, int[][] out, int nov )
	{
		int CSP = Integer.MAX_VALUE;
		int NTR = -1;
		
		for( int i = 0; i < nov; i++ )
		{
			if( verts[i].visited == false )
			{
				if( out[ verts[i].label - 1 ][1] < CSP &&  out[ verts[i].label - 1 ][1] >= 0 )
				{
					System.out.printf("\nShowing vertex %d has the lowest path of %d on check %d\nvertex %d's cost is %d", verts[i].label, out[ verts[i].label - 1 ][1], i+1,verts[i].label, out[verts[i].label-1][1]);
					CSP = out[ verts[i].label - 1 ][1];
					NTR = verts[i].label;
				}
			}
		}		
		return NTR;
	}

	private static void WriteToFile(int[][] OutputTable, int VerticesToMake)
	{
		 try
	        {
	            FileWriter results = new FileWriter("cop3503-asn2-output-Fugate-Andrew.txt");
	            
	            for( int i = 0; i < VerticesToMake + 1; i++ )
	            {
	            	if( i == 0 )
	            	{
	            		results.write("" + VerticesToMake + "\n" );
	            	}
	            	
	            	else
	            	{
	            		results.write( OutputTable[i-1][0] + " " + OutputTable[i-1][1] + " " + OutputTable[i-1][2] + "\n" );
	            	}
	            }
	            
	            results.close();
	        }
	  
	        catch (Exception e)
	        {
	            e.getStackTrace();
	        }
	}

	private static void PrintTable( int[][] out, int nov )
	{
		
		System.out.println();
		System.out.printf("\nVertex: Cost: From:");
		
		for( int i = 0; i < nov; i++ )
		{
			System.out.printf("\n%d %d %d", out[i][0], out[i][1], out[i][2] );
		}
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