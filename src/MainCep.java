import java.util.List;
import java.util.Scanner;
import repository.Repository;
import service.CepService;
import model.Cep;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

public class MainCep {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        CepService service = new CepService();
        Repository<Cep> repo = new Repository<>();
        char op;
        String x;
        boolean existe = false;
        Cep ultimo = null;
        int max = 20, cont = 0, maxh = 15, opL;
        LocalDateTime horario;
        List<Cep> lista = null;

        do{
        	
            System.out.println("\n1 - Consultar CEP");
            System.out.println("2 - Listar histórico de pesquisa");
            System.out.println("3 - Pesquisar dentro do histórico");
            System.out.println("4 - Deletar consulta");
            System.out.println("0 - Sair");
            
            x = sc.nextLine();
            if (!x.equals("1") && !x.equals("2") && !x.equals("0") && !x.equals("3") && !x.equals("4"))
            {
            	System.out.println("Valor inválido, tente novamente");
            	op = 1;
            }
            else
            {
            	op = x.charAt(0);
            	switch (op)
                {
                case '1':
                	existe = false;
                	System.out.print("Digite o CEP: ");
                    String cep = sc.nextLine();
                    if (cont < max)
                    {
                    	if (cep.length()!=8 || !cep.chars().allMatch(Character::isDigit))
                        {
                        	System.out.println("Digite um Cep válido de 8 numeros");
                        	break;
                        }
                        lista = repo.listar();
                        for(Cep z : lista) {
                        	if(cep.equals(z.getCodigo())) {
                        		existe = true;
                        		z.setConsultas(z.getConsultas()+1);
                        		break;
                        	}
                        }
                        if(!existe) {
                        	Cep c = service.buscar(cep);
                            if (c != null) {
                            	if (cont >= maxh) {
                                    lista.remove(0);
                                }
                                repo.salvar(c);
                                System.out.println(
                        			    "Cidade: " + c.getCidade() +
                        			    ", Estado: " + c.getEstado() +
                        			    ", Logradouro: " + c.getLogradouro() +
                        			    ", Complemento: " + c.getComplemento() +
                        			    ", Bairro: " + c.getBairro() +
                        			    ", UF: " + c.getUF() +
                        			    ", Região: " + c.getRegiao() +
                        			    ", DDD: " + c.getDDD()
                        			);
                                ultimo = c;
                                cont++;
                            }
                            else
                            {
                            	System.out.println("Cep inexistente");
                            }
                        } else {
                        	if(ultimo.getCodigo().equals(cep))
                        	{
                        		System.out.println("Cep já consultado");
                        	}
                        	else
                        	{
                        		for(Cep z : lista) {
                                	if(cep.equals(z.getCodigo())) {
                                		System.out.println(
                                			    "Cidade: " + z.getCidade() +
                                			    ", Estado: " + z.getEstado() +
                                			    ", Logradouro: " + z.getLogradouro() +
                                			    ", Complemento: " + z.getComplemento() +
                                			    ", Bairro: " + z.getBairro() +
                                			    ", UF: " + z.getUF() +
                                			    ", Região: " + z.getRegiao() +
                                			    ", DDD: " + z.getDDD()
                                			);
                                		ultimo=z;
                                		cont++;
                                	}
                                }
                        	}	
                        }
                    }else
                    {
                    	System.out.println("Máximo de consultas atingido.");
                    }
                    
                    
                    break;
                case '2':
                	System.out.println("Escolha a ordenação desejada para a lista.");
                	System.out.println("\n1 - Consultas recentes");
                	System.out.println("\n2 - Ordem original");
                	System.out.println("\n3 - Mais consultados");
                	opL = sc.nextInt();
                	switch(opL) {
                	case 1:
                		for (Cep c1 : repo.listar().stream()
                				.sorted(Comparator.comparing(Cep::getHorario).reversed())
                	            .collect(Collectors.toList())) {
                    		System.out.println(
                    			    c1.getCodigo() + " - " +
                    			    "Cidade: " + c1.getCidade() +
                    			    ", Estado: " + c1.getEstado() +
                    			    ", Logradouro: " + c1.getLogradouro() +
                    			    ", Complemento: " + c1.getComplemento() +
                    			    ", Bairro: " + c1.getBairro() +
                    			    ", UF: " + c1.getUF() +
                    			    ", Região: " + c1.getRegiao() +
                    			    ", DDD: " + c1.getDDD()
                    			);
                        }
                		break;
                	case 2:
                		for (Cep c1 : repo.listar()) {
                    		System.out.println(
                    			    c1.getCodigo() + " - " +
                    			    "Cidade: " + c1.getCidade() +
                    			    ", Estado: " + c1.getEstado() +
                    			    ", Logradouro: " + c1.getLogradouro() +
                    			    ", Complemento: " + c1.getComplemento() +
                    			    ", Bairro: " + c1.getBairro() +
                    			    ", UF: " + c1.getUF() +
                    			    ", Região: " + c1.getRegiao() +
                    			    ", DDD: " + c1.getDDD()
                    			);
                        }
                		break;
                	case 3:
                		for (Cep c1 : repo.listar().stream()
                	            .sorted(Comparator.comparingInt(Cep::getConsultas).reversed())
                	            .collect(Collectors.toList())) {
                	        System.out.println(
                	        		c1.getCodigo() + " - " +
	                	            "Cidade: " + c1.getCidade() +
	                	            ", Estado: " + c1.getEstado() +
	                	            ", Logradouro: " + c1.getLogradouro() +
	                	            ", Complemento: " + c1.getComplemento() +
	                	            ", Bairro: " + c1.getBairro() +
	                	            ", UF: " + c1.getUF() +
	                	            ", Região: " + c1.getRegiao() +
	                	            ", DDD: " + c1.getDDD()
                	        	);
                	    }
                		break;
                	default:
                		System.out.print("Opção inválida, tente novamente.");
                	}
                	
                	System.out.println("Foram feitas " + cont + " consultas bem sucedidas.");
                	if(ultimo != null) {
                		System.out.println("A ultima consulta feita foi pelo CEP " + ultimo.getCodigo() + " de " + ultimo.getCidade());
                	}
                	sc.nextLine();
                	break;
                	
                case '3':
                    System.out.print("Digite o CEP para buscar: ");
                    String busca = sc.nextLine();

                    boolean encontrado = false;

                    for (Cep c1 : repo.listar()) {
                        if (c1.getCodigo().equals(busca)) {
                            System.out.println(
                                c1.getCodigo() + " - " +
                                "Cidade: " + c1.getCidade() +
                                ", Estado: " + c1.getEstado() +
                                ", Logradouro: " + c1.getLogradouro() +
                                ", Complemento: " + c1.getComplemento() +
                                ", Bairro: " + c1.getBairro() +
                                ", UF: " + c1.getUF() +
                                ", Região: " + c1.getRegiao() +
                                ", DDD: " + c1.getDDD()
                            );
                            encontrado = true;
                        }
                    }

                    if (!encontrado) {
                        System.out.println("CEP não encontrado no histórico.");
                    }

                    break;
                	
                case '4':
                    System.out.print("Digite o CEP para apagar: ");
                    String apagar = sc.nextLine();
                    
                    boolean removido = repo.listar()
                    		.removeIf(c -> c.getCodigo().equals(apagar));

                    if (removido) {
                    	cont--;
                    	if(ultimo.getCodigo().equals(apagar)) {
                    		ultimo = null;
                    	}
                    	System.out.println("CEP removido do histórico.");
                    } else {
                        System.out.println("CEP não encontrado no histórico.");
                    }

                    break;
                	
                
                case '0':
                	System.out.println("Obrigado por usar o ViaCep 2.1.2!");
                	break;
                }
            }
        }while (op!='0');
    }
}