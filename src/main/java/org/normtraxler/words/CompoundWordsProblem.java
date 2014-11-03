package org.normtraxler.words;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * @author normtraxler
 *
 */
public class CompoundWordsProblem {

	static class StringWrapper {

		private final String word;
		private final int matchedUntilIndex;

		public StringWrapper(String wordValue, int matchedUntilIndex) {
			this.word = wordValue;
			this.matchedUntilIndex = matchedUntilIndex;
		}

		@Override
		public String toString() {
			return "StringWrapper [word=" + word
					+ ", matchedUntilIndex=" + matchedUntilIndex
					+ "]";
		}

		public String getWord() {
			return word;
		}

		public int getMatchedUntilIndex() {
			return matchedUntilIndex;
		}
	}

	/**
	 * This Comparator is used for inserting into the PriorityQueue. 
	 * Paths of highest match index are put at the front of the Queue.
	 * If the matching Index is the same, sort by String length.
	 */
	static class QueueComparator implements Comparator<StringWrapper> {

		@Override
		public int compare(StringWrapper str1, StringWrapper str2) {

			// Note: not using any getXX methods to improve speed.

			// Sort by number of matches first, greatest to least:
			if (str1.getMatchedUntilIndex() != str2.getMatchedUntilIndex()) {
				return str2.getMatchedUntilIndex() - str1.getMatchedUntilIndex();
			}

			// Compare by length of the strings:
			return str2.getWord().length() - str1.getWord().length();
		}
	}

	private final Comparator<StringWrapper> PRIORITY_QUEUE_COMPARATOR = new QueueComparator();

	private final List<StringWrapper> wrappedWords;
	private final List<String> words;

	public CompoundWordsProblem(List<String> inputWords) {
		this.words = inputWords;
		wrappedWords = new ArrayList<>();
		for (String word : words) {
			wrappedWords.add(new StringWrapper(word, 0));
		}
	}

	public String findLongestMatch() {

		PriorityQueue<StringWrapper> wordQueue = new PriorityQueue<StringWrapper>(
				wrappedWords.size(), PRIORITY_QUEUE_COMPARATOR);
		wordQueue.addAll(wrappedWords);
		StringWrapper longestWord;
		int curLength;
		int testWordLength;

		while (wordQueue.peek() != null) {
			longestWord = wordQueue.poll();

			if (longestWord.getMatchedUntilIndex() == longestWord.getWord()
					.length()) {
				// Found the result:
				return longestWord.getWord();
			}

			// search through the shorter strings to find a startsWith match:

			for (String testWord : words) {
				// if it's the same word, skip:
				if (testWord == longestWord.getWord()) {
					continue;
				}

				// if the test word is a longer word, skip to same some cycles:
				curLength = longestWord.getWord().length()
						- longestWord.getMatchedUntilIndex();
				testWordLength = testWord.length();
				if (testWordLength > curLength) {
					continue;
				}

				if (longestWord.getWord().startsWith(testWord,
						longestWord.getMatchedUntilIndex())) {

					StringWrapper newStr = new StringWrapper(
							longestWord.getWord(),
							longestWord.getMatchedUntilIndex() + testWordLength);
					wordQueue.add(newStr);
				}
			}
		}

		return null;
	}

	public static void main(String[] args) throws Exception {

		// Thread.sleep(20000);
		long startTime = Calendar.getInstance().getTimeInMillis();

		FileInputStream fis = new FileInputStream("words.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

		List<String> input = new ArrayList<String>();

		String line = reader.readLine();
		while (line != null) {
			// System.out.println("read>>>" + line);
			line.trim();
			if (!"".equals(line)) {
				input.add(line);
			}
			line = reader.readLine();
		}
		reader.close();

		long endTime1 = Calendar.getInstance().getTimeInMillis();
		long phase1Time = (endTime1 - startTime);

		System.out.println("Phase 1 (Read words from file) elapsed time (ms) "
				+ phase1Time);

		CompoundWordsProblem cw = new CompoundWordsProblem(input);

		long endTime2 = Calendar.getInstance().getTimeInMillis();
		long phase2Time = (endTime2 - endTime1);

		System.out.println("Phase 2 (Find longest word) elapsed time (ms) "
				+ phase2Time);

		String result = cw.findLongestMatch();
		System.out.println("Found Longest word:" + result);

		// Thread.sleep(20000);

	}

	public static void main2(String[] args) throws Exception {

		// Thread.sleep(20000);
		long startTime = Calendar.getInstance().getTimeInMillis();

		List<String> input = new ArrayList<String>();
		input.add("cat");
		input.add("cats");
		input.add("catsdogcats");
		input.add("catxdogcatsrat");
		input.add("dog");
		input.add("dogcatsdog");
		input.add("hippopotamuses");
		input.add("rat");
		input.add("ratcatdogcat");

		long endTime1 = Calendar.getInstance().getTimeInMillis();
		long phase1Time = (endTime1 - startTime);

		System.out.println("Phase 1 (Read word from file) elapsed time (ms) "
				+ phase1Time);

		CompoundWordsProblem cw = new CompoundWordsProblem(input);

		long endTime2 = Calendar.getInstance().getTimeInMillis();
		long phase2Time = (endTime2 - endTime1);

		System.out.println("Phase 2 (Find longest work) elapsed time (ms) "
				+ phase2Time);

		String result = cw.findLongestMatch();
		System.out.println("Longest match:" + result);

	}

}
