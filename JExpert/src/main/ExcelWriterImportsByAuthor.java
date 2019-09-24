package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

public class ExcelWriterImportsByAuthor {

	// essa classe faz a leitura da pasta output-withoutComments
	private static String[] columns = { "Author", "Imports Framework", "Imports General - Commmits FW",
			"Total of  Commits", "LOC Altered Framework", "Ratio  LOC/Import Total",
			"# Loc in Relation the  Framework" };
/*
B=> quantidade de imports únicos do framework+
D=> quantidade de commits que o desenvolvedor fez+
G=> F*E (proporção de linhas de framework em relação as linhas totais dos commits)+
*/
	

	static HashMap<String, ArrayList<String>> strings = new HashMap<String, ArrayList<String>>();

	public static void main(String[] args) throws IOException, InvalidFormatException, InterruptedException {
///media/labsoft/SAMSUNG/Projects/OutputBrute/hadoop/-pluralsight--spring-with-jpa-and-hibernate-38_output.csv
		Utils utils = new Utils();
		String frameworkSearch="hibernate";///media/labsoft/SAMSUNG/Projects/OutputBrute/hadoop
		GetEmail getEmail = new GetEmail();
		String saveOutputGeneral="/media/labsoft/SAMSUNG/Projects/output-Clean/hadoop";
		String saveOutputEmailUser="/media/labsoft/SAMSUNG/Projects/email/hadoop";
		//String directoryDatasetJava="DataSet";// nome do diretorio que possui codigo fonte  java 
		
		BufferedWriter bw = null;
		FileWriter fw = null;
		
		
		Map<String, Integer> mapCountFramework = new HashMap();
		Map<String, Integer> importsGeneral = new HashMap();
		Map<String, Integer> mapCountAllCommits = new HashMap();
		Multimap<String, String> hasMapimportsFramework = ArrayListMultimap.create();

		Multimap<String, String> importsCommmitsFw = ArrayListMultimap.create();

		Map<String, String> hasMapimportsGeneral = new HashMap();
		Multimap<String, String> allCommitsSource = ArrayListMultimap.create();

		Multimap<String, String> uniqueCommitDevelop = ArrayListMultimap.create();


		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = "	";

		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int res = fc.showOpenDialog(null);

		if (res == JFileChooser.APPROVE_OPTION) {

			File diretorio = fc.getSelectedFile();
			JOptionPane.showMessageDialog(null, "Directory Selected: " + diretorio.getName());
			File[] arquivos = diretorio.listFiles();

			ProgressBar progressBar = new ProgressBar();
			progressBar.desenho(arquivos.length);

			if (arquivos != null) {
				int length = arquivos.length;
				for (int i = 0; i < length; ++i) {
					File f = arquivos[i];

					double a = i;
					double c = length;
					double value = a / c;
					double percent = ((value) * 100);

					progressBar.imprimir(i);

					if (f.isFile()) {

						// Create a Workbook
						Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file
						CreationHelper createHelper = workbook.getCreationHelper();

						// Create a Sheet
						Sheet sheetFrame = workbook.createSheet(frameworkSearch);

						// Create a Font for styling header cells
						Font headerFont = workbook.createFont();
						headerFont.setBold(true);
						headerFont.setFontHeightInPoints((short) 14);
						headerFont.setColor(IndexedColors.RED.getIndex());

						// Create a CellStyle with the font
						CellStyle headerCellStyle = workbook.createCellStyle();
						headerCellStyle.setFont(headerFont);

						// Create a Row
						Row headerRowFrame = sheetFrame.createRow(0);
						Boolean flag = false;
						// Creating cells
						for (int b = 0; b < columns.length; b++) {

							Cell cell2 = headerRowFrame.createCell(b);
							cell2.setCellValue(columns[b]);
							cell2.setCellStyle(headerCellStyle);
						}

						try {

							br = new BufferedReader(new FileReader(f.getAbsoluteFile()));
							//System.out.println(f.getAbsoluteFile());
							
							fw = new FileWriter(saveOutputEmailUser + f.getName() + "_email.csv",true);
							bw = new BufferedWriter(fw);
							while ((line = br.readLine()) != null) {
								// use comma as separator
								String[] country = line.split(cvsSplitBy);
								String replaced = country[1].replaceAll("[()]", "");
								country[3] = country[3].replaceAll("[()]", "").replaceAll("[0-9]", "");

								// diferença em meses <=36 meses, ou seja 3 anos
								if (utils.calcData(country[2].substring(0, 10).trim()) <= 36) {
									allCommitsSource.put(country[1].replaceAll("[()]", "").trim(), country[0].trim());
									// System.out.println("All commits "+allCommitsSource.values());
									// cria a planilha com todos os imports daquele usuário
									if (isContain(country[3].toLowerCase(), "import")) {

										country[3] = country[3].replace("import", "").trim();
										// cria planilha com imports de angular no geral / com duplicados
										if (isContain(country[3].toLowerCase(), frameworkSearch) || country[3].toLowerCase().contains(frameworkSearch) || StringUtils.containsIgnoreCase(country[3], frameworkSearch)	|| Pattern.compile(Pattern.quote(frameworkSearch), Pattern.CASE_INSENSITIVE).matcher(country[3].toString()).find()) {

											incrementValueAuthor(mapCountFramework,	country[1].replaceAll("[()]", "").trim());
											hasMapimportsFramework.put(country[1].replaceAll("[()]", "").trim(),country[0].trim());

											flag = true;
											getEmail.searchUserEmail(fw,bw,diretorio.getPath()+"/",f.getName().replaceAll("_output.csv", "/").trim(),saveOutputEmailUser, country[0]);
											
										}
										incrementValueAuthor(importsGeneral,country[1].replaceAll("[()]", "").trim().toString());
										// hasMapimportsGeneral.put(country[1].replaceAll("[()]",//
										// "").trim(),country[0].trim());
										importsCommmitsFw.put(country[1].replaceAll("[()]", "").trim(),country[0].trim());
									}
									incrementValueAuthor(mapCountAllCommits,country[1].replaceAll("[()]", "").trim().toString());
								}
							}
							bw.close();
							fw.close();

							Map<String, Integer> countImportsCommmitsFW = new HashMap();

							for (String valueFr : Sets.newHashSet(hasMapimportsFramework.values())) {
								int count = Collections.frequency(new ArrayList<String>(importsCommmitsFw.values()),
										valueFr);
								if (count > 0) {
									String author = getKeyFromValue(importsCommmitsFw, valueFr).toString();

									if (countImportsCommmitsFW.get(author) == null
											|| countImportsCommmitsFW.get(author) == 0) {
										countImportsCommmitsFW.put(author, count);
									} else {
										countImportsCommmitsFW.put(author, countImportsCommmitsFW.get(author) + count);
									}
								}
							}

							Map<String, Integer> sourceCodeRelationFrame = new HashMap();
							// para remover registros duplicados
							for (String valueFr : Sets.newHashSet(hasMapimportsFramework.values())) {
								int count = Collections.frequency(new ArrayList<String>(allCommitsSource.values()),
										valueFr);
								if (count > 0) {
									String author = getKeyFromValue(allCommitsSource, valueFr).toString();
									if (sourceCodeRelationFrame.get(author) == null
											|| sourceCodeRelationFrame.get(author) == 0) {
										sourceCodeRelationFrame.put(author, count);
									} else {
										sourceCodeRelationFrame.put(author,
												sourceCodeRelationFrame.get(author) + count);
									}
								}
							}

							// System.out.println(Sets.newHashSet(allCommitsSource.values()));
							Map<String, Integer> uniqueCommits = new HashMap();
							// para remover registros duplicados
							// System.out.println("All commits "+
							// Sets.newHashSet(allCommitsSource.values()));
							// System.out.println("Frame"+
							// Sets.newHashSet(hasMapimportsFramework.values())) ;

							for (String valueFr : Sets.newHashSet(hasMapimportsFramework.values())) {
								int count = Collections.frequency(new ArrayList<String>(Sets.newHashSet(hasMapimportsFramework.values())),valueFr);
								if (count > 0) {
									String author = getKeyFromValue(hasMapimportsFramework, valueFr).toString();

									if (uniqueCommits.get(author) == null || uniqueCommits.get(author) == 0) {
										uniqueCommits.put(author, count);
									} else {
										uniqueCommits.put(author, uniqueCommits.get(author) + count);
									}
								}
							}

							if (flag) {

								int rowNum = 1;
								float ratioLocImportGeneral;
								float locRelationFramework;
								float locAlteredFramework;

								DecimalFormat df;
								try {
									for (String key : allCommitsSource.keySet()) {
										System.out.println(key);

										if (mapCountFramework.containsKey(key)) {
											Row row = sheetFrame.createRow(rowNum++);
											row.createCell(0).setCellValue(key);

											if (mapCountFramework.containsKey(key)) {
												row.createCell(1).setCellValue(mapCountFramework.get(key));
											}
											if (countImportsCommmitsFW.containsKey(key)) {
												row.createCell(2).setCellValue(countImportsCommmitsFW.get(key));
											}

											/*
											 * if (importsGeneral.containsKey(key)) { //Imports General-All commits
											 * row.createCell(3).setCellValue(importsGeneral.get(key)); }
											 */
											if (uniqueCommits.containsKey(key)) {
												row.createCell(3).setCellValue(uniqueCommits.get(key));
											}
											/*
											 * LOC altered in Total if (mapCountAllCommits.containsKey(key)) {
											 * row.createCell(4).setCellValue(mapCountAllCommits.get(key)); }
											 */
											if (sourceCodeRelationFrame.containsKey(key)) {
												// row.createCell(6).setCellValue(sourceCodeRelationFrame.get(key));
												row.createCell(4).setCellValue(sourceCodeRelationFrame.get(key)
														- countImportsCommmitsFW.get(key));
											}
											if (mapCountAllCommits.containsKey(key) & importsGeneral.containsKey(key)
													&& mapCountFramework.containsKey(key)) {

												locAlteredFramework = sourceCodeRelationFrame.get(key)
														- countImportsCommmitsFW.get(key);

												ratioLocImportGeneral = (float) mapCountFramework.get(key)
														/ (float) countImportsCommmitsFW.get(key);
												locRelationFramework = (float) ratioLocImportGeneral
														* (float) locAlteredFramework;

												row.createCell(5).setCellValue(round((float) ratioLocImportGeneral, 2));
												row.createCell(6).setCellValue(round(locRelationFramework, 2));

											}
										}
									}
								} catch (Exception ex) {
									System.out.println(ex);
								}

								for (int x = 0; x < columns.length; x++) {
									sheetFrame.autoSizeColumn(x);
								}

								FileOutputStream fileOut = new FileOutputStream(saveOutputGeneral+ f.getName().replaceAll(".csv","") + ".xlsx");

								workbook.write(fileOut);

								fileOut.close();

								workbook.close();
							}

						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							if (br != null) {
								try {
									br.close();

								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
				mapCountFramework = new HashMap();
				importsGeneral = new HashMap();

			}
			progressBar.setProgressBarVisibility(false);
		}
	}

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	public static Object getKeyFromValue(Multimap hm, Object value) {
		for (Object o : hm.keySet()) {

			if (hm.get(o).contains(value)) {
				return o;
			}
		}
		return null;
	}

	private static void incrementValueAuthor(Map<String, Integer> map, String key) {
		Integer count = map.get(key);

		if (count == null) {
			map.put(key, 1);
		} else {
			map.put(key, count + 1);
		}

	}

	public void add(String key, String value) {
		ArrayList<String> values = strings.get(key);
		if (values == null) {
			values = new ArrayList<String>();
			strings.put(key, values);
		}

		values.add(value);
	}

	private static boolean isContain(String source, String subItem) {
		String pattern = "\\b" + subItem + "\\b";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(source);
		return m.find();
	}
}