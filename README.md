# COMP4111 Assignment4- Java Lambda Programming
Through the use of two important feature of Java 8: (1) the lambda	expressions	and	 (2)	 the	 streams	 API to complete three different task.

### Task 1- Prime Average
You	 are	 given	 a	 large	 list (millions) of	randomly	 generated	natural	 numbers (not	exceeding	java.lang.Long.MAX_VALUE),	your	task	is	to	identify	the prime	numbers	in the list	and	compute	their	average. You	are	required	to use the	higher-order	method	<b>filter</b> of	streams	to	finish	the	task.

<b>Input:</b>	a	text	file,	each	line	of	which	contains	a	natural	number

<b>Output:</b >the	total	number	of	prime	numbers	and	their	average (print	them	on	the	screen)

### Task 2- Matrix Multiplication
You	are	give	an integer	matrix	A of	size	M x N,	your task	is write	a	program	 to compute	A x A^(T) (A^(T) is	A’s	transpose). You	are	required	to	use	the	higher-order method <b>map</b> of	streams	to finish	the	task.

<b>Input: </b>a	text	file	consisting	of	N lines,	each	of	which	contains M integers	separated	by tabs

<b>Output: </b>the	resulting	matrix	of	size	M x M (please	save	the	result	to	a	text	file	of	the	same format	as	the input)

### Task 3- Word Count
Suppose	you	are	given a set	of	words,	your	task	is	to	count	how	often	each	word	occurs.	You	should	 transform	all	words	into	lower	 cases before	 counting.	You	are	 required	 to	 use	 the	higher-order	method	<b>reduce</b> of	streams	to	finish	the	task.

## Getting Started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites
- Java8

### Installing
Either download and unzip the file or
`git clone https://github.com/cjwku1209/Java-Lambda-Programming.git `

### Deployment
##### Specifying your input file
To specify which txt file you want to use as <b>input</b> for each task find the following line in the corresponding java class and insert the input file name.
```Java
  String fileName = "your-input-file-name.txt";
```
##### Specifying your output file
To specify which txt file you want to use as <b>output</b> for each task find the following line in the corresponding java class and insert your desired output file name.
```Java
  File outputFile = new File("your-output-file-name.txt");
```

##### Running and Compiling java class
After Specifying your input and output file. Run the following command to compile and run the program, where java-file is the java file you would like to run.

```
Calvin-MacBook-Pro:lambda-Programming cjwku$ javac java-file.java
Calvin-MacBook-Pro:lambda-Programming cjwku$ java java-file
```

## Task Solution Explanation
### Task 1- Prime average

#### List<Long> primeList- an array list to store all prime number
```Java
try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
		List<Long> primeList = new ArrayList<>();
		primeList = stream.filter(num -> isPrime(Long.parseLong(num)))
				.mapToLong(line-> Long.parseLong(line))
				.parallel()
				.boxed()
				.collect(Collectors.toList());
```
Every line of String in the input.txt file is stored in  `Stream<String> stream` that supports sequential and parallel aggregation operations.  Since each line contains a number that is not large than `java.lang.Long.MAX_VALUE`. So `filter(num -> isPrime(Long.parseLong(num))` is ran to convert each number from String to long and parsed to `isPrime` function that returns true if is a prime and false if is not. The true result are then collected and converted from LongStream to a list through the line `.collect(Collectors.toList())`. In between the lines of `filter` and `collect`. The String of the true is mapped from String to Long by the line `.mapToLong(line-> Long.parseLong(line))` and all the stream rans in parallel by the `.parallel()` and `.boxed` is used to convert the LongStream into Long.

#### Is Prime function (Copied from stackoverflow see Reference)
```Java
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
```
A brute force algorithm that checks all the cases with a linear running time of O(n).

#### Calculating the number of prime numbers and it's average.
```Java
Long average = primeList.parallelStream().mapToLong(i->i.longValue()).parallel().sum()/primeList.size();
System.out.print(primeList.size() + "\t" + average);
}
Average is calculated by the sum of all the primeList divided by the number of value in the list, which is `size()`. To calculate the sum of the primeList, we first have to convert the stream to do automatic unboxing and convert the value inside to Long by running `mapToLong(i->i.longValue())` and finding the sum by `sum()`. The function `parallelStream` and `parallel()` is used to make the stream run in parallel, so it can run faster.
```

### Task 2- Matrix Multiplication

#### Using Stream to reading input and initialize a 2D array of long[][]

