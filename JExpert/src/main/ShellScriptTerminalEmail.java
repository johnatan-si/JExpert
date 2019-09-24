package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class ShellScriptTerminalEmail {

	public static void main(String[] args) throws IOException, InterruptedException {

		BufferedWriter bw = null;
		FileWriter fw = null;

		JFileChooser fc = new JFileChooser();
		String path = null, saveCsv = null;
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int res = fc.showOpenDialog(null);

		if (res == JFileChooser.APPROVE_OPTION) {

			File diretorio = fc.getSelectedFile();
			JOptionPane.showMessageDialog(null, "Directory Selected: " + diretorio.getName());

			File[] arquivos = diretorio.listFiles();

			/*
			 * ProgressBar progressBar = new ProgressBar();
			 * progressBar.desenho(arquivos.length);
			 */

			if (arquivos != null) {
				int length = arquivos.length;

				for (int i = 0; i < length; ++i) {
					File f = arquivos[i];

					double a = i;
					double c = length;
					double value = a / c;
					double percent = ((value) * 100);

					// status
					// progressBar.imprimir(i);

					System.out.println("Status " + percent);

					if (f.isFile()) {
					} else if (f.isDirectory()) {
						fw = new FileWriter("/media/labsoft/SAMSUNG/Projects/email/" + f.getName() + "_output.csv");
						System.out.println("Save in " + f.getAbsoluteFile() + "/" + f.getName() + ".csv");
						bw = new BufferedWriter(fw);
						calc(f, bw, fw);
					}
				}
				// progressBar.setProgressBarVisibility(false);
				
			}
		}
	}

	private static void calc(File arq, BufferedWriter bw, FileWriter fw) throws IOException, InterruptedException {

		File[] arquivos = arq.listFiles();
		Process process = null;

		if (arquivos != null) {
			int length = arquivos.length;

			for (int i = 0; i < length; ++i) {
				File f = arquivos[i];

				if (!f.isFile()) {
					//String[] cmd = { "/bin/sh", "-c", "cd " + f.toString() + "; git log --pretty=format:'%h;%an;%ae'" };
					
					String[] cmd = { "/bin/sh", "-c", "cd " + f.toString() + "; git log --pretty=format:'%h;%an;%ae'" };

					// git log --pretty=format:%h,%an,%ae,%s >
					// /media/labsoft/SAMSUNG/Projects/file.csv

					process = Runtime.getRuntime().exec(cmd);

					BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
					StringBuffer output = new StringBuffer();

					// p.waitFor();
					String line = "";
					while ((line = reader.readLine()) != null) {
						output.append(line + "\n");
						// System.out.println(line + "\n");
						bw.write(line + "\n");
					}
				}
			}
		}
//git log --pretty=format:%h,%an,%ae,%s > /media/labsoft/SAMSUNG/Projects/file.csv
//git blame -l -c --date=iso --pretty=format:'\"%h\",\"%an\",\"%ad\",\"%s\",\"%ae\"'
		bw.close();
		fw.close();
	}
}