import java.util.*;
import java.io.*;

public class HeaderReader {

	Scanner scanner;
	
	private String headerLine;

	private ArrayList<String> headers = new ArrayList<String>();
	
	public HeaderReader(File file) {
		try {
			scanner = new Scanner(file);
			headerLine = scanner.nextLine();

			Scanner parse = new Scanner(headerLine);
			parse.useDelimiter(",");
			while(parse.hasNext()) {
				headers.add(parse.next());
			}
			parse.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showHeaders() {
		for(String header : headers) {
			System.out.println(header);
		}
	}

	public ArrayList<String> getHeaders() {
		return headers;
	}
}