package ustc.sse.datamining;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class ExecuteShellComand {

	public static void main(String[] args) {
		getFrequentpatternsCommand();

	}

	public static void getFrequentpatternsCommand() {
		ExecuteShellComand obj = new ExecuteShellComand();
		// String[] command = null;

		LinkedList<String[]> commands = new LinkedList<String[]>();

		boolean frequentpatternsExists = new File("frequentpatterns.seq")
				.exists();
		if (frequentpatternsExists) {
			commands.add(new String[] { "rm", "frequentpatterns.seq" });

		}
		boolean fListExists = new File("fList.seq").exists();
		if (fListExists) {
			commands.add(new String[] { "rm", "fList.seq" });

		}
		// if jps not run, hadoop namenode -format
		commands.add(new String[] { "jps" });
		commands.add(new String[] { "/usr/local/hadoop/bin/hadoop", "fs",
				"-put", "output.dat", "output.dat" });
		commands.add(new String[] { "/usr/local/mahout/bin/mahout", "fpg",
				"-i", "output.dat", "-o", "patterns", "-k", "10", "-method",
				"mapreduce", "-s", "2" });
		commands.add(new String[] { "/usr/local/hadoop/bin/hadoop", "fs",
				"-getmerge", "patterns/frequentpatterns",
				"frequentpatterns.seq" });
		commands.add(new String[] { "/usr/local/hadoop/bin/hadoop", "fs",
				"-get", "patterns/fList", "fList.seq" });
		commands.add(new String[] { "/usr/local/hadoop/bin/hadoop", "fs",
				"-rm", "output.dat" });

		for (String[] command : commands) {
			int val = obj.executeCommand(command, true);
			if (val != 0) {
				System.out.println(command[0] + command[command.length - 1]
						+ " failed!\n");
				break;
			}
		}

	}

	private static int executeCommand(String[] command, boolean isDebugModel) {

		StringBuffer output = new StringBuffer();

		Process p;
		int val = 0;
		try {

			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			val = p.exitValue();
			if (isDebugModel) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(p.getInputStream()));

				String line = "";
				while ((line = reader.readLine()) != null) {
					output.append(line + "\n");
				}

				System.out.println(output);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return val;
	}

}