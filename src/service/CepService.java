package service;
import model.Cep;
import java.net.*;
import java.util.Scanner;

public class CepService {
	
    public Cep buscar(String cep) {

        try {
            URL url = new URL("https://viacep.com.br/ws/" + cep + "/json/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            Scanner sc = new Scanner(conn.getInputStream(), "UTF-8");
            String json = "";

            while (sc.hasNext()) json += sc.nextLine();
            sc.close();

            String cidade = extrair(json, "\"localidade\": \"");
            String estado = extrair(json, "\"estado\": \"");
            String logradouro = extrair(json, "\"logradouro\": \"");
            String complemento = extrair(json, "\"complemento\": \"");
            String bairro = extrair(json, "\"bairro\": \"");
            String UF = extrair(json, "\"uf\": \"");
            String regiao = extrair(json, "\"regiao\": \"");
            String DDD = extrair(json, "\"ddd\": \"");
            
            boolean erro = Boolean.parseBoolean(extrair(json, "\"erro\": \""));
            
            
            if(erro == true) {
            	return null;
            }else {
            	return new Cep(cep, cidade, estado, logradouro, complemento, bairro, UF, regiao, DDD);
            }

        } catch (Exception e) {
            return null;
        }
    }

    private String extrair(String json, String chave) {
        try {
            int i = json.indexOf(chave) + chave.length();
            return json.substring(i, json.indexOf("\"", i));
        } catch (Exception e) {
            return "N/A";
        }
    }
}