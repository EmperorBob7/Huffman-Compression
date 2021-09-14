import java.io.PrintStream;
import java.util.PriorityQueue;
import java.util.Scanner;

public class HuffmanCode {
	private HuffmanNode root = null;

	public HuffmanCode(int[] count) {
		PriorityQueue<HuffmanNode> queue = new PriorityQueue<>();
		for (int i = 0; i < count.length; i++) {
			if (count[i] == 0)
				continue;
			queue.add(new HuffmanNode(count[i], i));
		}
		while (queue.size() > 1) {
			root = new HuffmanNode(queue.poll(), queue.poll());
			queue.offer(root);
		}
	}

	public HuffmanCode(Scanner key) {
		root = new HuffmanNode(0, -1);
		while (key.hasNext()) {
			insert(root, key.nextInt(), key.next());
		}
	}

	private void insert(HuffmanNode root, int c, String code) {
		if (code.length() == 1) {
			if (code.equals("0"))
				root.left = new HuffmanNode(0, c);
			else
				root.right = new HuffmanNode(0, c);
			return;
		}

		if (code.charAt(0) == '0') {
			if (root.left == null)
				root.left = new HuffmanNode(0, -1);
			insert(root.left, c, code.substring(1));
		} else {
			if (root.right == null)
				root.right = new HuffmanNode(0, -1);
			insert(root.right, c, code.substring(1));
		}
	}

	public void save(PrintStream p) {
		recurse(root, "", p);
	}

	private void recurse(HuffmanNode r, String s, PrintStream p) {
		if (r.left == null && r.right == null) {
			p.print(r.character);
			p.print('\n');
			p.print(s);
			p.print('\n');
			return;
		}
		recurse(r.left, s + "0", p);
		recurse(r.right, s + "1", p);
	}

	public void translate(BitInputStream input, PrintStream output) {
		HuffmanNode r = root;
		while (input.hasNextBit()) {
			if (input.nextBit() == 0) {
				r = r.left;
			} else {
				r = r.right;
			}
			if (r.left == null && r.right == null) {
				output.write((char) r.character);
				r = root;
			}
		}
	}
}

class HuffmanNode implements Comparable<HuffmanNode> {
	int frequency, character;
	HuffmanNode left, right;

	public HuffmanNode(int frequency, int c) {
		this.frequency = frequency;
		this.character = c;
	}

	public HuffmanNode(HuffmanNode left, HuffmanNode right) {
		this.frequency = left.frequency + right.frequency;
		this.left = left;
		this.right = right;
	}

	@Override
	public int compareTo(HuffmanNode node) {
		return Integer.compare(frequency, node.frequency);
	}

	public String toString() {
		return String.format("[%c : %d]", (char) character, frequency);
	}
}