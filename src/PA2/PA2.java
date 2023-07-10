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
		
		VerticesToMake = File.nextInt();				
		temp = File.nextLine();
		SourceVertex = File.nextInt();
		temp = File.nextLine();
		Edges = File.nextInt();
		temp = File.nextLine();
		
		System.out.println();
		System.out.printf("Source Vertex is %d", SourceVertex );
		
		
		int[][] setup = UnderstandConnections( File, Edges, VerticesToMake );
		
		for( int i = 0; i < VerticesToMake; i++ )
		{
			System.out.println();
			System.out.printf("Vertex %d has %d edges leaving it", i+1, setup[i][0] );
		}
		
		for( int i = 0; i < Edges; i++ )
		{
			System.out.println();
			System.out.printf("Edge %d has a weight of %d", i+1, hold[i][0] );
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
		
		EdgeWeights( EdgeWeights );
		
		return setup;
	}

	private static void EdgeWeights( int[][] store )
	{
		hold = store;
	}
}

//

//


