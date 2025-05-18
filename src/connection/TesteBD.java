/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package connection;

import connection.ConnectionBD; 
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public class TesteBD {

    public static void main(String[] args) {
        ConnectionBD connectionBDInstance = null;
        Connection sqlConnection = null;

        System.out.println("--- INICIANDO TESTE DE CONEXÃO ---");

        // Teste 1: Obter conexão pela primeira vez
        try {
            System.out.println("\n[TESTE 1] Tentando obter instância de ConnectionBD e conexão SQL...");
            connectionBDInstance = ConnectionBD.getInstance();
            System.out.println("Instância de ConnectionBD obtida: " + connectionBDInstance);

            sqlConnection = connectionBDInstance.getConnection();

            if (sqlConnection != null && !sqlConnection.isClosed()) {
                System.out.println("SUCESSO: Conexão com o banco de dados estabelecida!");
                DatabaseMetaData metaData = sqlConnection.getMetaData();
                System.out.println("  URL do Banco: " + metaData.getURL());
                System.out.println("  Usuário do Banco: " + metaData.getUserName());
                System.out.println("  Driver: " + metaData.getDriverName() + " " + metaData.getDriverVersion());
                System.out.println("  Produto do Banco: " + metaData.getDatabaseProductName() + " " + metaData.getDatabaseProductVersion());
            } else {
                System.err.println("FALHA: Não foi possível obter a conexão SQL ou ela está fechada.");
            }

        } catch (SQLException e) {
            System.err.println("ERRO DE SQL [TESTE 1]: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (connectionBDInstance != null) {
                try {
                    System.out.println("\n[TESTE 1] Tentando fechar a conexão...");
                    connectionBDInstance.closeConnection();
                    if (sqlConnection != null) { // sqlConnection pode ser null se getInstance() falhou
                         System.out.println("SUCESSO: Conexão fechada. Status (isClosed()): " + sqlConnection.isClosed());
                    } else {
                        System.out.println("INFO: Conexão BD fechada (sqlConnection era null, indicando falha anterior na obtenção).");
                    }
                } catch (SQLException e) {
                    System.err.println("ERRO DE SQL ao fechar conexão [TESTE 1]: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("INFO [TESTE 1]: Nenhuma instância de ConnectionBD para fechar (provavelmente falhou ao obter).");
            }
        }

        System.out.println("\n----------------------------------------------------");

        // Teste 2: Tentar obter a conexão novamente (deve reabrir)
        // Redefinir para garantir que não estamos usando a referência sqlConnection antiga
        sqlConnection = null;
        // connectionBDInstance já foi fechada, então getInstance() deve criar uma nova

        try {
            System.out.println("\n[TESTE 2] Tentando obter instância de ConnectionBD e conexão SQL novamente (após fechar)...");
            connectionBDInstance = ConnectionBD.getInstance(); // Deve criar uma nova instância ou reabrir
            System.out.println("Nova instância de ConnectionBD obtida: " + connectionBDInstance);

            sqlConnection = connectionBDInstance.getConnection();

            if (sqlConnection != null && !sqlConnection.isClosed()) {
                System.out.println("SUCESSO: Conexão com o banco de dados REABERTA!");
                DatabaseMetaData metaData = sqlConnection.getMetaData();
                System.out.println("  URL do Banco (reaberta): " + metaData.getURL());
            } else {
                System.err.println("FALHA: Não foi possível reabrir a conexão SQL ou ela está fechada.");
            }

        } catch (SQLException e) {
            System.err.println("ERRO DE SQL [TESTE 2]: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (connectionBDInstance != null) {
                try {
                    System.out.println("\n[TESTE 2] Tentando fechar a conexão reaberta...");
                    connectionBDInstance.closeConnection();
                     if (sqlConnection != null) {
                         System.out.println("SUCESSO: Conexão reaberta fechada. Status (isClosed()): " + sqlConnection.isClosed());
                    } else {
                         System.out.println("INFO: Conexão BD (reaberta) fechada (sqlConnection era null).");
                    }
                } catch (SQLException e) {
                    System.err.println("ERRO DE SQL ao fechar conexão reaberta [TESTE 2]: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                 System.out.println("INFO [TESTE 2]: Nenhuma instância de ConnectionBD para fechar.");
            }
        }

        System.out.println("\n--- TESTE DE CONEXÃO CONCLUÍDO ---");
    }
}