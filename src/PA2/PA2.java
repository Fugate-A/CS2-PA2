//

//

package PA2;

import java.util.*;
import java.io.*;

public class PA2
{
	public static void main(String[] args)
	{
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
		
		do
		{
			System.out.println( File.nextLine() );
		} while( File.hasNextLine() );
		
	}
}

//

//


