═══════════════════════════════════════════════════════════════
  ROOTS AÇAÍ PREMIUM — ENTREGA 01
  Disciplina : Aplicações Backend
  Professor  : Wanderson Pereira dos Santos | Turma 3AM

  Grupo:
    Larissa Rodrigues Guimaraes Jales  – 202420927
    Gabriel Ryan Torres da Silva       – 202510446
    Maria Yolanda Resende de Araujo    – 202511926
═══════════════════════════════════════════════════════════════

ESTRUTURA DA ENTREGA
────────────────────
entrega01/
│
├── banco/
│   └── entrega01_migration.sql   ← Execute DEPOIS do roots_acai.sql
│
├── src/rootsacai/
│   ├── Modulo.java               ← NOVO  (Entrega 01)
│   ├── Permissao.java            ← NOVO  (Entrega 01)
│   ├── App.java                  ← ATUALIZADO com 3 novos menus
│   └── Usuario.java              ← ATUALIZADO (+desativar, +alterarUsuario)
│
└── web/
    └── admin_panel.html          ← Interface visual completa (rodar no browser)


O QUE FOI IMPLEMENTADO (conforme PDF)
───────────────────────────────────────

1. CLASSE Modulo  (src/rootsacai/Modulo.java)
   Atributos  : id | nome | descricao | ativo
   Métodos    :
     + cadastrarModulo(nome, descricao)   → INSERT
     + alterarModulo(novoNome, novaDesc)  → UPDATE
     + desativar()                        → UPDATE ativo=false (soft-delete)
     + listarTodos()                      → SELECT *
     + buscarPorId(id)                    → SELECT WHERE id

2. CLASSE Permissao  (src/rootsacai/Permissao.java)
   Atributos  : id | perfil_id (FK) | modulo_id (FK)
                | pode_visualizar | pode_editar | pode_excluir
   Métodos    :
     + salvar(perfilId, moduloId, viz, edit, excl) → UPSERT (INSERT ou UPDATE)
     + buscarPorPerfil(perfilId)                   → lista permissões do perfil
     + buscarPorPerfilEModulo(pId, mId)            → permissão específica
     + remover(perfilId, moduloId)                 → DELETE
     + listarMatriz(perfilId)                      → exibe checkboxes no console

3. CLASSE Usuario  (atualizada)
   Novos métodos:
     + alterarUsuario(id, novoNome, novaSenha, novoPerfilId)
     + desativar(id)   → soft-delete (ativo = FALSE)
   Campo novo : ativo BOOLEAN

4. App.java (atualizado)
   ✅ Tela "Configuração de Usuário"
      → incluir / alterar / listar / desativar

   ✅ Tela "Configuração de Módulos"
      → incluir / alterar / listar / desativar

   ✅ Tela "Configuração de Perfil"
      → seleciona perfil → checkboxes por módulo
      → pode_visualizar / pode_editar / pode_excluir

5. admin_panel.html  (interface web)
   → Painel administrativo completo com as 3 telas
   → Funciona sem servidor (abre direto no browser)
   → localStorage simula o banco de dados


COMO RODAR
──────────

A) CONSOLE (Java + MySQL)
   1. Execute banco/roots_acai.sql       (base existente)
   2. Execute banco/entrega01_migration.sql  (novas tabelas)
   3. Ajuste credenciais em ConexaoBD.java
   4. Adicione mysql-connector.jar ao projeto
   5. Run App.java (F6 no NetBeans)
   6. Login: admin / admin123
   7. Menu: Configuração de Usuarios | Modulos | Perfis

B) INTERFACE WEB (sem servidor)
   1. Abra web/admin_panel.html no navegador
   2. Clique nas abas do menu lateral:
      → Usuários     : incluir/alterar/listar/desativar
      → Módulos      : incluir/alterar/listar/desativar
      → Perfis       : incluir/alterar/listar/desativar
      → Permissões   : seleciona perfil + checkboxes por módulo
   3. Dados salvos em localStorage (persistem entre sessões)


BANCO DE DADOS — NOVAS TABELAS
──────────────────────────────

  modulos (id, nome, descricao, ativo, criado_em)
    ↓  1:N
  permissoes (id, perfil_id FK, modulo_id FK,
              pode_visualizar, pode_editar, pode_excluir)
    ↑  1:N
  perfis (id, nome, ativo)   ← já existia

  CONSTRAINT UNIQUE (perfil_id, modulo_id) garante que
  cada par (Perfil × Módulo) aparece exatamente uma vez.


REGRA DE NEGÓCIO IMPLEMENTADA
──────────────────────────────

  • "Sem Visualizar → sem Editar e sem Excluir"
    Ao desmarcar "Visualizar", os outros flags são zerados
    automaticamente (no Java via if(!viz) e no JS via cbxGuard()).

  • Soft-delete em todos os recursos:
    Usuários, Módulos e Perfis NUNCA são deletados do banco.
    O campo `ativo = FALSE` preserva o histórico de pedidos
    e auditoria.
═══════════════════════════════════════════════════════════════
