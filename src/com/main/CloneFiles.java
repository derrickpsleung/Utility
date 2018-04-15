package com.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CloneFiles {

	public static void main(String args[]) {

		String srcPath = "D:\\FPS repo\\ccb2\\CM\\";

		String searchPattern = "RM175C*.j*";

		String strToFind = "RM175C";
		String strToReplace = "RM175";

		try {
			String cmd1 = String.format("cd \"%s\" && dir /s /b %s", srcPath, searchPattern);
			String[] command1 = { "CMD", "/C", cmd1 };
			List<String> rsltList = runCmd(command1, "list result:");

			for (String rslt : rsltList) {

				System.out.println("XXXXXXXXXXXXXXXXXXX");
				System.out.println(rslt);

				String relativePath = rslt.substring(0, rslt.lastIndexOf("\\") + 1);
				String targetFileName = rslt.substring(rslt.lastIndexOf("\\") + 1).replace(strToFind, strToReplace);

				copyFile(rslt, relativePath + targetFileName);

				Path path = Paths.get(relativePath + targetFileName);
				Charset charset = StandardCharsets.UTF_8;
				String content = new String(Files.readAllBytes(path), charset);
				content = content.replaceAll(strToFind, strToReplace);
				Files.write(path, content.getBytes(charset));

			}

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}

	private static void copyFile(String fromPath, String toPath) throws IOException, InterruptedException {
		String cmd = String.format("copy /Y \"%s\" \"%s\"", fromPath, toPath);
		System.out.println("cmd: " + cmd);
		String[] command = { "CMD", "/C", cmd };
		runCmd(command, "copy result:");
	}

	private static List<String> runCmd(String[] command, String msg) throws IOException, InterruptedException {

		List<String> rsltLst = new ArrayList<String>();
		ProcessBuilder pb = new ProcessBuilder(command);
		pb.redirectErrorStream(true);

		Process proc = pb.start();

		BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		System.out.println(msg);
		String s = null;
		while ((s = stdInput.readLine()) != null) {
			System.out.println(s);
			rsltLst.add(s);
		}

		proc.waitFor();

		return rsltLst;
	}

}
