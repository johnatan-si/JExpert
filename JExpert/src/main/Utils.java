package main;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;

public class Utils {

	public int calcData(String data) {
		
		
		int ano=Integer.parseInt(data.substring(0, 4));
		int mes=Integer.parseInt(data.substring(5, 7)); 
		int dia=Integer.parseInt(data.substring(8, 10));
		
		LocalDate dataCommit =  LocalDate.of(ano,mes,dia);
        
        // Cria um Objeto LocalDate com a data 26/09/2020.
        LocalDate dataInicial = LocalDate.of(2018,05,23);
        
       
        // Calcula a diferença de meses entre as duas datas
        long diferencaEmMes = ChronoUnit.MONTHS.between(dataCommit, dataInicial);
     
        
   
    
		return Integer.parseInt(""+diferencaEmMes);
	}

	
	/*public static void main(String[] args) throws ParseException {
		 // Cria um Objeto LocalDate com a data atual.
        //LocalDate hoje = LocalDate.now();
		
		LocalDate dataCommit =  LocalDate.of(2015,10,01);
        
        // Cria um Objeto LocalDate com a data 26/09/2020.
        LocalDate dataInicial = LocalDate.of(2018,05,23);
        
        System.out.println(dataInicial);
        
        // Calcula a diferença de dias entre as duas datas
        long diferencaEmDias = ChronoUnit.DAYS.between(dataCommit, dataInicial);
        // Calcula a diferença de meses entre as duas datas
        long diferencaEmMes = ChronoUnit.MONTHS.between(dataCommit, dataInicial);
        // Calcula a diferença de anos entre as duas datas
        long diferencaEmAnos = ChronoUnit.YEARS.between(dataCommit, dataInicial);
        
        // Exibe a diferença em dias entre as datas
        System.out.println("Diferença em dias entre " + dataCommit + " e " + dataInicial + " = " + diferencaEmDias);
        // Exibe a diferença em meses entre as datas
        System.out.println("Diferença em meses entre " + dataCommit + " e " + dataInicial + " = " + diferencaEmMes);
        // Exibe a diferença em anos entre as datas
        System.out.println("Diferença em anos entre " + dataCommit + " e " + dataInicial + " = " + diferencaEmAnos);

	}*/
	
	
	
	
}