```Java
String fileName = "p2input1.txt";
Path path = Paths.get(fileName);
long[][] matrix = Files.lines(path)
		.map(
				spacing -> Arrays.stream(spacing.split("\\s"))
				.mapToLong(Long::parseLong)
				.toArray()
		)
		.toArray(long[][]::new);
```
Since the input file for matrix is split by tabs in each of the matrix's row, we can use the regular expression to obtain each number in each row by doing `long[][] matrix = Files.lines(path).map(spacing -> Arrays.stream(spacing.split("\t"))`. After obtaining the numbers in the matrix's row we need to convert them to Long by running `.mapToLong(Long::parseLong)` and convert it to array by `.toArray()`. After running each row we can then convert these arrays into a 2D array by running `.toArray(long[][]::new)`.

#### Using input matrix to calculate Transpose matrix (Reference from stackoverflow, see Reference's Transpose Matrix )
```Java
long[][] tmatrix = LongStream
					.range(0, matrix[0].length)
					.mapToObj(row -> LongStream.range(0, matrix.length)
							.map(col -> matrix[(int) col][(int) row]).toArray()
			).toArray(long[][]::new);
```
this is equivalent to
```Java
  for (int col = 0; col < matrix[0].length; col++) {
    for (int row = 0; row < matrix.length; row ++)
        tatrix[row][col] = matrix[col][row];
  }
```
#### Matrix Multiplication (Copied from stackoverflow, see Matrix Multiplication using stream in Reference)
```Java
long[][] result = Arrays.stream(matrix)
					.parallel()
					.map(r -> LongStream.range(0, tmatrix[0].length)
							.map(i -> LongStream.range(0, tmatrix.length)
									.map(j -> r[(int) j] * tmatrix[(int) j][(int) i])
									.sum()).
									toArray()
					).toArray(long[][]::new);
```
To do matrix multiplication, we create a Stream over the rows of the first matrix, map each row to the result of multiplying it with the second matrix column and collect that back into a long[][].

This will calculate matrix * tmatrix and the result will be in `long[][] result`. For the multiplication of each row, we can't create a Stream with Arrays.stream of the second matrix since this would create a Stream over the rows when we need a Stream over the columns. To counteract that, we simply go back to using an IntStream over the indexes (.map(j -> r[(int) j] * tmatrix[(int) j][(int) i])).

#### Output result Matrix
```Java
File outputFile = new File("p2output2.txt");
PrintWriter writer = new PrintWriter(outputFile);
Arrays.stream(result)
		.forEach(a -> writer.println(Arrays.stream(a).mapToObj(i -> ((Long) i).toString())
				.collect(Collectors.joining("\t"))));
writer.close();
```
Using `.forEach()` to get the array for each result's matrix row and then `using a -> writer.println(Arrays.stream(a).mapToObj(i -> ((Long) i).toString()).collect(Collectors.joining("\t")))` to get each row's column and split each number by a tab. It then repeats till there are no more rows and `writer.println` then helps to print new line character (\n) after each row.


### Task 3- Word Count
This is copied and modified a little. (See Reference for Word Count using stream)
```Java
String fileName = "p3input1.txt";
Path path = Paths.get(fileName);
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
```
For every line of code in the input file, the line is split into words that does not contain any letter that is not alphabetical or numerical (`.flatMap(line -> Arrays.stream(line.trim().split("[^a-zA-Z0-9]")))`). Each word is then all converted to lower case by `.map(word -> word.toLowerCase())` and then filter out any words that have length small or equal to zero by `.filter(word -> word.length() > 0)`. The Map is then sorted by alphabetical order by comparing the String key values, which are the words (`.sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey()))`). Afterwards, reduced is used to compare the previous value and current value. It then uses reduce to compare the accumulative and current entry and replace the current entry. It first puts the current entry key value and then `compute` with the entry key an attempt to map the specific value and returns null if it cannot find anything. In our class it should return 1. Since it means that this specific word only occurs once which is now. On the other hand, if it returns a value that is not null. The value returned should be incremented by one, since the current one is also the same word.


## Acknowledgments
* Hong Kong University of Science and Technology's COMP4111 course Assignment4
* Using stream to read input: https://www.mkyong.com/java8/java-8-stream-read-a-file-line-by-line/
* isPrime function: https://stackoverflow.com/questions/20798391/java-isprime-function
* Transpose matrix: https://stackoverflow.com/questions/34861469/compact-stream-expression-for-transposing-double-matrix
* Matrix Multiplication using stream:  https://stackoverflow.com/questions/34774384/multiply-2-double-matrices-using-streams
* Word Count using streams: https://shekhargulati.com/2015/09/19/word-count-example-in-java-8/
