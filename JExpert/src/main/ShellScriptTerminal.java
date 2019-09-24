package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;

public class ShellScriptTerminal {

	public static void main(String[] args) throws IOException, InterruptedException {

		BufferedWriter bw = null;
		FileWriter fw = null;

		String ambiente = args[0];

		System.out.println(ambiente);
		File diretorio = new File(ambiente);
		System.out.println("Directory Selected: " + diretorio.getName());
		File[] arquivos = diretorio.listFiles();

		if (arquivos != null) {
			int length = arquivos.length;

			for (int i = 0; i < length; ++i) {
				File f = arquivos[i];

				double a = i;
				double c = length;
				double value = a / c;
				double percent = ((value) * 100);


				System.out.println("Status " + percent);

				if (f.isFile()) {
				} else if (f.isDirectory()) {

					fw = new FileWriter("/home/labsoft/Downloads/Clone/output-brute/" + f.getName() + "_output.csv");
					System.out.println("Save in " + f.getAbsoluteFile() + "/" + f.getName() + ".csv");
					bw = new BufferedWriter(fw);
					calc(f, bw, fw);
				}
			}

		}

	}

	private static void calc(File arq, BufferedWriter bw, FileWriter fw) throws IOException, InterruptedException {

		File dir = new File(arq + "/");
		String[] extensions = new String[] { "java" };
		Process process = null;
		List<File> files = (List<File>) FileUtils.listFiles(dir, extensions, true);
		for (File file : files) {

			String[] cmd = { "/bin/sh", "-c",
					"cd " + dir.toString()
							+ "; git blame -l -c --date=iso --pretty=format:'\"%h\",\"%an\",\"%ad\",\"%s\"' "
							+ file.getCanonicalFile() };

			process = Runtime.getRuntime().exec(cmd);

			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			StringBuffer output = new StringBuffer();

			String line = "";
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
				// System.out.println(line + "\n");
				bw.write(line + "\n");
			}

		}

		bw.close();
		fw.close();
	}
}