import java.io.*;
import java.util.*;

public class Configurator {
	class Latitude {
		String name;

		public Latitude(String name) {
			this.name = name;
		}

		public String toString() {
			if(name == null) return "";
			else return String.format("\"Latitude\": {\n\t\"name\": \"%s\"\n}", name);
		}
	}

	class Longitude {
		String name;

		public Longitude(String name) {
			this.name = name;
		}

		public String toString() {
			if(name == null) return "";
			else return String.format("\"Longitude\": {\n\t\"name\": \"%s\"\n}", name);
		}
	}

	class Time {
		String name;
		String timezone;

		public Time(String name) {
			this.name     = name;
			this.timezone = "GMT";
		}

		public String toString() {
			if(name == null) return "";
			else return String.format("\"Time\": {\n\t\"name\": \"%s\",\n\t\"timezone\": \"%s\"\n}", name, timezone);
		}
	}

	class RelatedData {
		ArrayList<String> target;
		ArrayList<String> related;
		String method;

		public RelatedData(ArrayList<String> target, ArrayList<String> related, String method) {
			this.target  = target;
			this.method  = method;
			this.related = related;
		}

		public String toString() {
			if(target.size() == 0 && related.size() == 0 && method == null) return "";

			String m = String.format("\"method\": \"%s\"", (method == null)? "others":method);

			String t = "\"target\": [";
			for(int i = 0; i < target.size(); i++) {
				String s = String.format("\"%s\"", target.get(i));
				t += s;
				if(i != target.size() - 1) {
					t += ", ";
				}
			}
			t += "]";

			String r = "\"related\": [";
			for(int i = 0; i < related.size(); i++) {
				String s = String.format("\"%s\"", related.get(i));
				r += s;
				if(i != related.size() - 1) {
					r += ", ";
				}
			}
			r += "]";

			return String.format("\"RelatedData\":[\n\t{\n\t\t%s,\n\t\t%s,\n\t\t%s\n\t}\n]", t, r, m);
		}
	}

	Latitude    lat;
	Longitude   lon;
	Time        time;
	RelatedData data;

	public void setLatitude(String name) {
		lat = new Latitude(name);
	}

	public void setLongitude(String name) {
		lon = new Longitude(name);
	}

	public void setTime(String name) {
		time = new Time(name);
	}

	public void setRelatedData(ArrayList<String> target, ArrayList<String> related, String method) {
		data = new RelatedData(target, related, method);
	}

	public void generateJsonFile() {
		String content = "{\n\n";
		String[] config = new String[4];
		config[0] = lat.toString();
		config[1] = lon.toString();
		config[2] = time.toString();
		config[3] = data.toString();

		boolean isFirst = true;
		for(int i = 0; i < config.length; i++) {
			if(! config[i].equals("")) {
				if(! isFirst) {
					content += ",\n";
				}
				content += config[i];
				isFirst = false;
			}
		}
		content += "\n\n}";
		System.out.println(content);
		try {
			PrintWriter writer = new PrintWriter(new FileOutputStream("column_config.json"));
			writer.println(content);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}