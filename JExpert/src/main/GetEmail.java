package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class GetEmail {

	public void searchUserEmail(FileWriter fw, BufferedWriter bw, String projectPath, String fileName, String saveOutputEmailUser, String hashCommit)			throws IOException, InterruptedException {

		System.out.println(projectPath.replaceAll("output-brute", "DataSet").trim()+fileName);
		calc(fw,bw,hashCommit,projectPath.replaceAll("output-brute", "DataSet").trim()+fileName);

	}

	private static void calc(FileWriter fw, BufferedWriter bw, String hashCommit, String projectPath) throws IOException, InterruptedException {

		Process process = null;

		//String[] cmd = { "git --no-pager show -s --format='%an %ae' " + hashCommit };
		//String[] cmd = { "/bin/sh", "-c", "cd " + projectPath + "; git --no-pager show -s --format='%an' " + hashCommit};
		//String[] cmd = { "/bin/sh", "-c", "cd " + projectPath + "; git --no-pager show -s --format='%an %ae' " + hashCommit};
		
		String[] cmd = { "/bin/sh", "-c","cd " + projectPath + "; git log --pretty=format:'%h,%an,%ae' "};


		process = Runtime.getRuntime().exec(cmd);

		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		StringBuffer output = new StringBuffer();

		String line = "";
		while ((line = reader.readLine()) != null) {
			output.append(line + "\n");
			System.out.println(line + "\n");
			
			bw.write("https://github.com/"+line + "\n");
		}
		bw.close();
		fw.close();

	}
}
