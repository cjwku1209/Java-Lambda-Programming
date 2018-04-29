import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class MatrixMultiplication {

	public static void main(String[] args){
		try{
			Path path = Paths.get("p2input1.txt");
			long[][] matrix = Files.lines(path)
					.map(
							spacing -> Arrays.stream(spacing.split("\t"))
							.mapToLong(Long::parseLong)
							.toArray()
					)
					.toArray(long[][]::new);

			long[][] tmatrix = LongStream
					.range(0, matrix[0].length)
					.mapToObj(row -> LongStream.range(0, matrix.length)
							.map(col -> matrix[(int) col][(int) row]).toArray()
			).toArray(long[][]::new);

			long[][] result = Arrays.stream(matrix)
					.parallel()
					.map(r -> LongStream.range(0, tmatrix[0].length)
							.map(i -> LongStream.range(0, tmatrix.length)
									.map(j -> r[(int) j] * tmatrix[(int) j][(int) i])
									.sum()).
									toArray()
					).toArray(long[][]::new);

			File outputFile = new File("p2output1.txt");
			PrintWriter writer = new PrintWriter(outputFile);

			Arrays.stream(result)
					.forEach(a -> writer.println(Arrays.stream(a).mapToObj(i -> ((Long) i).toString())
							.collect(Collectors.joining("\t"))));
			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
