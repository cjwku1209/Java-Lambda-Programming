import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class WordCount {

	public static void main(String[] args){
		try{
			Path path = Paths.get("p3input1.txt");
			Map<String, Integer> wordCount = Files.lines(path)
					.flatMap(line -> Arrays.stream(line.trim().split("[^a-zA-Z0-9]")))
					.map(word -> word.toLowerCase())
					.filter(word -> word.length() > 0)
					.map(word -> new AbstractMap.SimpleEntry<>(word, 1))
					.sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey()))
					.reduce(new LinkedHashMap<>(), (acc, entry) -> {
						acc.put(entry.getKey(), acc.compute(entry.getKey(), (k, v) -> v == null ? 1 : v + 1));
						return acc;
					}, (m1, m2) -> m1);
			File outputFile = new File("p3output1.txt");
			PrintWriter writer = new PrintWriter(outputFile);
			wordCount.forEach((k, v) -> writer.write(k + "\t" + v + "\n"));
			writer.close ();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
