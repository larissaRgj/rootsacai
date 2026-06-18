package rootsacai;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ════════════════════════════════════════════════════════════
 *  Classe Permissao – Roots Acai Premium
 *  Disciplina: Aplicacoes Backend
 *  Professor : Wanderson Pereira dos Santos
 *
 *  Tabela intermediaria entre Perfil e Modulo.
 *  Define EXATAMENTE o que cada Perfil pode fazer em cada Modulo
 *  por meio de tres flags booleanas:
 *    pode_visualizar | pode_editar | pode_excluir
 *
 *  Diagrama:
 *    id | perfil_id (FK) | modulo_id (FK)
 *    | pode_visualizar | pode_editar | pode_excluir
 *
 *  Metodos publicos (INTERFACE):
 *    salvar(perfilId, moduloId, viz, edit, excl)  → INSERT ou UPDATE
 *    buscarPorPerfil(perfilId)                    → lista de permissoes do perfil
 *    buscarPorPerfilEModulo(perfilId, moduloId)   → permissao especifica
 *    remover(perfilId, moduloId)                  → DELETE
 *    listarMatriz(perfilId)                       → exibe checkboxes no console
 * ════════════════════════════════════════════════════════════
 */
public class Permissao {

    // ── atributos ────────────────────────────────────────────
    private int     id;
    private int     perfilId;
    private int     moduloId;
    private String  nomeModulo;   // join auxiliar para exibicao
    private boolean podeVisualizar;
    private boolean podeEditar;
    private boolean podeExcluir;

    // ── construtor completo ──────────────────────────────────
    public Permissao(int id, int perfilId, int moduloId, String nomeModulo,
                     boolean podeVisualizar, boolean podeEditar, boolean podeExcluir) {
        this.id              = id;
        this.perfilId        = perfilId;
        this.moduloId        = moduloId;
        this.nomeModulo      = nomeModulo;
        this.podeVisualizar  = podeVisualizar;
        this.podeEditar      = podeEditar;
        this.podeExcluir     = podeExcluir;
    }

    // ════════════════════════════════════════════════════════
    //  OPERACOES DE INTERFACE
    // ════════════════════════════════════════════════════════

