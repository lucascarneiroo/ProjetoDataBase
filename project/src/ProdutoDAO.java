import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import java.util.Scanner;

public class ProdutoDAO {

    private ObjectContainer db;
    private Scanner scanner;

    public ProdutoDAO(ObjectContainer db) {
        this.db = db;
        this.scanner = new Scanner(System.in);
    }


    public void adicionarProduto() {
        System.out.println("Digite o nome do produto:");
        String nome = scanner.nextLine();
        System.out.println("Digite o preço do produto:");
        double preco = scanner.nextDouble();
        System.out.println("Digite a quantidade em estoque do produto:");
        int quantidade = scanner.nextInt();
        scanner.nextLine();
        Produto produto = new Produto(nome, preco, quantidade);
        db.store(produto);
        System.out.println("Produto adicionado com sucesso!");
    }


    public void selecionarProdutos() {
        ObjectSet<Produto> result = db.queryByExample(Produto.class);
        if (result.isEmpty()) {
            System.out.println("Não há produtos cadastrados.");
        } else {
            System.out.println("Produtos:");
            while (result.hasNext()) {
                Produto produto = result.next();
                System.out.println("Nome: " + produto.getNome());
                System.out.println("Preço: " + produto.getPreco());
                System.out.println("Quantidade em estoque: " + produto.getQuantidadeEstoque());
                System.out.println();
            }
        }
    }


    public void atualizarProduto() {
        System.out.println("Digite o nome do produto que deseja atualizar:");
        String nome = scanner.nextLine();
        ObjectSet<Produto> result = db.queryByExample(new Produto(nome, 0.0, 0));
        if (result.isEmpty()) {
            System.out.println("Produto não encontrado!");
        } else {
            Produto produto = result.next();
            exibirOpcoesAtualizacao();
            int opcao = scanner.nextInt();
            scanner.nextLine();
            switch (opcao) {
                case 1:
                    System.out.println("Digite o novo nome do produto:");
                    String novoNome = scanner.nextLine();
                    produto.setNome(novoNome);
                    break;
                case 2:
                    System.out.println("Digite o novo preço do produto:");
                    double novoPreco = scanner.nextDouble();
                    produto.setPreco(novoPreco);
                    break;
                case 3:
                    System.out.println("Digite a nova quantidade em estoque do produto:");
                    int novaQuantidade = scanner.nextInt();
                    produto.setQuantidadeEstoque(novaQuantidade);
                    break;
                default:
                    System.out.println("Opção inválida.");
                    return;
            }
            db.store(produto);
            System.out.println("Produto atualizado com sucesso!");
        }
    }


    private void exibirOpcoesAtualizacao() {
        System.out.println("Escolha uma opção de atualização:");
        System.out.println("1. Atualizar Nome");
        System.out.println("2. Atualizar Preço");
        System.out.println("3. Atualizar Quantidade em Estoque");
    }


    public void deletarProduto() {
        System.out.println("Digite o nome do produto que deseja deletar:");
        String nome = scanner.nextLine();
        ObjectSet<Produto> result = db.queryByExample(new Produto(nome, 0.0, 0));
        if (result.isEmpty()) {
            System.out.println("Produto não encontrado!");
        } else {
            Produto produto = result.next();
            db.delete(produto);
            System.out.println("Produto excluído com sucesso!");
        }
    }


    public void exibirMenu() {
        System.out.println("Escolha uma opção:");
        System.out.println("1. Adicionar Produto");
        System.out.println("2. Selecionar Produtos");
        System.out.println("3. Atualizar Produto");
        System.out.println("4. Deletar Produto");
        System.out.println("5. Sair");
    }


    public void executarOperacao(int opcao) {
        switch (opcao) {
            case 1:
                adicionarProduto();
                break;
            case 2:
                selecionarProdutos();
                break;
            case 3:
                atualizarProduto();
                break;
            case 4:
                deletarProduto();
                break;
            case 5:
                System.out.println("Encerrando o programa...");
                break;
            default:
                System.out.println("Opção inválida.");
        }
    }

    public static void main(String[] args) {
        ObjectContainer db = Db4oEmbedded.openFile("produto.db4o");
        ProdutoDAO produtoDAO = new ProdutoDAO(db);
        Scanner scanner = new Scanner(System.in);
        int opcao;
        do {
            produtoDAO.exibirMenu();
            opcao = scanner.nextInt();
            scanner.nextLine();
            produtoDAO.executarOperacao(opcao);
        } while (opcao != 5);
        db.close();
        scanner.close();
    }
}
