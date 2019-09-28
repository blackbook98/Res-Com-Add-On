package abc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.Package;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



public class ResCom
{
	
	public static void main(String[] args) {		
			
		try {
				//ConvertExcel();
			String path="*Your path*.pdf";
			String conpath="*Your path*.txt";
			    ConvertPDF(path,conpath);
			    BasicConfigurator.configure();
			    
			    
			} catch (IOException e) {
			    e.printStackTrace();
			}
	 	
}

	public static String[] splitStr(String line)
	{
String temp[] = new String[40];
			for(int j=0;j<temp.length;j++)
				temp[j] = "";
			
			int count = 0, a = 0;
		
			char[] characters = (line.trim()).toCharArray();
			            
			for(int k=0; k<characters.length; k++)
			{
				if(!Character.isWhitespace(characters[k]))
					temp[a] = temp[a] + characters[k];	
				else
				{
					count++;
				
					if(count == 4)
					{
						count = 1;
						a++;
						temp[a] = " ";
					}
					else if((k+1) >= characters.length && count >= 3)
					{
						a++;
						temp[a] = " ";
					}
					else if(!Character.isWhitespace(characters[k+1]))
					{
						count = 0;
						a++;
					}
				}
			}
			
			int flag=1;

			for(int l=1;l<4;l++)
			{
				char ch = line.charAt(line.length()-l);
				if(!Character.isWhitespace(ch))
					flag=0;	
			}

			if(flag==1)
				temp[++a]=" ";
			
			String inter[]=new String[a+1];
			for(int k=0;k<=a;k++)
				inter[k]=temp[k];
			return inter;
	}
	
