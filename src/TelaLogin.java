import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Scanner;

import model.Usuario;
import repository.Repository;
import service.CepService;

public class TelaLogin {
	 
	public static void main(String[] args) throws NoSuchAlgorithmException {
	Scanner sc = new Scanner(System.in);
	boolean o = false;
	CepService service = new CepService();
	Repository repo = new Repository();
	
	do {
	System.out.println("\n -informe o seu email.");
	 String email = sc.nextLine();
	 System.out.println("2 -informe o sua senha.");
	 String senha = sc.nextLine();
	 
	 Usuario u = repo.login(email, senha);
	 
	 if(u != null) {
		 System.out.println("\n1 usuario verificado ola" + u.getNome());
		 
		 o = true;
		 MainCep.main(args);
		 
	 }else {
		 System.out.println("\n1 falha na conexão tente de novo");
	 }

	 
}while(!o);

}
	
}