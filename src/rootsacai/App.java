package rootsacai;

import java.util.List;
import java.util.Scanner;

/**
 * ════════════════════════════════════════════════════════════
 *  ROOTS ACAI PREMIUM – App.java (ENTREGA 01)
 *  Disciplina: Aplicacoes Backend
 *  Professor : Wanderson Pereira dos Santos | Turma 3AM
 *
 *  Grupo:
 *    Larissa Rodrigues Guimaraes Jales  – 202420927
 *    Gabriel Ryan Torres da Silva       – 202510446
 *    Maria Yolanda Resende de Araujo    – 202511926
 *
 *  Novidades desta entrega:
 *    ✅ Tela Configuracao de Usuario  (incluir/alterar/listar/desativar)
 *    ✅ Tela Configuracao de Modulos  (incluir/alterar/listar/desativar)
 *    ✅ Tela Configuracao de Perfil   (checkboxes por modulo)
 * ════════════════════════════════════════════════════════════
 */
public class App {

    public static void main(String[] args) {

        Scanner     sc   = new Scanner(System.in);
        LojaService loja = new LojaService();

        System.out.println("╔═══════════════════════════════════════════╗");
        System.out.println("║   🍇  ROOTS ACAI PREMIUM  🍇             ║");
        System.out.println("║   Sistema de Gestao – Entrega 01         ║");
        System.out.println("╚═══════════════════════════════════════════╝");

        ConexaoBD.getConexao();

        int op;
        do {
            System.out.println("\n=== SISTEMA ===");
            System.out.println("1 - Cadastrar usuario");
            System.out.println("2 - Login");
            System.out.println("0 - Sair");
            System.out.print("Opcao: ");
            op = lerInt(sc);

            switch (op) {
                case 1 -> {
                    System.out.print("Nome  : "); String nome  = sc.nextLine().trim();
                    System.out.print("Login : "); String login = sc.nextLine().trim();
                    System.out.print("Senha : "); String senha = sc.nextLine().trim();
                    Usuario.cadastrarUsuario(nome, login, senha, 2);
                }
                case 2 -> {
                    System.out.print("Login : "); String login = sc.nextLine().trim();
                    System.out.print("Senha : "); String senha = sc.nextLine().trim();
                    Usuario logado = Usuario.realizarLogin(login, senha);
                    if (logado != null) menuUsuario(sc, loja, logado);
                }
                case 0 -> {
                    ConexaoBD.fechar();
                    System.out.println("Ate logo! 🍇 Roots Acai Premium");
                }
                default -> System.out.println("Opcao invalida.");
            }
        } while (op != 0);
        sc.close();
    }

    // ════════════════════════════════════════════════════════
    //  MENU POS-LOGIN
    // ════════════════════════════════════════════════════════
    static void menuUsuario(Scanner sc, LojaService loja, Usuario user) {
        boolean isAdmin = user.getPerfil() != null &&
                          user.getPerfil().getNome().equalsIgnoreCase("Administrador");
        int op;
        do {
            System.out.println("\n=== LOJA DE ACAI – Roots Premium ===");
            System.out.println("Ola, " + user.getNome()
                + " | Perfil: " + (user.getPerfil() != null ? user.getPerfil().getNome() : "—")
                + " | Ultimo acesso: " + user.getUltimoAcesso());
            System.out.println("-------------------------------------");
            System.out.println("1 - Montar Acai");
            System.out.println("2 - Alterar minha senha");
            if (isAdmin) {
                System.out.println("─────── ADMINISTRACAO ───────────────");
                System.out.println("3 - Configuracao de Usuarios");
                System.out.println("4 - Configuracao de Modulos");
                System.out.println("5 - Configuracao de Perfis");
            }
            System.out.println("0 - Logout");
            System.out.print("Opcao: ");
            op = lerInt(sc);

            switch (op) {
                case 1 -> loja.fazerPedido(sc);
                case 2 -> {
                    System.out.print("Senha atual : "); String antiga = sc.nextLine().trim();
                    System.out.print("Nova senha  : "); String nova   = sc.nextLine().trim();
                    user.alterarSenha(antiga, nova);
                }
                case 3 -> { if (isAdmin) menuConfiguracaoUsuarios(sc); else deny(); }
                case 4 -> { if (isAdmin) menuConfiguracaoModulos(sc);  else deny(); }
                case 5 -> { if (isAdmin) menuConfiguracaoPerfis(sc);   else deny(); }
                case 0 -> System.out.println("Logout realizado.");
                default -> System.out.println("Opcao invalida.");
            }
        } while (op != 0);
    }