    /**
     * Tela "Configuracao de Perfil" → SALVAR CHECKBOXES
     *
     * Insere uma nova permissao ou atualiza se ja existir
     * (UPSERT via INSERT ... ON DUPLICATE KEY UPDATE).
     * Isso mapeia exatamente o comportamento de checkboxes:
     * o usuario marca/desmarca e clica em Salvar.
     */
    public static Permissao salvar(int perfilId, int moduloId,
                                   boolean podeVisualizar,
                                   boolean podeEditar,
                                   boolean podeExcluir) {

        // Regra de negocio: sem visualizacao, os outros nao fazem sentido
        if (!podeVisualizar) {
            podeEditar   = false;
            podeExcluir  = false;
        }

        String sql = """
            INSERT INTO permissoes
                (perfil_id, modulo_id, pode_visualizar, pode_editar, pode_excluir)
            VALUES (?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
                pode_visualizar = VALUES(pode_visualizar),
                pode_editar     = VALUES(pode_editar),
                pode_excluir    = VALUES(pode_excluir)
            """;

        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt    (1, perfilId);
            ps.setInt    (2, moduloId);
            ps.setBoolean(3, podeVisualizar);
            ps.setBoolean(4, podeEditar);
            ps.setBoolean(5, podeExcluir);
            ps.executeUpdate();

            // Obtem o ID gerado (INSERT) ou o existente (UPDATE)
            ResultSet rs = ps.getGeneratedKeys();
            int novoId = rs.next() ? rs.getInt(1) : -1;

            // Busca nome do modulo para o toString
            Modulo m = Modulo.buscarPorId(moduloId);
            String nomeM = m != null ? m.getNome() : "Modulo #" + moduloId;

            System.out.printf("✅ Permissao salva → Perfil %d | %s [Viz:%s Edit:%s Excl:%s]%n",
                perfilId, nomeM,
                podeVisualizar ? "✓" : "✗",
                podeEditar     ? "✓" : "✗",
                podeExcluir    ? "✓" : "✗");

            return new Permissao(novoId, perfilId, moduloId, nomeM,
                                 podeVisualizar, podeEditar, podeExcluir);

        } catch (SQLException e) {
            System.out.println("❌ Erro ao salvar permissao: " + e.getMessage());
        }
        return null;
    }

    /**
     * Tela "Configuracao de Perfil" → LISTAR (preencher checkboxes)
     * Retorna todas as permissoes de um perfil, com JOIN ao nome do modulo.
     */
    public static List<Permissao> buscarPorPerfil(int perfilId) {
        List<Permissao> lista = new ArrayList<>();
        String sql = """
            SELECT p.id, p.perfil_id, p.modulo_id,
                   m.nome AS nome_modulo,
                   p.pode_visualizar, p.pode_editar, p.pode_excluir
            FROM permissoes p
            JOIN modulos m ON m.id = p.modulo_id
            WHERE p.perfil_id = ?
            ORDER BY m.nome
            """;
        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, perfilId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Permissao(
                    rs.getInt("id"),
                    rs.getInt("perfil_id"),
                    rs.getInt("modulo_id"),
                    rs.getString("nome_modulo"),
                    rs.getBoolean("pode_visualizar"),
                    rs.getBoolean("pode_editar"),
                    rs.getBoolean("pode_excluir")
                ));
            }
        } catch (SQLException e) {
            System.out.println("❌ Erro ao buscar permissoes: " + e.getMessage());
        }
        return lista;
    }

    /** Busca permissao especifica de um par (perfilId, moduloId) */
    public static Permissao buscarPorPerfilEModulo(int perfilId, int moduloId) {
        String sql = """
            SELECT p.*, m.nome AS nome_modulo
            FROM permissoes p
            JOIN modulos m ON m.id = p.modulo_id
            WHERE p.perfil_id = ? AND p.modulo_id = ?
            """;
        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, perfilId);
            ps.setInt(2, moduloId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Permissao(
                    rs.getInt("id"), rs.getInt("perfil_id"), rs.getInt("modulo_id"),
                    rs.getString("nome_modulo"),
                    rs.getBoolean("pode_visualizar"),
                    rs.getBoolean("pode_editar"),
                    rs.getBoolean("pode_excluir")
                );
            }
        } catch (SQLException e) {
            System.out.println("❌ " + e.getMessage());
        }
        return null;
    }

    /** Remove permissao de um par (perfilId, moduloId) */
    public static boolean remover(int perfilId, int moduloId) {
        String sql = "DELETE FROM permissoes WHERE perfil_id = ? AND modulo_id = ?";
        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, perfilId);
            ps.setInt(2, moduloId);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("⚠ Permissao removida (perfil=" + perfilId + ", modulo=" + moduloId + ").");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("❌ Erro ao remover permissao: " + e.getMessage());
        }
        return false;
    }

    /**
     * Tela "Configuracao de Perfil" → exibe a MATRIZ de checkboxes no console.
     * Para cada modulo ativo, mostra quais flags estao marcadas.
     */
    public static void listarMatriz(int perfilId) {
        Perfil perfil = Perfil.buscarPorId(perfilId);
        if (perfil == null) { System.out.println("❌ Perfil nao encontrado."); return; }

        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.printf ("║  Configuracao de Perfil: %-30s║%n", perfil.getNome());
        System.out.println("╠════════════════════════════════════════════════════════╣");
        System.out.printf ("║  %-22s  %-10s %-8s %-8s║%n",
                           "Modulo", "Visualizar", "Editar", "Excluir");
        System.out.println("╠════════════════════════════════════════════════════════╣");

        List<Modulo>    modulos     = Modulo.listarTodos();
        List<Permissao> permissoes  = buscarPorPerfil(perfilId);

        for (Modulo m : modulos) {
            if (!m.isAtivo()) continue;
            // Busca permissao correspondente (pode nao existir)
            Permissao perm = permissoes.stream()
                .filter(p -> p.getModuloId() == m.getId())
                .findFirst().orElse(null);

            boolean viz  = perm != null && perm.isPodeVisualizar();
            boolean edit = perm != null && perm.isPodeEditar();
            boolean excl = perm != null && perm.isPodeExcluir();

            System.out.printf("║  %-22s  %-10s %-8s %-8s║%n",
                m.getNome(),
                viz  ? "[✓]" : "[ ]",
                edit ? "[✓]" : "[ ]",
                excl ? "[✓]" : "[ ]");
        }
        System.out.println("╚════════════════════════════════════════════════════════╝");
    }

    // ── getters ──────────────────────────────────────────────
    public int     getId()             { return id;             }
    public int     getPerfilId()       { return perfilId;       }
    public int     getModuloId()       { return moduloId;       }
    public String  getNomeModulo()     { return nomeModulo;     }
    public boolean isPodeVisualizar()  { return podeVisualizar; }
    public boolean isPodeEditar()      { return podeEditar;     }
    public boolean isPodeExcluir()     { return podeExcluir;    }

    @Override
    public String toString() {
        return String.format("Perfil %d | %-20s | Viz:%s Edit:%s Excl:%s",
            perfilId,
            nomeModulo != null ? nomeModulo : "Modulo#" + moduloId,
            podeVisualizar ? "✓" : "✗",
            podeEditar     ? "✓" : "✗",
            podeExcluir    ? "✓" : "✗");
    }
}