	public static void ConvertPDF(String path, String conPath) throws IOException
	{
		PDDocument doc = PDDocument.load(new File(path)); // put path to your input pdf file here
		String text =  new PDFTextStripper().getText(doc);
		System.out.println(text);
		   
		File file=new File(conPath);
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));

		int starti=0,aftercol=0;
		String lines[] = text.split("\\r?\\n");

		   
		for(int i=0;i<lines.length;i++)
		{
		   	if(lines[i].toUpperCase().contains("USN"))
		   	{
		   		String words[]=lines[i].split("\\s+");
		   		int count = 0;
		   	
			   	for(int j=0;j<words.length;j++)
			   	{
	
				   	if(words[j].matches("[0-9]+") && words[j].length() >= 1) // headings which have only numbers will not be printed
				   		count++;
				   	
				   	else if(words[j].toUpperCase().contains("NAME"))
				   	{
				   		starti=j;
				   		pw.write(StringUtils.center(words[j],30)); 
				   		//System.out.print(StringUtils.center(words[j],30));
				   	}
				   	
				   	else
				   	{
				   		pw.write(StringUtils.center(words[j],15));
				   		//System.out.print(StringUtils.center(words[j],15));// printing headings other than usn,name...
				   	}
			   	
			   	}
			   	pw.println();
			   	aftercol = words.length - starti - count - 1;
			   	//System.out.println();
		   	
		   	}
		   	else if(lines[i].contains("1DS"))
		   	{ 
		   		int endi=0;
		   		String str="";
		   	
		        String temp[] = splitStr(lines[i]);
		        endi = temp.length - aftercol - 1;
		   	   
			   	for(int l=0;l<starti;l++)//data before name
			   	{
			   	   	pw.print(StringUtils.center(temp[l],15));
			   	   	//System.out.print(StringUtils.center(temp[l],15));
			   	}
			       
			   	for(int k=starti;k<=endi;k++)//name
			   		str = str+" "+temp[k];
			                
			   	pw.print(StringUtils.center(str, 30));
			   	//System.out.print(StringUtils.center(str, 30));
			   	
			   	for (int m=endi+1;m<temp.length;m++)//after columns
			   	{
			   		pw.write(StringUtils.center(temp[m], 15));
			   		//System.out.print(StringUtils.center(temp[m],15));
			   	}
			   	pw.println();
			   	//System.out.println();
		   	}
		   	else
		   	{ 
		   		pw.println(lines[i]);
		   		//System.out.println(lines[i]);
		   	}
		}
		pw.close();
	}
	}
	
	public static void ConvertExcel() throws IOException
	{File file=new File(conPath);
		
		PrintWriter pw = new PrintWriter(file);
		
		if(path.contains(".xlsx"))
		{
		
			FileInputStream fis = new FileInputStream(path);
	
		     // Finds the workbook instance for XLSX file
		     XSSFWorkbook myWorkBook = new XSSFWorkbook(path);
		    
		     // Return first sheet from the XLSX workbook
		     XSSFSheet mySheet = myWorkBook.getSheetAt(0);
		    
		     // Get iterator to all the rows in current sheet
		     Iterator<Row> rowIterator = mySheet.iterator();
		    
		     // Traversing over each row of XLSX file
		     while (rowIterator.hasNext()) {
		         Row row = rowIterator.next();
		
		         // For each row, iterate through each columns
		         Iterator<Cell> cellIterator = row.cellIterator();
		         while (cellIterator.hasNext()) {
		
		             Cell cell = cellIterator.next();
		
		             switch (cell.getCellType()) {
		             case Cell.CELL_TYPE_STRING:
		            	 if(cell.getStringCellValue().contentEquals("USN"))
		            	 {
		                 //System.out.print(cell.getStringCellValue() + "\t\t");
		                 pw.write(cell.getStringCellValue() + "\t\t");
		            	 }
		            	 else
		            	 {
		            	 //System.out.print(cell.getStringCellValue() + "\t");
		                 pw.write(cell.getStringCellValue() + "\t");
		            	 }
		                 break;
		             case Cell.CELL_TYPE_NUMERIC:
		                 //System.out.print(Math.round(cell.getNumericCellValue()) + "\t");
		                 pw.write(Math.round(cell.getNumericCellValue()) + "\t");
		                 break;
		             case Cell.CELL_TYPE_BOOLEAN:
		                 //System.out.print(cell.getBooleanCellValue() + "\t");
		                 pw.write(cell.getBooleanCellValue() + "\t");
		                 break;
		             default :
		          
		             }
		        
		         }  //System.out.print("\n");
		         pw.println();
		     }pw.close(); 
	    }
		else
		{
			FileInputStream fis2 = new FileInputStream(path);
	
			  //Get the workbook instance for XLS file 
			  HSSFWorkbook workbook = new HSSFWorkbook(fis2);
	
			  //Get first sheet from the workbook
			  HSSFSheet sheet = workbook.getSheetAt(0);
	
			  //Iterate through each rows from first sheet
			   Iterator<Row> rowIterator = sheet.iterator();
			   while(rowIterator.hasNext()) {
			     Row row = rowIterator.next();
	
			     //For each row, iterate through each columns
			    Iterator<Cell> cellIterator = row.cellIterator();
			    while(cellIterator.hasNext()) {
	
			        Cell cell = cellIterator.next();
	
			        switch(cell.getCellType()) {
			            case Cell.CELL_TYPE_BOOLEAN:
			                //System.out.print(cell.getBooleanCellValue() + "\t");
			                pw.write(cell.getBooleanCellValue() + "\t");
			                break;
			            case Cell.CELL_TYPE_NUMERIC:
			                //System.out.print(Math.round(cell.getNumericCellValue()) + "\t");
			                pw.write(Math.round(cell.getNumericCellValue()) + "\t");
			                break;
			            case Cell.CELL_TYPE_STRING:
			            	if(cell.getStringCellValue().contentEquals("USN"))
			            	{
			                //System.out.print(cell.getStringCellValue() + "\t\t");
			                pw.write(cell.getStringCellValue() + "\t\t");
			            	}
			            	else
			            	{
			            		//System.out.print(cell.getStringCellValue() + "\t");
			            		pw.write(cell.getStringCellValue() + "\t");
			            	}
			                break;
			        }
			    }
			    //System.out.println("");
			    pw.println();
			}
			pw.close();
	  }
 }
	
