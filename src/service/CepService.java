package service;
import model.Cep;
import java.util.ArrayList;
import java.util.List;
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
            	return new Cep(cep, cidade, estado, logradouro, complemento, bairro, UF, regiao, DDD, 1, null);
            }

        } catch (Exception e) {
            return null;
        }
    }
    
    public List<Cep> buscarPorEndereco(String uf, String cidade, String logradouro) {
    	
    	List<Cep> lista = new ArrayList<>();
        try {
        	cidade = cidade.replace(" ", "%20");
            logradouro = logradouro.replace(" ", "%20");
            URL url = new URL("https://viacep.com.br/ws/" + uf + "/" + cidade + "/" + logradouro + "/json/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            Scanner sc = new Scanner(conn.getInputStream(), "UTF-8");
            String json = "";

            while (sc.hasNext()) json += sc.nextLine();
            sc.close();
            
            json = json.substring(1, json.length() - 1);

            String[] objetos = json.split("\\}\\s*,\\s*\\{");

            for (String obj : objetos) {
                
                String cepDaLista = extrair(obj, "\"cep\": \"").replace("-", "");
                String estado = extrair(obj, "\"estado\": \"");
                String logradouroRetornado = extrair(obj, "\"logradouro\": \""); // Nome alterado para não conflitar
                String complemento = extrair(obj, "\"complemento\": \"");
                String bairro = extrair(obj, "\"bairro\": \"");
                String regiao = extrair(obj, "\"regiao\": \"");
                String DDD = extrair(obj, "\"ddd\": \"");
                
                String erroStr = extrair(obj, "\"erro\": \"");
                boolean erro = erroStr != null && erroStr.equals("true");
                
                if(erro) {
                    return null;
                } else {
                	if(!logradouroRetornado.equals("N/A")) {
                    Cep c = new Cep(cepDaLista, cidade, estado, logradouroRetornado, complemento, bairro, uf, regiao, DDD, 1, null);
                    lista.add(c);
                	}else {
                		return null;
                	}
                }
            }
            
            return lista;
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