    // ════════════════════════════════════════════════════════
    //  TELA: CONFIGURACAO DE USUARIOS  ← NOVA
    //  Requisito: incluir | alterar | listar | desativar
    // ════════════════════════════════════════════════════════
    static void menuConfiguracaoUsuarios(Scanner sc) {
        int op;
        do {
            System.out.println("\n╔════════════════════════════════════╗");
            System.out.println("║   CONFIGURACAO DE USUARIOS         ║");
            System.out.println("╠════════════════════════════════════╣");
            System.out.println("║ 1 - Incluir usuario                ║");
            System.out.println("║ 2 - Alterar usuario                ║");
            System.out.println("║ 3 - Listar todos os usuarios       ║");
            System.out.println("║ 4 - Desativar usuario              ║");
            System.out.println("║ 0 - Voltar                         ║");
            System.out.println("╚════════════════════════════════════╝");
            System.out.print("Opcao: ");
            op = lerInt(sc);

            switch (op) {

                // ── INCLUIR ──────────────────────────────────────
                case 1 -> {
                    System.out.println("\n── Incluir Usuario ──");
                    System.out.print("Nome  : "); String nome  = sc.nextLine().trim();
                    System.out.print("Login : "); String login = sc.nextLine().trim();
                    System.out.print("Senha : "); String senha = sc.nextLine().trim();

                    // Escolha do perfil
                    System.out.println("Perfis disponiveis:");
                    Perfil.listarTodos().forEach(p -> System.out.println("  " + p));
                    System.out.print("ID do Perfil: ");
                    int perfilId = lerInt(sc);

                    Usuario.cadastrarUsuario(nome, login, senha, perfilId);
                }

                // ── ALTERAR ──────────────────────────────────────
                case 2 -> {
                    System.out.println("\n── Alterar Usuario ──");
                    Usuario.listarTodos();
                    System.out.print("ID do usuario a alterar: ");
                    int id = lerInt(sc);

                    System.out.print("Novo nome  (Enter = manter): ");
                    String novoNome  = sc.nextLine().trim();
                    System.out.print("Nova senha (Enter = manter): ");
                    String novaSenha = sc.nextLine().trim();

                    Perfil.listarTodos().forEach(p -> System.out.println("  " + p));
                    System.out.print("Novo ID perfil (0 = manter): ");
                    int novoPerfilId = lerInt(sc);

                    Usuario.alterarUsuario(id, novoNome, novaSenha, novoPerfilId);
                }

                // ── LISTAR ───────────────────────────────────────
                case 3 -> Usuario.listarTodos();

                // ── DESATIVAR ────────────────────────────────────
                case 4 -> {
                    System.out.println("\n── Desativar Usuario ──");
                    Usuario.listarTodos();
                    System.out.print("ID do usuario a desativar: ");
                    int id = lerInt(sc);
                    Usuario.desativar(id);
                }

                case 0 -> System.out.println("← Voltando ao menu principal.");
                default -> System.out.println("Opcao invalida.");
            }
        } while (op != 0);
    }

    // ════════════════════════════════════════════════════════
    //  TELA: CONFIGURACAO DE MODULOS  ← NOVA
    //  Requisito: incluir | alterar | listar | desativar
    // ════════════════════════════════════════════════════════
    static void menuConfiguracaoModulos(Scanner sc) {
        int op;
        do {
            System.out.println("\n╔════════════════════════════════════╗");
            System.out.println("║   CONFIGURACAO DE MODULOS          ║");
            System.out.println("╠════════════════════════════════════╣");
            System.out.println("║ 1 - Incluir modulo                 ║");
            System.out.println("║ 2 - Alterar modulo                 ║");
            System.out.println("║ 3 - Listar todos os modulos        ║");
            System.out.println("║ 4 - Desativar modulo               ║");
            System.out.println("║ 0 - Voltar                         ║");
            System.out.println("╚════════════════════════════════════╝");
            System.out.print("Opcao: ");
            op = lerInt(sc);

            switch (op) {

                // ── INCLUIR ──────────────────────────────────────
                case 1 -> {
                    System.out.println("\n── Incluir Modulo ──");
                    System.out.print("Nome      : "); String nome = sc.nextLine().trim();
                    System.out.print("Descricao : "); String desc = sc.nextLine().trim();
                    Modulo.cadastrarModulo(nome, desc);
                }

                // ── ALTERAR ──────────────────────────────────────
                case 2 -> {
                    System.out.println("\n── Alterar Modulo ──");
                    Modulo.listarTodos();
                    System.out.print("ID do modulo a alterar: ");
                    int id = lerInt(sc);
                    Modulo m = Modulo.buscarPorId(id);
                    if (m == null) { System.out.println("❌ Modulo nao encontrado."); break; }
                    System.out.print("Novo nome  (Enter = manter): "); String novoNome = sc.nextLine().trim();
                    System.out.print("Nova desc  (Enter = manter): "); String novaDesc = sc.nextLine().trim();
                    m.alterarModulo(
                        novoNome.isBlank() ? m.getNome()      : novoNome,
                        novaDesc.isBlank() ? m.getDescricao() : novaDesc
                    );
                }

                // ── LISTAR ───────────────────────────────────────
                case 3 -> Modulo.listarTodos();

                // ── DESATIVAR ────────────────────────────────────
                case 4 -> {
                    System.out.println("\n── Desativar Modulo ──");
                    Modulo.listarTodos();
                    System.out.print("ID do modulo a desativar: ");
                    int id = lerInt(sc);
                    Modulo m = Modulo.buscarPorId(id);
                    if (m == null) { System.out.println("❌ Modulo nao encontrado."); break; }
                    m.desativar();
                }

                case 0 -> System.out.println("← Voltando ao menu principal.");
                default -> System.out.println("Opcao invalida.");
            }
        } while (op != 0);
    }

