#include <libpq-fe.h>
#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <netinet/ip.h>
#include <netinet/in.h>
#include <unistd.h>
#include <pthread.h>

struct User {
  char username[50];
  char password[50];
  char accessibility[100];
  int flag;
};

PGconn *start_connection(PGconn *conn);
PGresult *send_query_insert(PGconn *conn, struct User user);
PGresult *send_query_select(PGconn *conn, struct User *user);
bool registration(struct User user, PGconn *conn);
bool login(struct User *user, PGconn *conn);
void *connection_handler(void *socket_desc);
void close_connection(PGconn *conn, PGresult *res);
void sendMessageToClient(char* message, int sock);

int main(int argc, char const *argv[]) {
  int socket_desc, client_sock, c, *new_sock;
	struct sockaddr_in server, client;

  // Crea il socket
	socket_desc = socket(AF_INET , SOCK_STREAM , 0);
	if (socket_desc == -1) {
		printf("Errore durante la creazione della socket");
	}

  // Prepara la struttura sockaddr_in
	server.sin_family = AF_INET;
	server.sin_addr.s_addr = INADDR_ANY;
	server.sin_port = htons(8080);

  // Effettua il binding
	if(bind(socket_desc, (struct sockaddr *)&server, sizeof(server)) < 0) {
		// Stampa il messaggio di errore
		perror("Bind fallito");
		exit(1);
	}

  // Metti in ascolto
	listen(socket_desc, 3);

  printf("\n BENVENUTO SUL SERVER DI MOVIEHUB \n\n");

  // Accetta una connessione in entrata
	printf("Server in ascolto su porta 8080...\n\n");
	c = sizeof(struct sockaddr_in);

	while ((client_sock = accept(socket_desc, (struct sockaddr *)&client, (socklen_t*)&c))) {
    printf("Connessione con il client stabilita\n\n");

    pthread_t sniffer_thread;
		new_sock = malloc(1);
		*new_sock = client_sock;

    if (pthread_create(&sniffer_thread, NULL, connection_handler, (void*) new_sock) < 0) {
      perror("Errore durante la creazione del thread\n\n");
			exit(1);
    }

    printf("Handler assegnato\n\n");
  }

  if (client_sock < 0) {
		perror("Connessione con il client fallita\n\n");
		exit(1);
	}

  return 0;
}

PGconn *start_connection(PGconn *conn) {
  char *conninfo = "host=localhost dbname=moviehub user=postgres password=moviehub2024";
  
  conn = PQconnectdb(conninfo);

  /* Controlla se la connessione è stata effettuata con successo */
  if (PQstatus(conn) != CONNECTION_OK) {
      fprintf(stderr, "Connessione al database fallita: %s\n", PQerrorMessage(conn));
      PQfinish(conn);
      exit(1);
  }

  return conn;
}

PGresult *send_query_insert(PGconn *conn, struct User user) {
  char query_insert[1000];
  sprintf(query_insert, "INSERT INTO users(username, password, accessibility) VALUES ('%s', '%s', '%s')", user.username, user.password, user.accessibility);

  PGresult *res = PQexec(conn, query_insert);
  if (PQresultStatus(res) != PGRES_COMMAND_OK) {
      fprintf(stderr, "Lo statement INSERT ha fallito: %s", PQerrorMessage(conn));
      PQclear(res);
      exit(1);
  }

  PQclear(res);
  return res;
}

PGresult *send_query_select(PGconn *conn, struct User *user) {
  char query_select[1000];

  if (user->flag > 0) {
    sprintf(query_select, "SELECT accessibility FROM users WHERE username='%s'", user->username);
  }
  else {
    sprintf(query_select, "SELECT accessibility FROM users WHERE username='%s' AND password='%s'", user->username, user->password);
  }

  PGresult *res = PQexec(conn, query_select);
  if (PQresultStatus(res) != PGRES_TUPLES_OK) {
      fprintf(stderr, "Lo statement SELECT ha fallito: %s", PQerrorMessage(conn));
      PQclear(res);
      exit(1);
  }

  int rows = PQntuples(res);
  if (rows > 0) {
    strcpy(user->accessibility, PQgetvalue(res, 0, 0));
  }
  
  PQclear(res);
  return res; 
}

bool registration(struct User user, PGconn *conn) {
  PGresult *res = send_query_insert(conn, user);
  close_connection(conn, res);

  return true;
}

bool login(struct User *user, PGconn *conn) {
  PGresult *res = send_query_select(conn, user);
  close_connection(conn, res);

  int rows = PQntuples(res);
  if (rows > 0) {
    return true;
  }
  else {
    return false;
  }
}

void *connection_handler(void *socket_desc) {
  // Ottieni il descrittore del socket
	int sock = *(int*)socket_desc;
  struct User user;
	int read_size;
  char message[255], client_message[2000];
  PGconn *conn;

  conn = start_connection(conn);

  // Ricevi un messaggio dal client
	while ((read_size = recv(sock, client_message, 2000, 0)) > 0 ) {
    char* token = strtok(client_message, ";");

    if (token != NULL) {
      if (strcmp(token, "Registrazione") == 0) {
        printf("Registrazione nuovo utente in corso...\n\n");

        token = strtok(NULL, ";");
        strcpy(user.username, token);

        token = strtok(NULL, ";");
        strcpy(user.password, token);

        token = strtok(NULL, ";");
        strcpy(user.accessibility, token);

        if (registration(user, conn)) {
          printf("Registrazione avvenuta con successo\n\n");
          strcpy(message, "OK");
        }
        else {
          printf("Errore durante la registrazione\n\n");
          strcpy(message, "Errore");
        }       
      }

      if (strcmp(token, "Login") == 0) {
        printf("Login utente in corso...\n\n");

        token = strtok(NULL, ";");
        strcpy(user.username, token);

        token = strtok(NULL, ";");
        strcpy(user.password, token);

        if (login(&user, conn)) {
          printf("Login avvenuto con successo\n\n");
          strcpy(message, user.accessibility);
        }
        else {
          printf("Errore durante il login\n\n");
          strcpy(message, "Errore");
        }
      }

      sendMessageToClient(message, sock); 
    }
	}

  if (read_size == 0)	{
		puts("Client disconnesso\n\n");
		fflush(stdout);
	}
	else if (read_size == -1)	{
		perror("recv failed");
	}

	// Libera il puntatore al socket
	free(socket_desc);

  return 0;
}

void sendMessageToClient(char* message, int sock) {
  if (write(sock, message, strlen(message)) < 0) {
    perror("Errore durante la scrittura sul socket\n\n");
	  exit(1);
  }
}

void close_connection(PGconn *conn, PGresult *res) {
    PQclear(res);
    PQfinish(conn);
}
