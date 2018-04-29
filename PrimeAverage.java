import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PrimeAverage {

	public static boolean isPrime(long num){
		if(num < 2) return false;
		if(num == 2 || num == 3) return true;
		if(num%2 == 0 || num%3 == 0) return false;
		long sqrtN = (long)Math.sqrt(num)+1;
		for(long i = 6L; i <= sqrtN; i += 6) {
			if(num%(i-1) == 0 || num%(i+1) == 0) return false;
		}
		return true;
	}

	public static void main(String[] args){
		String fileName = "p1input2.txt";
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
			List<Long> primeList = new ArrayList<>();
			primeList = stream.filter(num -> isPrime(Long.parseLong(num)))
					.mapToLong(line-> Long.parseLong(line))
					.parallel()
					.boxed()
					.collect(Collectors.toList());
			Long average = primeList.parallelStream().mapToLong(i->i.longValue()).parallel().sum()/primeList.size();
			System.out.println(primeList.size() + "\t" + average);


		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