    // ════════════════════════════════════════════════════════
    //  TELA: CONFIGURACAO DE PERFIS  ← NOVA
    //  Requisito: selecionar perfil → checkboxes por modulo
    //             (pode_visualizar / pode_editar / pode_excluir)
    // ════════════════════════════════════════════════════════
    static void menuConfiguracaoPerfis(Scanner sc) {
        int op;
        do {
            System.out.println("\n╔════════════════════════════════════╗");
            System.out.println("║   CONFIGURACAO DE PERFIS           ║");
            System.out.println("╠════════════════════════════════════╣");
            System.out.println("║ 1 - Listar perfis                  ║");
            System.out.println("║ 2 - Incluir perfil                 ║");
            System.out.println("║ 3 - Alterar perfil                 ║");
            System.out.println("║ 4 - Configurar permissoes          ║");
            System.out.println("║ 0 - Voltar                         ║");
            System.out.println("╚════════════════════════════════════╝");
            System.out.print("Opcao: ");
            op = lerInt(sc);

            switch (op) {

                // ── LISTAR ───────────────────────────────────────
                case 1 -> Perfil.listarTodos().forEach(p -> System.out.println("  " + p));

                // ── INCLUIR ──────────────────────────────────────
                case 2 -> {
                    System.out.print("Nome do novo perfil: ");
                    Perfil.cadastrarPerfil(sc.nextLine().trim());
                }

                // ── ALTERAR ──────────────────────────────────────
                case 3 -> {
                    Perfil.listarTodos().forEach(p -> System.out.println("  " + p));
                    System.out.print("ID do perfil a alterar: "); int id = lerInt(sc);
                    Perfil p = Perfil.buscarPorId(id);
                    if (p == null) { System.out.println("❌ Perfil nao encontrado."); break; }
                    System.out.print("Novo nome    : "); String nome = sc.nextLine().trim();
                    System.out.print("Ativo? (s/n) : "); boolean ativo = sc.nextLine().trim().equalsIgnoreCase("s");
                    p.alterarPerfil(nome.isBlank() ? p.getNome() : nome, ativo);
                }

                // ── CHECKBOXES DE PERMISSAO ───────────────────────
                case 4 -> menuPermissoes(sc);

                case 0 -> System.out.println("← Voltando ao menu principal.");
                default -> System.out.println("Opcao invalida.");
            }
        } while (op != 0);
    }

    // ════════════════════════════════════════════════════════
    //  SUB-MENU: PERMISSOES (CHECKBOXES)
    //  Para cada modulo ativo, o admin marca as 3 flags.
    // ════════════════════════════════════════════════════════
    static void menuPermissoes(Scanner sc) {
        // 1. Seleciona o perfil
        System.out.println("\n── Selecione o Perfil ──");
        List<Perfil> perfis = Perfil.listarTodos();
        if (perfis.isEmpty()) { System.out.println("Nenhum perfil cadastrado."); return; }
        perfis.forEach(p -> System.out.println("  " + p));
        System.out.print("ID do perfil: ");
        int perfilId = lerInt(sc);
        Perfil perfil = Perfil.buscarPorId(perfilId);
        if (perfil == null) { System.out.println("❌ Perfil nao encontrado."); return; }

        // 2. Exibe a matriz atual (como se fossem checkboxes na tela)
        Permissao.listarMatriz(perfilId);

        // 3. Para cada modulo ativo, pergunta os checkboxes
        List<Modulo> modulos = Modulo.listarTodos();
        for (Modulo m : modulos) {
            if (!m.isAtivo()) continue;

            System.out.println("\n── Modulo: " + m.getNome() + " ──");
            System.out.print("  [Visualizar] (s/n): "); boolean viz  = sc.nextLine().trim().equalsIgnoreCase("s");
            System.out.print("  [Editar]     (s/n): "); boolean edit = sc.nextLine().trim().equalsIgnoreCase("s");
            System.out.print("  [Excluir]    (s/n): "); boolean excl = sc.nextLine().trim().equalsIgnoreCase("s");

            Permissao.salvar(perfilId, m.getId(), viz, edit, excl);
        }

        // 4. Exibe a matriz atualizada
        System.out.println("\n── Permissoes atualizadas ──");
        Permissao.listarMatriz(perfilId);
    }

    // ── Utilitarios ──────────────────────────────────────────
    static void deny() { System.out.println("❌ Sem permissao de administrador."); }

    public static int lerInt(Scanner sc) {
        try { return Integer.parseInt(sc.nextLine().trim()); }
        catch (NumberFormatException e) { return -1; }
    }
}
